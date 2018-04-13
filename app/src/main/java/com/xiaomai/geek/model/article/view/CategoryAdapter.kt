package com.xiaomai.geek.model.article.view

import android.content.Intent
import android.widget.TextView
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.xiaomai.geek.R
import com.xiaomai.geek.base.BaseAdapter
import com.xiaomai.geek.common.Const
import com.xiaomai.geek.databinding.ArticleCategoryItemBinding
import com.xiaomai.geek.model.article.model.Category

/**
 * Created by wangce on 2018/1/29.
 */
class CategoryAdapter : BaseAdapter<Category, ArticleCategoryItemBinding>(R.layout.article_category_item) {

    private val colorGenerator by lazy {
        ColorGenerator.MATERIAL
    }

    override fun onBindViewHolder(holder: Holder<ArticleCategoryItemBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.itemView.findViewById<TextView>(R.id.tv_name).setBackgroundColor(colorGenerator.getColor(item.name))
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            Intent(context, ArticleListActivity::class.java).apply {
                putExtra(Const.ARTICLE, item)
                context.startActivity(this)
            }
        }
    }
}