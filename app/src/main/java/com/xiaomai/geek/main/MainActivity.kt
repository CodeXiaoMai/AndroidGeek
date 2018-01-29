package com.xiaomai.geek.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.xiaomai.geek.R
import com.xiaomai.geek.article.viewmodel.ArticleViewModel
import com.xiaomai.geek.base.BaseActivity
import com.xiaomai.geek.common.wrapper.GeeKLog
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by wangce on 2018/1/26.
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)

        viewModel.getArticles().observe(this, Observer {
            GeeKLog.d(TAG, "size = ${it?.size}")
        })

        button.setOnClickListener {
            viewModel.loadArticles()
        }

    }
}