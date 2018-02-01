package com.xiaomai.geek.article.model

import com.xiaomai.geek.db.ArticleCategory

/**
 * Created by wangce on 2018/2/1.
 */
class CategoryResponse(
        var version: Int,
        var list: List<ArticleCategory>
)