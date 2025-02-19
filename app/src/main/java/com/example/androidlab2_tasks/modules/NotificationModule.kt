package com.example.androidlab2_tasks.modules

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.androidlab2_tasks.CreateTaskActivity
import com.example.androidlab2_tasks.R


class NotificationModule(val context: Context) {

    private val channelId = "i.apps.notifications" // Unique channel ID for notifications
    private val description = "Test notification"  // Description for the notification channel

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                description,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true) // Turn on notification light
                lightColor = Color.GREEN
                enableVibration(true) // Allow vibration for notifications
            }

            val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
    }

    /**
     * Build and send a notification with a custom layout and action.
     */
    @SuppressLint("MissingPermission")
    fun sendNotification(id: Int, description: String) {
        // Intent that triggers when the notification is tapped
        val intent = Intent(context, CreateTaskActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("task_id", id)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        // Build the notification
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Notification icon
            .setContentTitle("Available task") // Title displayed in the notification
            .setContentText(description) // Text displayed in the notification
            .setAutoCancel(true) // Dismiss notification when tapped
            .setContentIntent(pendingIntent) // Pending intent triggered when tapped
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Notification priority for better visibility

        val notification = builder.build()
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;

        // Display the notification
        with(NotificationManagerCompat.from(context)) {
            notify(id, notification)
        }
    }
}