
package com.xiaomai.geek.di.component;

import com.xiaomai.geek.di.modules.NewsContainerModule;
import com.xiaomai.geek.di.scope.PerFragment;
import com.xiaomai.geek.module.news.NewsContainerFragment;

import dagger.Component;

/**
 * Created by XiaoMai on 2017/3/27 18:31.
 */

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = NewsContainerModule.class)
public interface NewsContainerComponent {

    void inject(NewsContainerFragment fragment);
}
