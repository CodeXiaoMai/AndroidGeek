package com.xiaomai.geek.common.utils

import android.app.Application
import com.google.gson.Gson
import com.xiaomai.geek.common.wrapper.GeeKLog
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by wangce on 2018/3/5.
 */
object AssetUtil {

    fun <T> readFromAsset(context: Application, fileName: String, classOf: Class<T>): T? {
        var inputStreamReader: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null

        try {
            inputStreamReader = InputStreamReader(context.assets.open(fileName))
            bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var line: String?
            var hasNext = true
            while (hasNext) {
                line = bufferedReader.readLine()
                hasNext = line != null
                if (hasNext) {
                    stringBuilder.append(line)
                }
            }
            GeeKLog.json(stringBuilder.toString())
            return Gson().fromJson<T>(stringBuilder.toString(), classOf)
        } catch (e: Exception) {
            inputStreamReader?.close()
            bufferedReader?.close()
            return null
        }
    }
}
