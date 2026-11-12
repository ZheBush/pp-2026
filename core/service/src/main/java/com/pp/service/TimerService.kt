package com.pp.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class TimerService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())
    private var secondsElapsed = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val packageName = intent?.getStringExtra("PACKAGE_NAME") ?: "App"

        startForeground(NOTIFICATION_ID, createNotification("Время: 00:00"))
        startTimer(packageName)

        return START_NOT_STICKY
    }

    private fun startTimer(packageName: String) {
        serviceScope.launch {
            while (true) {
                delay(1000L.milliseconds)
                secondsElapsed++
                updateNotification(formatTime(secondsElapsed))
            }
        }
    }

    private fun formatTime(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", mins, secs)
    }

    private fun createNotification(text: String): Notification {
        TODO("Not yet implemented")
    }

    private fun updateNotification(text: String) {
        TODO("Not yet implemented")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
    }
}