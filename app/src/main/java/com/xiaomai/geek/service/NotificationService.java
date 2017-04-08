
package com.xiaomai.geek.service;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by XiaoMai on 2017/4/8 13:22.
 */

public class NotificationService extends IntentService {

    private static final String TAG = "NotificationService";

    public static final String ACTION_NOTIFICATION = "com.xiaomai.geek.service.action.NOTIFICATION";

    public static final String EXTRA_CONTENT = "extra_content";

    public static void startService(Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(ACTION_NOTIFICATION);
        context.startService(intent);
    }

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent: ");
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
