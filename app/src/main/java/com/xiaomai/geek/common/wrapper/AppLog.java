
package com.xiaomai.geek.common.wrapper;

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
        if (BuildConfig.DEBUG) {
            Logger.i(msg);
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Logger.d(msg);
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG) {
            Logger.w(msg);
        }
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Logger.e(msg);
        }
    }

    public static void e(Throwable e) {
        if (BuildConfig.DEBUG) {
            Logger.e(e, "");
        }
    }
}
