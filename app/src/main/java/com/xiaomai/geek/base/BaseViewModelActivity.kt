package com.xiaomai.geek.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.xiaomai.geek.common.PageStatus
import kotlinx.android.synthetic.main.geek_base_activity.*

/**
 * Created by wangce on 2018/1/29.
 */
abstract class BaseViewModelActivity<T : BaseViewModel> : BaseActivity() {

    lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this@BaseViewModelActivity).get(getViewModelClazz())
        viewModel.pageStatus.observe(this@BaseViewModelActivity, Observer {
            when (it) {
                PageStatus.LOADING -> showLoading()
                PageStatus.DIS_MISS_LOADING -> dismissLoading()
                PageStatus.NORMAL -> {
                    showContent()
                    dismissLoading()
                }
                PageStatus.EMPTY -> {
                    showEmpty()
                    dismissLoading()
                }
                PageStatus.ERROR -> {
                    showError()
                    dismissLoading()
                }
            }
        })

        viewModel.snackMessage.observe(this@BaseViewModelActivity, Observer {
            showSnackBar(it)
        })
    }

    open fun showSnackBar(it: String?) {
        it?.apply {
            Snackbar.make(content_view, this, Snackbar.LENGTH_SHORT).show()
        }
    }

    abstract fun getViewModelClazz(): Class<T>
}