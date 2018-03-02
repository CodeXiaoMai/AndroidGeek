package com.xiaomai.geek.base.observer

import com.xiaomai.geek.base.BaseViewModel
import com.xiaomai.geek.common.PageStatus
import com.xiaomai.geek.common.wrapper.GeeKLog
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

/**
 * Created by wangce on 2018/2/27.
 */
abstract class BaseViewModelCompletableObserver(private val viewModel: BaseViewModel) : CompletableObserver {

    override fun onSubscribe(d: Disposable) {
        viewModel.compositeDisposable?.add(d)
    }

    override fun onError(e: Throwable) {
        GeeKLog.e(e)
        viewModel.pageStatus.value = PageStatus.ERROR
    }
}