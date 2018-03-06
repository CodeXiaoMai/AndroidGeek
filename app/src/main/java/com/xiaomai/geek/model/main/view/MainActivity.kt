package com.xiaomai.geek.model.main.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.View
import com.xiaomai.geek.R
import com.xiaomai.geek.base.BaseViewModelActivity
import com.xiaomai.geek.common.MenuItemView
import com.xiaomai.geek.model.article.view.ArticleCategoryListFragment
import com.xiaomai.geek.model.main.viewmodel.MainViewModel
import com.xiaomai.geek.model.setting.view.SettingActivity
import com.xiaomai.geek.model.todo.view.TasksListActivity
import kotlinx.android.synthetic.main.main_activity.*

/**
 * Created by wangce on 2018/1/26.
 */
class MainActivity : BaseViewModelActivity<MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title_view.setTitle(getString(R.string.article))

        title_view.setBackView(R.drawable.menu_nav, View.OnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        })

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.content, ArticleCategoryListFragment.newInstance(), ArticleCategoryListFragment::class.java.simpleName)
        transaction.commit()

        initDrawerView()
    }

    override fun getViewModelClazz(): Class<MainViewModel> = MainViewModel::class.java

    override fun onResume() {
        super.onResume()

        viewModel.getConfig()
    }

    private fun initDrawerView() {
        val headerView = navigation_view.getHeaderView(0)
        val menuArticle = headerView.findViewById<MenuItemView>(R.id.menu_item_article)
        val menuToDo = headerView.findViewById<MenuItemView>(R.id.menu_item_to_do)
        val menuSetting = headerView.findViewById<MenuItemView>(R.id.menu_item_settings)

        menuArticle.isSelected = true
        menuArticle.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        menuToDo.setOnClickListener {
            startActivity(Intent(this@MainActivity, TasksListActivity::class.java))
        }

        menuSetting.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingActivity::class.java))
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