package com.xiaomai.geek.base.observer

import com.xiaomai.geek.base.BaseViewModel
import com.xiaomai.geek.common.PageStatus
import io.reactivex.disposables.Disposable

/**
 * Created by wangce on 2018/1/29.
 */
abstract class BaseViewModelObserver<T>(private val viewModel: BaseViewModel) : BaseObserver<T>() {
    override fun onComplete() {
    }

    override fun onSubscribe(disposable: Disposable) {
        viewModel.compositeDisposable?.add(disposable)
    }

    override fun onError(e: Throwable) {
        super.onError(e)
        viewModel.pageStatus.postValue(PageStatus.ERROR)
    }
}