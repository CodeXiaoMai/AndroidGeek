
package com.xiaomai.geek;

import android.app.Application;
import android.content.Context;

import com.xiaomai.geek.di.component.ApplicationComponent;
import com.xiaomai.geek.di.component.DaggerApplicationComponent;
import com.xiaomai.geek.di.modules.ApplicationModule;
import com.xiaomai.geek.service.InitializeService;

/**
 * Created by XiaoMai on 2017/3/24 18:16.
 */

public class GeekApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        InitializeService.startActionInit(this);
    }

    public static GeekApplication get(Context context) {
        return (GeekApplication) context.getApplicationContext();
    }

    ApplicationComponent mApplicationComponent;

    public ApplicationComponent getApplicationComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this)).build();
        }
        return mApplicationComponent;
    }
}
