package com.moengage.dailyhunt.core.data.repository

import android.content.Context


private const val PREF_NAME = "daily_hunt"
private const val PREF_NOTIFICATION_REQUEST_COUNT = "pref_notification_request_count"

/**
 * Repository to manage shared preferences
 *
 * @param applicationContext instance of [Context]
 */
class PrefRepository(applicationContext: Context) {

    private val sharedPreferences =
        applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val editor = sharedPreferences.edit()

    /**
     * Increments the notification request count by 1.
     */
    fun incrementNotificationRequestCount() {
        var storedValue = getNotificationRequestCount()
        editor.putInt(PREF_NOTIFICATION_REQUEST_COUNT, ++storedValue)
        editor.commit()
    }

    /**
     * Gets current notification request count
     *
     * @return [Int] for notification request count. Default value 0.
     */
    fun getNotificationRequestCount(): Int {
        return sharedPreferences.getInt(PREF_NOTIFICATION_REQUEST_COUNT, 0)
    }


}