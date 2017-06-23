package com.xiaomai.geek.di.component;

import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.ArticleModule;
import com.xiaomai.geek.di.module.GitHubModule;
import com.xiaomai.geek.di.module.VideoModule;
import com.xiaomai.geek.di.scope.PerActivity;
import com.xiaomai.geek.ui.module.articel.ArticleFragment;
import com.xiaomai.geek.ui.module.github.TrendingFragment;
import com.xiaomai.geek.ui.module.video.VideoContainerFragment;

import dagger.Component;

/**
 * Created by XiaoMai on 2017/4/24.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {
        ActivityModule.class, GitHubModule.class, ArticleModule.class, VideoModule.class
})
public interface MainComponent extends ActivityComponent {

    void inject(TrendingFragment trendingFragment);

    void inject(ArticleFragment articleFragment);

    void inject(VideoContainerFragment videoContainerFragment);
}
