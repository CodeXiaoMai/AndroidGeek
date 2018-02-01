package com.xiaomai.geek.base

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by wangce on 2018/1/31.
 */
open class BaseDataSource {
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    open fun onDestroy() {
        compositeDisposable.clear()
    }
}