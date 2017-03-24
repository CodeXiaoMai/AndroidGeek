
package com.xiaomai.geek.di.component;

import android.content.Context;

import com.xiaomai.geek.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by XiaoMai on 2017/3/24 18:23.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context getContext();
}
