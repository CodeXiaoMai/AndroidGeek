
package com.xiaomai.geek.common.wrapper;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.xiaomai.geek.BuildConfig;


/**
 * Created by XiaoMai on 2017/3/9 15:56. Logger封装 创建一个AppLog类来包装Logger
 */

public class AppLog {

    private static final String TAG = "AndroidGeek";

    public static void init() {
        Logger.init(TAG);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void e(Throwable e) {
        e(TAG, e);
    }

    public static void e(String tag, Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, "", e);
        }
    }
}
