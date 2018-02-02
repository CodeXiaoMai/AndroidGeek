package com.xiaomai.geek.article.model

import java.io.Serializable

/**
 * Created by wangce on 2018/2/2.
 */
data class ArticleResponse(
        val category: List<Category>
) : Serializable

data class Category(
        val name: String, //RxJava
        val description: String, //RxJava Description
        val image: String, //http://reactivex.io/assets/Rx_Logo_S.png
        val url: String, //https://github.com/ReactiveX/RxJava
        val articles: List<Article>
) : Serializable

data class Article(
        val name: String, //给初学者的RxJava2.0教程(一)
        val url: String, //http://www.jianshu.com/p/464fa025229e
        val author: String, //佚名
        val keyword: String //rxjava/初学者
) : Serializable