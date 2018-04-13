package com.xiaomai.geek.common.utils

import android.content.Context
import android.content.Intent

/**
 * Created by wangce on 2018/2/23.
 */
object ShareUtils {

    fun share(context: Context, title: String, content: String? = null) {
        val stringBuilder = StringBuilder()
        stringBuilder.append(title)

        if (!content.isNullOrEmpty()) {
            stringBuilder.append("\n$content")
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, stringBuilder.toString())
        }
        context.startActivity(intent)
    }
}