package com.xiaomai.geek.di.component;

import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.GitHubModule;
import com.xiaomai.geek.di.scope.PerActivity;
import com.xiaomai.geek.ui.MainActivity;
import com.xiaomai.geek.ui.module.github.RepoDetailActivity;
import com.xiaomai.geek.ui.module.github.RepoListActivity;
import com.xiaomai.geek.ui.module.github.SearchActivity;
import com.xiaomai.geek.ui.module.github.UserActivity;
import com.xiaomai.geek.ui.module.github.UserListActivity;

import dagger.Component;

/**
 * Created by XiaoMai on 2017/4/24.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, GitHubModule.class})
public interface GitHubComponent extends ActivityComponent {

    void inject(MainActivity activity);

    void inject(UserActivity activity);

    void inject(RepoListActivity activity);

    void inject(UserListActivity activity);

    void inject(RepoDetailActivity repoDetailActivity);

    void inject(SearchActivity activity);
}
