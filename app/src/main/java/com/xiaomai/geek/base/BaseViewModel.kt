package com.xiaomai.geek.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by wangce on 2018/1/29.
 */
open class BaseViewModel : ViewModel() {
    var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.apply {
            clear()
            compositeDisposable = null
        }
    }
}