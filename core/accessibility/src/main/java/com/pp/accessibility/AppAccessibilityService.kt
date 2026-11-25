package com.pp.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.pp.common.InterceptorPrefs
import com.pp.domain.usecase.HandleAppExitUseCase
import com.pp.domain.usecase.HandleAppLaunchUseCase
import com.pp.domain.usecase.grayscale.SetGrayscaleUseCase
import com.pp.domain.usecase.interceptor.DismissInterceptorUseCase
import com.pp.domain.usecase.interceptor.ShowInterceptorUseCase
import com.pp.domain.usecase.timer.StartTimerUseCase
import com.pp.domain.usecase.timer.StopTimerUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class AppAccessibilityService : AccessibilityService() {

    @Inject lateinit var handleAppLaunchUseCase: HandleAppLaunchUseCase
    @Inject lateinit var handleAppExitUseCase: HandleAppExitUseCase
    @Inject lateinit var showInterceptorUseCase: ShowInterceptorUseCase
    @Inject lateinit var dismissInterceptorUseCase: DismissInterceptorUseCase
    @Inject lateinit var startTimerUseCase: StartTimerUseCase
    @Inject lateinit var stopTimerUseCase: StopTimerUseCase
    @Inject lateinit var setGrayscaleUseCase: SetGrayscaleUseCase

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var currentPackage: String? = null
    private var activeInterceptedPackage: String? = null

    private val ignoredPackages = setOf(
        "com.android.systemui",
        "android",
        "com.android.launcher",
        "com.android.launcher3",
        "com.google.android.apps.nexuslauncher"
    )

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return

            if (packageName == this.packageName || packageName in ignoredPackages) return

            if (packageName != currentPackage) {
                val previousPackage = currentPackage
                currentPackage = packageName

                if (previousPackage != null) {
                    serviceScope.launch {
                        handleAppExitUseCase()
                        stopTimerUseCase()
                        setGrayscaleUseCase(false)
                        if (activeInterceptedPackage != null && activeInterceptedPackage != packageName) {
                            activeInterceptedPackage = null
                            withContext(Dispatchers.Main) {
                                dismissInterceptorUseCase()
                            }
                        }
                    }
                    InterceptorPrefs.clearIfDifferent(packageName)
                }

                serviceScope.launch {
                    val actions = handleAppLaunchUseCase(packageName)
                    if (actions != null) {
                        if (actions.shouldShowInterceptor && !InterceptorPrefs.isBypassed(packageName)) {
                            activeInterceptedPackage = packageName
                            withContext(Dispatchers.Main) {
                                showInterceptorUseCase(packageName)
                            }
                        }
                        if (actions.shouldTimer) {
                            startTimerUseCase(packageName)
                        }
                        if (actions.shouldEnableGrayscale) {
                            Log.d("GrayscaleController", "enabled")
                            setGrayscaleUseCase(true)
                        }
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        currentPackage = null
        activeInterceptedPackage = null
        serviceScope.launch(Dispatchers.Main) {
            dismissInterceptorUseCase()
            stopTimerUseCase()
            setGrayscaleUseCase(false)
        }
    }

    override fun onDestroy() {
        serviceScope.launch(Dispatchers.Main) {
            dismissInterceptorUseCase()
            stopTimerUseCase
            setGrayscaleUseCase(false)
        }
        serviceScope.cancel()
        super.onDestroy()
    }
}