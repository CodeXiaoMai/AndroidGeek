package com.xiaomai.geek.application

import android.app.Application
import com.xiaomai.geek.db.DaoSession

/**
 * Created by wangce on 2018/1/26.
 */
class GeekApplication : Application() {

    companion object {
        lateinit var DAO_SESSION: DaoSession
    }

    override fun onCreate() {
        super.onCreate()

        InitializeService.start(this)
    }
}