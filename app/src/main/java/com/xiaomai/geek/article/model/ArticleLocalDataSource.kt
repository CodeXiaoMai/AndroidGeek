package com.xiaomai.geek.article.model

import android.app.Application
import com.google.gson.Gson
import com.xiaomai.geek.application.GeekApplication
import com.xiaomai.geek.common.wrapper.GeeKLog
import com.xiaomai.geek.db.ArticleRecord
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

    fun saveArticleRecord(articleRecord: ArticleRecord): Observable<Boolean> {
        return create {
            GeekApplication.DAO_SESSION.articleRecordDao.apply {
                val resultList = queryRaw("where url = ?", articleRecord.url)
                if (resultList.isEmpty()) {
                    // 如果数据库中没有保存此 url，插入数据库
                    articleRecord.times = 1
                    val insert = insert(articleRecord)
                    it.onNext(insert > 0)
                    it.onComplete()
                } else {
                    // 如果数据库中有此 url，更新数据库
                    resultList[0].apply {
                        url = articleRecord.url
                        name = articleRecord.name
                        author = articleRecord.author
                        keywords = articleRecord.keywords
                        progress = articleRecord.progress
                        readTime = articleRecord.readTime
                        times++
                        update(this)
                        it.onNext(true)
                        it.onComplete()
                    }
                }
            }
        }
    }

    fun readArticleRecord(article: Article): Observable<ArticleRecord> {
        return create {
            GeekApplication.DAO_SESSION.articleRecordDao.apply {
                val list = queryRaw("where url = ?", article.url)
                if (list.isNotEmpty()) {
                    it.onNext(list[0])
                    it.onComplete()
                } else {
                    it.onError(Throwable("no record"))
                }
            }
        }
    }
}