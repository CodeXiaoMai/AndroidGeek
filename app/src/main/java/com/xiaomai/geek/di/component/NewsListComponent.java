package com.xiaomai.geek.di.component;

import com.xiaomai.geek.di.modules.NewsListModule;
import com.xiaomai.geek.di.scope.PerFragment;
import com.xiaomai.geek.module.news.NewsListFragment;

import dagger.Component;

/**
 * Created by XiaoMai on 2017/3/28 14:10.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = NewsListModule.class)
public interface NewsListComponent {

    void inject(NewsListFragment fragment);
}
