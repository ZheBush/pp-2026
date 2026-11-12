package com.pp.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.pp.domain.usecase.HandleAppExitUseCase
import com.pp.domain.usecase.HandleAppLaunchUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppAccessibilityService : AccessibilityService() {

    @Inject lateinit var handleAppLaunchUseCase: HandleAppLaunchUseCase
    @Inject lateinit var handleAppExitUseCase: HandleAppExitUseCase

    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private var currentPackage: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return

            if (packageName != currentPackage) {
                if (currentPackage != null) {
                    serviceScope.launch { handleAppExitUseCase() }
                }
                currentPackage = packageName

                serviceScope.launch {
                    val actions = handleAppLaunchUseCase(packageName)
                    if (actions != null) {
                        if (actions.shouldEnableGrayscale) {
                            enableGrayscaleMode(true)
                        }
                        if (actions.shouldShowInterceptor) {
                            launchInterceptorOverlay(packageName)
                        }
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        currentPackage = null
    }

    private fun enableGrayscaleMode(enable: Boolean) {
        TODO("Not yet implemented")
    }

    private fun launchInterceptorOverlay(packageName: String) {
        TODO("Not yet implemented")
    }
}