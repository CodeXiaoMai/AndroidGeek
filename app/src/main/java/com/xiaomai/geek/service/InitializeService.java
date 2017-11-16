package com.xiaomai.geek.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.squareup.leakcanary.LeakCanary;
import com.xiaomai.geek.GeekApplication;

/**
 * Created by XiaoMai on 2017/11/16.
 */

public class InitializeService extends IntentService {
    private static final String ACTION_INIT_WHEN_APP_CREATE = "com.xiaomai.geek.service.action.INIT";

    public InitializeService() {
        super(InitializeService.class.getSimpleName());
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (null == intent) {
            return;
        }

        String action = intent.getAction();
        if (TextUtils.equals(action, ACTION_INIT_WHEN_APP_CREATE)) {
            performInit();
        }
    }

    private void performInit() {
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(GeekApplication.get(this));
        }
    }
}
