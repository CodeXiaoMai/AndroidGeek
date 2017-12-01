package com.xiaomai.geek.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.xiaomai.geek.ui.module.password.DoorActivity;

/**
 * Created by XiaoMai on 2017/12/1.
 */

public class HomePressReceiver extends BroadcastReceiver {

    private static final String TAG = "HomePressReceiver";
    public static final String SYSTEM_REASON = "reason";
    public static final String SYSTEM_HOME_KEY = "homekey";
    public static final String SYSTEM_HOME_KEY_LONG = "recentapps";

    public HomePressReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (TextUtils.equals(action, Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_REASON);
            if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                // 表示按了home键
                Log.e(TAG, "onReceive: home press");
                DoorActivity.launch(context, true);
            } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                // 表示长按home键
            }
        }
    }
}
