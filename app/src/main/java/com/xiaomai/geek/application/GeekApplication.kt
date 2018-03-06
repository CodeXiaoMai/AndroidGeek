package com.xiaomai.geek.application

import android.app.Application
import com.xiaomai.geek.db.DaoMaster
import com.xiaomai.geek.db.DaoSession

/**
 * Created by wangce on 2018/1/26.
 */
class GeekApplication : Application() {
    private val DB_NAME = "geek"

    companion object {
        lateinit var DAO_SESSION: DaoSession
    }

    override fun onCreate() {
        super.onCreate()

        // 初始化数据库
        val devOpenHelper = DaoMaster.DevOpenHelper(this, DB_NAME)
        val daoMaster = DaoMaster(devOpenHelper.writableDb)
        GeekApplication.DAO_SESSION = daoMaster.newSession()
        InitializeService.start(this)
    }
}