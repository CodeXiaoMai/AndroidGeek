package com.xiaomai.geek.model.main.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.xiaomai.geek.base.BaseViewModel
import com.xiaomai.geek.base.observer.BaseSingleObserver
import com.xiaomai.geek.db.Config
import com.xiaomai.geek.model.main.model.ConfigRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangce on 2018/3/5.
 */
class MainViewModel(context: Application)
    : BaseViewModel(context) {

    private val configRepository: ConfigRepository = ConfigRepository(getApplication())

    var config: MutableLiveData<Config> = MutableLiveData()

    fun getConfig() {
        configRepository.getConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSingleObserver<Config>() {
                    override fun onSuccess(t: Config) {
                        config.value = t
                    }
                })
    }

    fun saveConfig(config: Config) {
        configRepository.saveConfig(config)
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}