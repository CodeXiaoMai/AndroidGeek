package com.xiaomai.geek.main

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.View
import com.xiaomai.geek.R
import com.xiaomai.geek.article.view.ArticleCategoryListFragment
import com.xiaomai.geek.base.BaseActivity
import com.xiaomai.geek.common.MenuItemView
import kotlinx.android.synthetic.main.main_activity.*

/**
 * Created by wangce on 2018/1/26.
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title_view.setTitle(getString(R.string.article))

        title_view.setOnBackClickListener(View.OnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        })

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.content, ArticleCategoryListFragment.newInstance(), ArticleCategoryListFragment::class.java.simpleName)
        transaction.commit()

        initDrawerView()
    }

    private fun initDrawerView() {
        val headerView = navigation_view.getHeaderView(0)
        val menuArticle = headerView.findViewById<MenuItemView>(R.id.menu_item_article)

        menuArticle.isSelected = true
        menuArticle.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getLayoutId(): Int = R.layout.main_activity

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }
}