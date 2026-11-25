package com.pp.feature.grayscale.impl

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.pp.domain.controller.GrayscaleController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GrayscaleControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : GrayscaleController {

    override fun setGrayscaleEnabled(enabled: Boolean) {
        runCatching {
            val resolver = context.contentResolver
            if (enabled) {
                Settings.Secure.putInt(resolver, DALTONIZER_MODE_KEY, DALTONIZER_MONOCHROMACY)
                Settings.Secure.putInt(resolver, DALTONIZER_ENABLED_KEY, 1)
            } else {
                Settings.Secure.putInt(resolver, DALTONIZER_ENABLED_KEY, 0)
            }
        }.onFailure { error ->
            if (error is SecurityException) {
                Log.w(
                    TAG,
                    "Нет разрешения WRITE_SECURE_SETTINGS. Выдайте через ADB: " +
                            "adb shell pm grant ${context.packageName} android.permission.WRITE_SECURE_SETTINGS"
                )
            } else {
                Log.e(TAG, "Не удалось переключить чёрно-белый режим", error)
            }
        }
    }

    companion object {
        private const val TAG = "GrayscaleController"
        private const val DALTONIZER_ENABLED_KEY = "accessibility_display_daltonizer_enabled"
        private const val DALTONIZER_MODE_KEY = "accessibility_display_daltonizer"
        private const val DALTONIZER_MONOCHROMACY = 0
    }
}