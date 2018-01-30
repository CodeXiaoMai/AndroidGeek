package com.xiaomai.geek.application

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.facebook.stetho.Stetho
import com.xiaomai.geek.BuildConfig
import com.xiaomai.geek.article.db.DaoMaster
import com.xiaomai.geek.article.db.DaoSession
import com.xiaomai.geek.common.wrapper.GeeKLog

/**
 * Created by wangce on 2018/1/29.
 */
class InitializeService(name: String = "InitializeService") : IntentService(name) {

    companion object {
        private const val ACTION_INIT_WHEN_APP_CREATE = "com.xiaomai.geek.service.action.INIT"
        private const val DB_ARTICLE_NAME = "article"
        private var ARTICLE_DAO_SESSION: DaoSession? = null

        fun start(context: Context) {
            val intent = Intent(context, InitializeService::class.java)
            intent.action = ACTION_INIT_WHEN_APP_CREATE
            context.startService(intent)
        }

        fun getArticleDaoSession() = ARTICLE_DAO_SESSION
    }

    override fun onHandleIntent(intent: Intent?) {
        intent?.apply {
            action?.let {
                if (TextUtils.equals(ACTION_INIT_WHEN_APP_CREATE, action)) {
                    performInit()
                }
            }
        }
    }

    private fun performInit() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            GeeKLog.init()
        }

        // 初始化数据库
        val devOpenHelper = DaoMaster.DevOpenHelper(this@InitializeService, DB_ARTICLE_NAME)
        val daoMaster = DaoMaster(devOpenHelper.writableDb)
        ARTICLE_DAO_SESSION = daoMaster.newSession()
    }

}