package com.xiaomai.geek.di.modules;

import android.content.Context;

import com.xiaomai.geek.GeekApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by XiaoMai on 2017/3/24 18:23.
 */

@Module
public class ApplicationModule {

    private final GeekApplication application;

    public ApplicationModule(GeekApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application.getApplicationContext();
    }

}
