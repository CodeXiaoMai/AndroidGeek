
package com.xiaomai.geek.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * Create by XiaoMai on 2017/3/24 18:15.
 * 一些初始化操作，避免Application启动时过多的操作
 */
public class InitializeService extends IntentService {
    private static final String ACTION_INIT = "com.xiaomai.geek.service.action.INIT";

    public InitializeService() {
        super("InitializeService");
    }

    public static void startActionInit(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                handleActionInit();
            }
        }
    }

    private void handleActionInit() {
//        throw new UnsupportedOperationException("Not yet implemented");
    }

}
