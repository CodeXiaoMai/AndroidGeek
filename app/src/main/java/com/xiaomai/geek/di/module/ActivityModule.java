
package com.xiaomai.geek.di.module;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.xiaomai.geek.di.scope.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by XiaoMai on 2017/3/29 17:40.
 */

@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    public ActivityModule(Fragment fragment) {
        this.activity = fragment.getActivity();
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }
}
