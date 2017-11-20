package com.xiaomai.geek.service;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xiaomai.geek.common.wrapper.AppLog;

/**
 * Created by XiaoMai on 2017/11/20.
 */

public class NotificationService extends IntentService {
    private static final String TAG = "NotificationService";
    public static final String ACTION_NOTIFICATION = "com.xiaomai.geek.service.action.NOTIFICATION";
    public static final String EXTRA_CONTENT = "extra_content";

    public NotificationService() {
        super(NotificationService.class.getSimpleName());
    }

    public static void startService(@NonNull Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(ACTION_NOTIFICATION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppLog.i(TAG, "onHandleIntent: ");
        if (intent != null) {
            String action = intent.getAction();
            if (TextUtils.equals(action, ACTION_NOTIFICATION)) {
                String content = intent.getStringExtra(EXTRA_CONTENT);
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, content));
            }
        }
    }
}
