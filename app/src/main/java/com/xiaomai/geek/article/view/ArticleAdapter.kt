package com.xiaomai.geek.article.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.xiaomai.geek.article.model.ArticleResponse
import com.xiaomai.geek.databinding.ArticleItemBinding

/**
 * Created by wangce on 2018/1/29.
 */
class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.Companion.Holder>() {

    var articles: List<ArticleResponse>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val articleItemBinding = ArticleItemBinding.inflate(layoutInflater, parent, false)
        return Holder(articleItemBinding)
    }

    override fun getItemCount(): Int {
        return articles?.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        articles?.let {
            holder?.bind(articles?.get(position))
        }
    }

    companion object {
        class Holder(private val binding: ArticleItemBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(article: ArticleResponse?) {
                article.let {
                    binding.article = it
                    binding.executePendingBindings()
                }
            }
        }
    }

}