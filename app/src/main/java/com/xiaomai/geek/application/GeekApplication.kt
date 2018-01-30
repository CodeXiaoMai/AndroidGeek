package com.xiaomai.geek.application

import android.app.Application

/**
 * Created by wangce on 2018/1/26.
 */
class GeekApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        InitializeService.start(this)
    }
}