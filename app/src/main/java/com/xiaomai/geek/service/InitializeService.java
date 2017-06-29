package com.xiaomai.geek.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.xiaomai.geek.BuildConfig;
import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.common.utils.FileUtils;
import com.xiaomai.geek.common.wrapper.CrashHelper;
import com.xiaomai.geek.data.db.PasswordDBHelper;

/**
 * Created by XiaoMai on 2017/4/5 11:23.
 */

public class InitializeService extends IntentService {

    private static final String ACTION_INIT_WHEN_APP_CREATE = "com.xiaomai.geek.service.action.INIT";

    public InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (null == intent)
            return;
        String action = intent.getAction();
        if (TextUtils.equals(action, ACTION_INIT_WHEN_APP_CREATE))
            performInit();
    }

    private void performInit() {
        // 检查创建文件夹
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getExternalFilesDirs(null);
        }
        FileUtils.checkDirs(Environment.getExternalStorageDirectory() + "/" + getPackageName());

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        // 为了避免开发过程中的错误影响统计数据
        if (!BuildConfig.DEBUG) {
            CrashHelper.init(getApplicationContext());
        }

        // 调用此方法，如果数据库版本有更新，就更新数据库
        PasswordDBHelper.getInstance(this).getWritableDatabase();

        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(GeekApplication.get(this));
        }
    }
}
