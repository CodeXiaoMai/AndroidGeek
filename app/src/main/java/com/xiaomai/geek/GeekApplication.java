
package com.xiaomai.geek;

import android.app.Application;
import android.content.Context;

import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.geek.di.component.ApplicationComponent;
import com.xiaomai.geek.di.component.DaggerApplicationComponent;
import com.xiaomai.geek.di.module.ApplicationModule;
import com.xiaomai.geek.service.InitializeService;

/**
 * Created by XiaoMai on 2017/3/29 17:30.
 */

public class GeekApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppLog.init();
        InitializeService.start(this);
    }

    public static GeekApplication get(Context context) {
        return (GeekApplication) context.getApplicationContext();
    }

    ApplicationComponent mApplicationComponent;

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this)).build();
        }
        return mApplicationComponent;
    }
}
