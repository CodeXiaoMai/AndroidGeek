package com.xiaomai.geek.base

import com.xiaomai.geek.common.wrapper.GeeKLog
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

/**
 * Created by wangce on 2018/2/28.
 */
abstract class BaseSingleObserver<T> : SingleObserver<T> {
    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {
        GeeKLog.e(e)
    }
}