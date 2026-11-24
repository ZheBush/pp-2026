package com.pp.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.pp.common.InterceptorPrefs
import com.pp.domain.usecase.DismissInterceptorUseCase
import com.pp.domain.usecase.HandleAppExitUseCase
import com.pp.domain.usecase.HandleAppLaunchUseCase
import com.pp.domain.usecase.ShowInterceptorUseCase
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

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var currentPackage: String? = null
    private var activeInterceptedPackage: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return

            if (packageName == this.packageName) return

            if (packageName != currentPackage) {
                val previousPackage = currentPackage
                currentPackage = packageName

                if (previousPackage != null) {
                    serviceScope.launch {
                        handleAppExitUseCase()
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
                        if (actions.shouldEnableGrayscale) {
                            enableGrayscaleMode(true)
                        }
                        if (actions.shouldShowInterceptor && !InterceptorPrefs.isBypassed(packageName)) {
                            activeInterceptedPackage = packageName
                            withContext(Dispatchers.Main) {
                                showInterceptorUseCase(packageName)
                            }
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
        }
    }

    override fun onDestroy() {
        serviceScope.launch(Dispatchers.Main) {
            dismissInterceptorUseCase()
        }
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun enableGrayscaleMode(enable: Boolean) {
        TODO("Not yet implemented")
    }
}