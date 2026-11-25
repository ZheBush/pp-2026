package com.pp.impl

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class TimerForegroundService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var timerJob: Job? = null
    private var secondsElapsed = 0
    private var targetPackage: String = ""

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val nextPackage = intent?.getStringExtra(EXTRA_TARGET_PACKAGE).orEmpty()

        if (nextPackage.isNotEmpty() && nextPackage == targetPackage && timerJob?.isActive == true) {
            return START_NOT_STICKY
        }

        targetPackage = nextPackage
        secondsElapsed = 0

        startForeground(NOTIFICATION_ID, createNotification(formatElapsed(secondsElapsed)))
        restartTimer()

        return START_NOT_STICKY
    }

    private fun restartTimer() {
        timerJob?.cancel()
        timerJob = serviceScope.launch {
            while (isActive) {
                delay(1000L.milliseconds)
                secondsElapsed++
                updateNotification(formatElapsed(secondsElapsed))
            }
        }
    }

    private fun formatElapsed(totalSeconds: Int): String {
        val mins = totalSeconds / 60
        val secs = totalSeconds % 60
        return String.format(Locale.US, "%02d:%02d", mins, secs)
    }

    private fun createNotification(elapsedText: String): Notification {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)?.let { intent ->
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val appLabel = getAppLabel(targetPackage)
        val contentText = if (appLabel.isNotBlank()) {
            "$appLabel: $elapsedText"
        } else {
            "Время сессии: $elapsedText"
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Контроль экранного времени")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_menu_today)
            .setContentIntent(launchIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun updateNotification(elapsedText: String) {
        val notification = createNotification(elapsedText)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun getAppLabel(pkg: String): String {
        if (pkg.isBlank()) return ""
        return runCatching {
            val appInfo = packageManager.getApplicationInfo(pkg, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        }.getOrDefault(pkg)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Таймер сессии",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Показывает длительность текущей сессии в отслеживаемом приложении"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        timerJob?.cancel()
        serviceScope.cancel()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_TARGET_PACKAGE = "extra_target_package"
        private const val CHANNEL_ID = "timer_service_channel_id"
        private const val NOTIFICATION_ID = 1001
    }

}