package com.xiaomai.geek.article.model

import android.app.Application
import android.text.TextUtils
import com.google.gson.Gson
import com.xiaomai.geek.application.GeekApplication
import com.xiaomai.geek.common.wrapper.GeeKLog
import com.xiaomai.geek.db.Article
import io.reactivex.Observable
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by wangce on 2018/1/26.
 */
class ArticleLocalDataSource(private val context: Application) {
    private val TAG = this::class.java.simpleName
    private val FILE_NAME = "articleCategories"

    fun getCategoryResponseFromAssets(): Observable<CategoryResponse> {
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
                val articleResponses = Gson().fromJson<CategoryResponse>(stringBuilder.toString(), CategoryResponse::class.java)
                it.onNext(articleResponses)
                it.onComplete()
            } catch (e: Throwable) {
                it.onError(e)
            }
        }
    }

    fun saveArticles(categoryResponse: CategoryResponse) {
        GeeKLog.d(TAG, "start insert in db")

        if (categoryResponse.version > 0/*GeekApplication.ARTICLE_DAO_SESSION.configDao.queryBuilder()
                        .list()[0].version*/) {
            val localArticles = GeekApplication.ARTICLE_DAO_SESSION.articleDao.queryBuilder().list()

            val updateList = mutableListOf<Article>()

            val insertList = mutableListOf<Article>()
            // 插入更新
            categoryResponse.list.forEach { articleCategory ->
                articleCategory.articles.forEach { remoteArticle ->
                    if (localArticles.isEmpty()) {
                        insertList.add(remoteArticle)
                    } else {
                        if (!localArticles.contains(remoteArticle)) {
                            localArticles.forEach { localArticle ->
                                if (TextUtils.equals(localArticle.url, remoteArticle.url)) {
                                    localArticle.author = remoteArticle.author
                                    localArticle.keywords = remoteArticle.keywords
                                    localArticle.name = remoteArticle.name
                                    updateList.add(localArticle)
                                } else {
                                    insertList.add(remoteArticle)
                                }
                            }
                        }
                    }
                }
            }

            // 删除
            val deleteList = mutableListOf<Article>()

            localArticles.forEach { localArticle ->
                var hasValue = false
                categoryResponse.list.forEach {
                    it.articles.forEach { remoteArticle ->
                        if (localArticle.url == remoteArticle.url) {
                            hasValue = true
                        }
                    }
                }
                if (!hasValue) {
                    deleteList.add(localArticle)
                }
            }
            GeekApplication.ARTICLE_DAO_SESSION.articleDao.apply {
                insertInTx(insertList)
                GeeKLog.d(TAG, "insert ${insertList.size} items")
                deleteInTx(deleteList)
                GeeKLog.d(TAG, "delete ${deleteList.size} items")
                updateInTx(updateList)
                GeeKLog.d(TAG, "update ${updateList.size} items")
            }

        }
    }
}