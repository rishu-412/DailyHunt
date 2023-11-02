package com.moengage.dailyhunt.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build


/**
 * Checks the internet connectivity of the device
 *
 * @param context instance of [Context]
 * @return true if connected to the internet otherwise false
 */
fun hasInternetConnection(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm.activeNetwork != null
    } else {
        cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }
}