package com.xiaomai.geek.base.observer

import com.xiaomai.geek.common.wrapper.GeeKLog
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

/**
 * Created by wangce on 2018/2/27.
 */
abstract class BaseCompletableObserver : CompletableObserver {

    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {
        GeeKLog.e(e)
    }
}