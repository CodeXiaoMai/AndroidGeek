package com.xiaomai.geek.common.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by wangce on 2018/1/31.
 */
class NetworkUtil {
    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val networkInfo = getNetworkInfo(context)
            return networkInfo != null && networkInfo.isAvailable
        }

        fun isWifiAvailable(context: Context): Boolean {
            val networkInfo = getNetworkInfo(context)
            return networkInfo != null
                    && networkInfo.isAvailable
                    && networkInfo.type == ConnectivityManager.TYPE_WIFI
        }

        private fun getNetworkInfo(context: Context): NetworkInfo? {
            val connectivityManager: ConnectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo
        }
    }
}