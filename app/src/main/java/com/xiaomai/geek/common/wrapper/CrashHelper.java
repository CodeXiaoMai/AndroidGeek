package com.xiaomai.geek.common.wrapper;

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.xiaomai.geek.BuildConfig;

/**
 * Created by XiaoMai on 2017/4/20.
 */

public class CrashHelper {

    private static final String CHANNEL_NAME = "xiaomai";

    private static final String APP_ID = "8568d789f8";

    public static void init(Context context) {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setAppChannel(CHANNEL_NAME);
        CrashReport.initCrashReport(context, APP_ID, BuildConfig.DEBUG, strategy);

    }
}
