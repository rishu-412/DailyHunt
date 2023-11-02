package com.moengage.dailyhunt.ui.utils

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private const val TAG = "DateTimeUtils"

fun getFormattedNewsArticleTime(timeString: String): String {
    try {
        val tz: TimeZone = TimeZone.getTimeZone("UTC")
        val iSODateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        iSODateFormat.timeZone = tz
        val date = iSODateFormat.parse(timeString) ?: return ""
        return DateFormat.getDateInstance(DateFormat.MONTH_FIELD).format(date)
    } catch (t: Throwable) {
        Log.i(TAG, "getFormattedNewsArticleTime(): ", t)
        return ""
    }
}

fun getSystemTimeSeconds(): Int {
    return (System.currentTimeMillis() / 1000).toInt()
}