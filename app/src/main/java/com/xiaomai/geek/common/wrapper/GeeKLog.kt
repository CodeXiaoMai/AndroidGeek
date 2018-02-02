package com.xiaomai.geek.common.wrapper

import android.util.Log
import com.orhanobut.logger.Logger
import com.xiaomai.geek.BuildConfig

/**
 * Created by wangce on 2018/1/29.
 */
class GeeKLog {

    companion object {
        private const val TAG = "AndroidGeek"

        fun init() {
            Logger.init(TAG)
        }

        fun d(tag: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, msg)
            }
        }

        fun e(msg: String) {
            if (BuildConfig.DEBUG) {
                Logger.e(msg)
            }
        }

        fun e(e: Throwable) {
            if (BuildConfig.DEBUG) {
                Logger.e(e, "${e.message}")
            }
        }

        fun json(json: String) {
            if (BuildConfig.DEBUG) {
                Logger.json(json)
            }
        }
    }
}