package com.xiaomai.geek.base

import android.databinding.ViewDataBinding
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.xiaomai.geek.R
import kotlinx.android.synthetic.main.geek_base_activity.view.*
import kotlinx.android.synthetic.main.geek_base_list_layout.view.*

/**
 * Created by wangce on 2018/1/30.
 */
abstract class BaseListFragment<V, B : ViewDataBinding, M : BaseViewModel> : BaseViewModelFragment<M>() {

    var mAdapter: BaseAdapter<V, B>? = null

    override fun customView(inflater: LayoutInflater, rootView: View) {
        super.customView(inflater, rootView)
        val contentView = inflater.inflate(R.layout.geek_base_list_layout, rootView.content_view, false)
        mAdapter = getAdapter()
        contentView.recycler_view.let {
            it.layoutManager = getLayoutManager()
            it.adapter = mAdapter
        }

        rootView.content_view.addView(contentView)

        rootView.swipe_refresh_layout.setOnRefreshListener {
            loadList()
        }

        loadList()
    }

    abstract fun loadList()

    abstract fun getAdapter(): BaseAdapter<V, B>

    open fun getLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(context)
}