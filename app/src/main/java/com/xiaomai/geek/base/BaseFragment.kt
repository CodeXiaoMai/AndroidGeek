package com.xiaomai.geek.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xiaomai.geek.R
import kotlinx.android.synthetic.main.geek_base_activity.view.*
import kotlinx.android.synthetic.main.geek_empty_view.view.*
import kotlinx.android.synthetic.main.geek_error_view.view.*

/**
 * Created by wangce on 2018/2/22.
 */
abstract class BaseFragment : Fragment() {

    private lateinit var rootView: View

    val logTag: String = javaClass.simpleName

    open fun showLoading() {
        rootView.swipe_refresh_layout.isRefreshing = true
    }

    open fun dismissLoading() {
        rootView.swipe_refresh_layout.isRefreshing = false
    }

    open fun showContent() {
        resetPage(rootView.swipe_refresh_layout)
    }

    open fun showEmpty() {
        resetPage(rootView.empty_root_layout)
    }

    open fun showError() {
        resetPage(rootView.error_root_layout)
    }

    private fun resetPage(view: View) {
        rootView.swipe_refresh_layout.visibility = View.GONE
        rootView.empty_root_layout.visibility = View.GONE
        rootView.error_root_layout.visibility = View.GONE

        view.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.geek_base_fragment, container, false)
        customView(inflater, rootView)
        return rootView
    }

    open fun customView(inflater: LayoutInflater, rootView: View) {}
}