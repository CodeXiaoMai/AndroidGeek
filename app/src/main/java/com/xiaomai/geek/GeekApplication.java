package com.xiaomai.geek;

import android.app.Application;
import android.content.Context;

import com.xiaomai.geek.service.InitializeService;

/**
 * Created by xiaomai on 2017/10/25.
 */

public class GeekApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        InitializeService.start(this);
    }

    public static GeekApplication get(Context context) {
        return (GeekApplication) context.getApplicationContext();
    }
}