package com.xiaomai.geek.model.article.model

import com.xiaomai.geek.db.Article
import java.io.Serializable

/**
 * Created by wangce on 2018/2/2.
 */
data class ArticleResponse(
        val version: Long,
        val category: MutableList<Category>
) : Serializable

data class Category(
        val name: String, //RxJava
        val description: String, //RxJava Description
        val image: String, //http://reactivex.io/assets/Rx_Logo_S.png
        val url: String, //https://github.com/ReactiveX/RxJava
        val articles: MutableList<Article>
) : Serializable