package com.keeththigan.elyra.core.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.keeththigan.elyra.R

/**
 * Posts system notifications for safety-critical events.
 *
 * Raised locally by whichever client detects the condition, since Cloud
 * Messaging would need a backend sender and the project runs on the Firebase
 * free tier. The history lives in Firestore, so all clients see the same list.
 */
class ElyraNotifier(
    private val context: Context
) {

    init {
        createChannel()
    }

    private fun createChannel() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_SAFETY,
            "Safety alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description =
                "Alerts when a safety-critical appliance is switched off " +
                    "automatically or a device reports a fault."
        }

        val manager =
            context.getSystemService(NotificationManager::class.java)

        manager?.createNotificationChannel(channel)
    }

    fun notifySafetyAlert(
        id: Int,
        title: String,
        message: String
    ) {

        if (!hasPermission()) return

        val notification =
            NotificationCompat.Builder(context, CHANNEL_SAFETY)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(
                    NotificationCompat.BigTextStyle().bigText(message)
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

        runCatching {
            NotificationManagerCompat.from(context).notify(id, notification)
        }
    }

    private fun hasPermission(): Boolean {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val CHANNEL_SAFETY = "elyra_safety_alerts"
    }
}
