package com.xiaomai.geek.common.utils

import android.content.Context
import android.content.Intent
import android.text.TextUtils

/**
 * Created by XiaoMai on 2017/5/26.
 */
object ShareUtils {

    @JvmStatic fun share(context: Context, title: String?, content: String?) {
        var stringBuilder: StringBuilder = StringBuilder()
        if (!TextUtils.isEmpty(title)) {
            stringBuilder.append(title)
        }
        if (!TextUtils.isEmpty(content)) {
            stringBuilder.append("\n").append(content)
        }
        var intent: Intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString())
        context.startActivity(Intent.createChooser(intent, "分享到"))
    }

    @JvmStatic fun share(context: Context, content: String) {
        share(context, content, "")
    }
}