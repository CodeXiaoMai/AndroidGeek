package com.xiaomai.geek.base

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.xiaomai.geek.R
import kotlinx.android.synthetic.main.geek_base_activity.*
import kotlinx.android.synthetic.main.geek_base_list_layout.*

/**
 * Created by wangce on 2018/1/30.
 */
abstract class BaseListActivity<V, B : ViewDataBinding, M : BaseViewModel> : BaseViewModelActivity<M>() {

    var mAdapter: BaseAdapter<V, B>? = null

    override fun getLayoutId(): Int = R.layout.geek_base_list_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAdapter = getAdapter()
        recycler_view?.let {
            it.layoutManager = getLayoutManager()
            it.adapter = mAdapter
        }

        swipe_refresh_layout.setOnRefreshListener {
            loadList()
        }

        loadList()
    }

    abstract fun loadList()

    abstract fun getAdapter(): BaseAdapter<V, B>

    open fun getLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(this@BaseListActivity)
}