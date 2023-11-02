package com.moengage.dailyhunt.notification

import android.Manifest
import android.app.Notification
import android.app.Notification.Builder
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.moengage.dailyhunt.R
import com.moengage.dailyhunt.ui.activities.MainActivity
import com.moengage.dailyhunt.ui.utils.getSystemTimeSeconds
import org.json.JSONObject

private const val TAG = "NotificationUtils"
private const val DEFAULT_NOTIFICATION_CHANNEL_ID = "101"
private const val DEFAULT_NOTIFICATION_CHANNEL_NAME = "News Article"
private const val DEFAULT_NOTIFICATION_CHANNEL_DESCRIPTION = "Provides updates for new articles"


private const val NOTIFICATION_ATTRIBUTE_TITLE = "news_title"
private const val NOTIFICATION_ATTRIBUTE_DESCRIPTION = "news_description"

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

/**
 * Creates and Show the notification
 *
 * @param context instance of [Context]
 * @param notificationData notification data received in onMessageReceived() of
 * FirebaseMessagingService
 */
fun createNotification(context: Context, notificationData: Map<String, String>) {
    try {
        Log.i(TAG, "createNotification(): Will try to create and show the notification")
        val notificationPayload = JSONObject(notificationData)
        Log.i(TAG, "createNotification(): Notification Payload=$notificationPayload")

        // Prepare notification
        val notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, DEFAULT_NOTIFICATION_CHANNEL_ID)
        } else {
            Notification.Builder(context)
        }

        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notificationPayload.getString(NOTIFICATION_ATTRIBUTE_TITLE))
            .setContentText(notificationPayload.getString(NOTIFICATION_ATTRIBUTE_DESCRIPTION))
            .setAutoCancel(true)

        prepareNotificationForTapAction(context, notificationBuilder, notificationPayload)
        prepareNotificationForReadActionButton(context, notificationBuilder, notificationPayload)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(
                    TAG,
                    "createNotification(): App does not have notification permission. Discarding notification display"
                )
                return
            }
            notify(getSystemTimeSeconds(), notificationBuilder.build())
        }
    } catch (t: Throwable) {
        Log.e(TAG, "createNotification: ", t)
    }
}

/**
 * Prepare Notification tap action
 *
 * @param context instance of [Context]
 * @param notificationBuilder instance of [Notification.Builder]
 * @param payload notification payload
 */
private fun prepareNotificationForTapAction(
    context: Context, notificationBuilder: Notification.Builder, payload: JSONObject
) {
    val handlerActivityIntent =
        MainActivity.getNotificationActionIntent(context, payload.toString())
    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 0, handlerActivityIntent, PendingIntent.FLAG_IMMUTABLE)

    notificationBuilder.setContentIntent(pendingIntent)
}

/**
 * Prepare Notification for Read Action Button
 *
 * @param context instance of [Context]
 * @param notificationBuilder instance of [Notification.Builder]
 * @param payload notification payload
 */
private fun prepareNotificationForReadActionButton(
    context: Context, notificationBuilder: Builder, payload: JSONObject
) {
    val handleActivityIntent = MainActivity.getNotificationActionIntent(context, payload.toString())
    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 0, handleActivityIntent, PendingIntent.FLAG_IMMUTABLE)

    val readAction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Notification.Action.Builder(
            Icon.createWithResource(context, R.drawable.ic_launcher_foreground),
            "Read",
            pendingIntent
        )
    } else {
        Notification.Action.Builder(
            R.drawable.ic_launcher_foreground, "Read", pendingIntent
        )
    }
    notificationBuilder.addAction(readAction.build())
}