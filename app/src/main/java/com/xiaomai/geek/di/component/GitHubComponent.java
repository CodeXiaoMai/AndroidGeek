package com.xiaomai.geek.di.component;

import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.GitHubModule;
import com.xiaomai.geek.di.scope.PerActivity;
import com.xiaomai.geek.ui.MainActivity;

import dagger.Component;

/**
 * Created by XiaoMai on 2017/4/24.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, GitHubModule.class})
public interface GitHubComponent extends ActivityComponent {

    void inject(MainActivity activity);
}
