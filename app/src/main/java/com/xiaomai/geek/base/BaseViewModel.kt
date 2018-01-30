package com.xiaomai.geek.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.xiaomai.geek.common.PageStatus
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by wangce on 2018/1/29.
 */
open class BaseViewModel(context: Application) : AndroidViewModel(context) {

    var pageStatus: MutableLiveData<PageStatus> = MutableLiveData()

    var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.apply {
            clear()
            compositeDisposable = null
        }
    }
}