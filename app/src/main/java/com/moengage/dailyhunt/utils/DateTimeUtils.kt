package com.moengage.dailyhunt.utils

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private const val TAG = "DateTimeUtils"

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

fun getSystemTimeSeconds(): Int {
    return (System.currentTimeMillis() / 1000).toInt()
}