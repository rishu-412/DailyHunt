package com.moengage.dailyhunt.notification.fcm

import android.Manifest
import android.app.Notification
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
import com.moengage.dailyhunt.utils.getSystemTimeSeconds
import org.json.JSONObject


private const val DEFAULT_NOTIFICATION_CHANNEL_ID = "101"

private const val NOTIFICATION_ATTRIBUTE_TITLE = "news_title"
private const val NOTIFICATION_ATTRIBUTE_DESCRIPTION = "news_description"
private const val NOTIFICATION_ATTRIBUTE_NEWS_URL = "news_url"

object NotificationHandler {

    private val tag = "NotificationHandler"

    private val lock = Any()

    /**
     * Creates and Show the notification
     *
     * @param context instance of [Context]
     * @param notificationData notification data received in onMessageReceived() of
     * FirebaseMessagingService
     */
    fun createNotification(context: Context, notificationData: Map<String, String>) {
        synchronized(lock) {
            try {
                Log.i(tag, "createNotification(): Will try to create and show the notification")
                val notificationPayload = JSONObject(notificationData)
                Log.i(tag, "createNotification(): Notification Payload=$notificationPayload")

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
                prepareNotificationForReadActionButton(
                    context,
                    notificationBuilder,
                    notificationPayload
                )

                with(NotificationManagerCompat.from(context)) {
                    if (ActivityCompat.checkSelfPermission(
                            context, Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.i(
                            tag,
                            "createNotification(): App does not have notification permission. Discarding notification display"
                        )
                        return
                    }
                    notify(getSystemTimeSeconds(), notificationBuilder.build())
                }
            } catch (t: Throwable) {
                Log.e(tag, "createNotification: ", t)
            }
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
        val handlerActivityIntent = getNotificationActionIntent(payload)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                handlerActivityIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

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
        context: Context, notificationBuilder: Notification.Builder, payload: JSONObject
    ) {
        val handleActivityIntent = getNotificationActionIntent(payload)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                handleActivityIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

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

    /**
     * Provides notification redirection intent
     *
     * @param payload instance of [JSONObject]
     * @return [Intent]
     */
    private fun getNotificationActionIntent(payload: JSONObject): Intent {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            data = Uri.parse(payload.getString(NOTIFICATION_ATTRIBUTE_NEWS_URL))
        }
        return intent
    }
}