package com.xiaomai.geek.di.component;

import com.xiaomai.geek.di.module.AccountModule;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.scope.PerActivity;
import com.xiaomai.geek.ui.module.LoginActivity;

import dagger.Component;

/**
 * Created by XiaoMai on 2017/4/26.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {
        ActivityModule.class, AccountModule.class
})
public interface AccountComponent extends ActivityComponent{

    void inject(LoginActivity activity);
}
