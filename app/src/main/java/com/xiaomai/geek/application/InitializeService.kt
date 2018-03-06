package com.xiaomai.geek.application

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.facebook.stetho.Stetho
import com.xiaomai.geek.BuildConfig
import com.xiaomai.geek.common.wrapper.GeeKLog

/**
 * Created by wangce on 2018/1/29.
 */
class InitializeService(name: String = "InitializeService") : IntentService(name) {

    companion object {
        private const val ACTION_INIT_WHEN_APP_CREATE = "com.xiaomai.geek.service.action.INIT"

        fun start(context: Context) {
            val intent = Intent(context, InitializeService::class.java)
            intent.action = ACTION_INIT_WHEN_APP_CREATE
            context.startService(intent)
        }
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
    }
}