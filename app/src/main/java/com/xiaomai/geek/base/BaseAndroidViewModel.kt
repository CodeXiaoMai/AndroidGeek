package com.xiaomai.geek.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by wangce on 2018/1/29.
 */
open class BaseAndroidViewModel(context: Application) : AndroidViewModel(context) {

    var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.apply {
            clear()
            compositeDisposable = null
        }
    }
}