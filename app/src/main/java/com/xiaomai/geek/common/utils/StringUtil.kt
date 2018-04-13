package com.xiaomai.geek.common.utils

import android.text.TextUtils
import android.util.Base64

/**
 * Created by wangce on 2018/1/26.
 */
object StringUtil {
    fun base64Decode(originalString: String): String {
        return if (TextUtils.isEmpty(originalString)) "" else String(Base64.decode(originalString, Base64.DEFAULT))
    }
}