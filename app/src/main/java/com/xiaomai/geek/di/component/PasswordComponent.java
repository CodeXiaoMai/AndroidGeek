package com.xiaomai.geek.di.component;

import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.PasswordModule;
import com.xiaomai.geek.di.scope.PerActivity;
import com.xiaomai.geek.ui.module.password.EditAccountActivity;

import dagger.Component;

/**
 * Created by XiaoMai on 2017/3/30 16:35.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, PasswordModule.class})
public interface PasswordComponent extends ActivityComponent {

    void inject(EditAccountActivity editAccountActivity);
}
