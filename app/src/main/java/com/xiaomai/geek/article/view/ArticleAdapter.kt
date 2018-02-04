package com.xiaomai.geek.article.view

import android.content.Intent
import com.xiaomai.geek.R
import com.xiaomai.geek.article.model.Article
import com.xiaomai.geek.base.BaseAdapter
import com.xiaomai.geek.common.Const
import com.xiaomai.geek.databinding.ArticleItemBinding

/**
 * Created by wangce on 2018/1/29.
 */
class ArticleAdapter : BaseAdapter<Article, ArticleItemBinding>(R.layout.article_item) {

    override fun onBindViewHolder(holder: Holder<ArticleItemBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            Intent(context, ArticleDetailActivity::class.java).apply {
                putExtra(Const.ARTICLE, getItem(position))
                context.startActivity(this)
            }
        }
    }
}