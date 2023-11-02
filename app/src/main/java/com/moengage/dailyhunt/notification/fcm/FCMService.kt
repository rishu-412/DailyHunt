package com.moengage.dailyhunt.notification.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * FCM Service to handle push messages
 *
 */
class FCMService : FirebaseMessagingService() {

    private val tag = "FCMService"
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(tag, "onNewToken(): $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        NotificationHandler.createNotification(applicationContext, message.data)
    }

}