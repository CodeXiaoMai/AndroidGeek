package com.xiaomai.geek.base.observer

import com.xiaomai.geek.base.BaseViewModel
import com.xiaomai.geek.common.PageStatus
import com.xiaomai.geek.common.wrapper.GeeKLog
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

/**
 * Created by wangce on 2018/2/28.
 */
abstract class BaseViewModelSingleObserver<T>(private val viewModel: BaseViewModel) : SingleObserver<T> {

    override fun onSubscribe(d: Disposable) {
        viewModel.compositeDisposable?.add(d)
    }

    override fun onError(e: Throwable) {
        GeeKLog.e(e)
        viewModel.pageStatus.value = PageStatus.ERROR
    }
}