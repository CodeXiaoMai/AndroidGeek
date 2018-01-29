package com.xiaomai.geek.article.model

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xiaomai.geek.common.wrapper.GeeKLog
import io.reactivex.Observable
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by wangce on 2018/1/26.
 */
class ArticleLocalDataSource(private val context: Application) : ArticleDataSource {
    private val FILE_NAME = "chapters"

    override fun getArticles(): Observable<List<ArticleResponse>> {
        return Observable.create {
            var inputStreamReader: InputStreamReader
            var bufferedReader: BufferedReader

            try {
                inputStreamReader = InputStreamReader(context.assets.open(FILE_NAME))
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
                val typeToken = object : TypeToken<List<ArticleResponse>>() {}.type
                val articleResponses = Gson().fromJson<List<ArticleResponse>>(stringBuilder.toString(), typeToken)
                it.onNext(articleResponses)
                it.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                it.onError(e)
            }

        }
    }
}