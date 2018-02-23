package com.xiaomai.geek.base

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.xiaomai.geek.common.PageStatus

/**
 * Created by wangce on 2018/1/29.
 */
abstract class BaseViewModelFragment<T : BaseViewModel> : BaseFragment() {

    lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this@BaseViewModelFragment).get(getViewModelClazz())
        viewModel.pageStatus.observe(this@BaseViewModelFragment, Observer {
            when (it) {
                PageStatus.LOADING -> showLoading()
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
    }

    abstract fun getViewModelClazz(): Class<T>
}