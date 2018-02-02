package com.xiaomai.geek.article.model

import com.xiaomai.geek.base.BaseDataSource
import io.reactivex.Observable

/**
 * Created by wangce on 2018/1/26.
 *
 * 先从 Server 获取数据，再保存到本地数据库，然后根据本地文章阅读情况展示数据
 */
class ArticleRepository(private val articleLocalDataSource: ArticleLocalDataSource,
                        private val articleRemoteDataSource: ArticleRemoteDataSource
) : ArticleDataSource, BaseDataSource() {

    override fun getArticleResponse(): Observable<ArticleResponse> {
        return articleLocalDataSource.getArticleResponseFromAssets()
    }

}