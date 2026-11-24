package com.pp.feature.interceptor.impl

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.pp.domain.InterceptorController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterceptorControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
): InterceptorController {
    private val windowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private var overlayView: ComposeView? = null
    private var overlayLifecycleOwner: OverlayLifecycleOwner? = null

    override fun show(targetPackage: String) {
        if (!Settings.canDrawOverlays(context)) return
        if (overlayView != null) return

        val lifecycleOwner = OverlayLifecycleOwner().apply {
            onCreate()
            onStart()
            onResume()
        }

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
            dimAmount = 0.75f
        }

        val composeView = ComposeView(context).apply {
            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            setContent {
                InterceptorOverlayContent(
                    targetPackage = targetPackage,
                    onCancelClick = {
                        goHome()
                        dismiss()
                    },
                    onAllowClick = {
                        dismiss()
                    }
                )
            }
        }

        overlayLifecycleOwner = lifecycleOwner
        overlayView = composeView

        runCatching {
            windowManager.addView(composeView, layoutParams)
        }.onFailure {
            dismiss()
        }
    }

    override fun dismiss() {
        val view = overlayView ?: return
        overlayView = null

        runCatching {
            windowManager.removeViewImmediate(view)
        }.onFailure {
            runCatching { windowManager.removeView(view) }
        }

        overlayLifecycleOwner?.destroy()
        overlayLifecycleOwner = null
    }

    private fun goHome() {
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(homeIntent)
    }
}