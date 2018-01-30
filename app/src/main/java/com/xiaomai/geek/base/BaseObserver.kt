package com.xiaomai.geek.base

import com.xiaomai.geek.common.PageStatus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by wangce on 2018/1/29.
 */
abstract class BaseObserver<T>(private val viewModel: BaseViewModel) : Observer<T> {
    override fun onComplete() {
    }

    override fun onSubscribe(disposable: Disposable) {
        viewModel.compositeDisposable?.add(disposable)
    }

    override fun onError(throwable: Throwable) {
        viewModel.pageStatus.value = PageStatus.ERROR
    }
}