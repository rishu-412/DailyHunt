package com.moengage.dailyhunt.utils

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private const val TAG = "DateTimeUtils"

/**
 * Parses date from ISO timestamp.
 *
 * @param timeString
 * @return [String] of formatted date in [DateFormat.LONG] format
 */
fun getFormattedNewsArticleTime(timeString: String): String {
    try {
        val iSODateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val date = iSODateFormat.parse(timeString) ?: return ""
        return DateFormat.getDateInstance(DateFormat.LONG).format(date)
    } catch (t: Throwable) {
        Log.i(TAG, "getFormattedNewsArticleTime(): ", t)
        return ""
    }
}

/**
 * Provides system time in seconds type casted to [Int]
 *
 * @return [Int] for the system time seconds
 */
fun getSystemTimeSeconds(): Int {
    return (System.currentTimeMillis() / 1000).toInt()
}