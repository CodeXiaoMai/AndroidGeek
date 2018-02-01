package com.xiaomai.geek.base

import com.xiaomai.geek.common.wrapper.GeeKLog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by wangce on 2018/1/29.
 */
abstract class BaseObserver<T> : Observer<T> {

    override fun onComplete() {
    }

    override fun onSubscribe(disposable: Disposable) {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        GeeKLog.e(e)
    }

    override fun onNext(value: T) {
        onSuccess(value)
    }

    abstract fun onSuccess(value: T)
}