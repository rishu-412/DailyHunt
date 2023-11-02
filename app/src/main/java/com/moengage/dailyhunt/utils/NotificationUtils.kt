package com.moengage.dailyhunt.utils

import android.Manifest
import android.app.Notification
import android.app.Notification.Builder
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.moengage.dailyhunt.R
import org.json.JSONObject

private const val TAG = "NotificationUtils"
private const val DEFAULT_NOTIFICATION_CHANNEL_ID = "101"
private const val DEFAULT_NOTIFICATION_CHANNEL_NAME = "News Article"
private const val DEFAULT_NOTIFICATION_CHANNEL_DESCRIPTION = "Provides updates for new articles"

/**
 * Created the notification channels if the SDK version >= 26
 *
 * @param context instance of [Context]
 */
fun createNotificationChannels(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        //Prepare Notification Channel
        val channel = NotificationChannel(
            DEFAULT_NOTIFICATION_CHANNEL_ID,
            DEFAULT_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = DEFAULT_NOTIFICATION_CHANNEL_DESCRIPTION
        }

        // Register the channel with the system.
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}
