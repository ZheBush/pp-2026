package com.pp.impl

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.pp.domain.controller.TimerController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.java

@Singleton
class TimerControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TimerController {

    override fun start(targetPackage: String) {
        if (targetPackage == context.packageName) return

        val intent = Intent(context, TimerForegroundService::class.java).apply {
            putExtra(TimerForegroundService.EXTRA_TARGET_PACKAGE, targetPackage)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    override fun stop() {
        val intent = Intent(context, TimerForegroundService::class.java)
        context.stopService(intent)
    }
}