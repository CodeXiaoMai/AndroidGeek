package com.xiaomai.geek.article.model

import android.app.Application
import com.google.gson.Gson
import com.xiaomai.geek.common.wrapper.GeeKLog
import io.reactivex.Observable
import io.reactivex.Observable.create
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by wangce on 2018/1/26.
 */
class ArticleLocalDataSource(private val context: Application) {
    private val TAG = this::class.java.simpleName
    private val FILE_NAME = "articles"

    fun getArticleResponseFromAssets(): Observable<ArticleResponse> {
        return create {
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
                val articleResponses = Gson().fromJson<ArticleResponse>(stringBuilder.toString(), ArticleResponse::class.java)
                it.onNext(articleResponses)
                it.onComplete()
            } catch (e: Throwable) {
                it.onError(e)
            }
        }
    }
}