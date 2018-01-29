package com.xiaomai.geek.base

import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by wangce on 2018/1/29.
 */
abstract class BaseObserver<T>(private val compositeDisposable: CompositeDisposable?) : Observer<T> {
    override fun onComplete() {
    }

    override fun onSubscribe(disposable: Disposable) {
        compositeDisposable?.add(disposable)
    }

    override fun onError(throwable: Throwable) {
    }
}