package com.xiaomai.geek.main

import android.content.Intent
import android.os.Bundle
import com.xiaomai.geek.R
import com.xiaomai.geek.article.view.ArticleListActivity
import com.xiaomai.geek.base.BaseActivity

/**
 * Created by wangce on 2018/1/26.
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this@MainActivity, ArticleListActivity::class.java))
    }

    override fun getLayoutId(): Int = R.layout.main_activity
}