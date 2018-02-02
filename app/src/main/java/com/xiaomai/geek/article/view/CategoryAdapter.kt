package com.xiaomai.geek.article.view

import android.content.Intent
import com.xiaomai.geek.R
import com.xiaomai.geek.article.model.Category
import com.xiaomai.geek.base.BaseAdapter
import com.xiaomai.geek.databinding.ArticleCategoryItemBinding

/**
 * Created by wangce on 2018/1/29.
 */
class CategoryAdapter : BaseAdapter<Category, ArticleCategoryItemBinding>(R.layout.article_category_item) {

    override fun onBindViewHolder(holder: Holder<ArticleCategoryItemBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            Intent(context, ArticleListActivity::class.java).apply {
                putExtra(ArticleListActivity.DATA, getItem(position))
                context.startActivity(this)
            }
        }
    }
}