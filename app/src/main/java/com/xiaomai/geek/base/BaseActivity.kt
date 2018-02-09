package com.xiaomai.geek.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import com.xiaomai.geek.R
import kotlinx.android.synthetic.main.geek_base_activity.*
import kotlinx.android.synthetic.main.geek_empty_view.*
import kotlinx.android.synthetic.main.geek_error_view.*

/**
 * Created by wangce on 2018/1/30.
 */
abstract class BaseActivity : AppCompatActivity() {
    val TAG = javaClass.simpleName

    open fun showLoading() {
        swipe_refresh_layout.isRefreshing = true
    }

    open fun dismissLoading() {
        swipe_refresh_layout.isRefreshing = false
    }

    open fun showContent() {
        resetPage(swipe_refresh_layout)
    }

    open fun showEmpty() {
        resetPage(empty_root_layout)
    }

    open fun showError() {
        resetPage(error_root_layout)
    }

    private fun resetPage(view: View) {
        swipe_refresh_layout.visibility = View.GONE
        empty_root_layout.visibility = View.GONE
        error_root_layout.visibility = View.GONE

        view.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (useBaseLayout()) {
            setContentView(R.layout.geek_base_activity)
            if (getLayoutId() > 0) {
                LayoutInflater.from(this@BaseActivity).inflate(getLayoutId(), content_view, true)
            }
        } else {
            setContentView(getLayoutId())
        }
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun useBaseLayout() = true
}