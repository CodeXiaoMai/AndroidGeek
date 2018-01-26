package com.xiaomai.geek.article.model

/**
 * Created by wangce on 2018/1/26.
 */
data class ArticleResponse(
        val name: String = "",
        val description: String = "",
        val image: String = "",
        val url: String? = "",
        val owner: String? = "",
        val repoName: String? = "",
        val articles: List<Article>? = null
)