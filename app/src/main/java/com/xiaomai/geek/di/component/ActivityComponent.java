package com.xiaomai.geek.di.component;

import android.app.Activity;

import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.scope.PerActivity;

import dagger.Component;

/**
 * Created by XiaoMai on 2017/3/29 17:42.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity activity();
}
