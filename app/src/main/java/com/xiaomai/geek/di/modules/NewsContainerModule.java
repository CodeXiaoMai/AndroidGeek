
package com.xiaomai.geek.di.modules;

import com.xiaomai.geek.api.NewsDataSource;
import com.xiaomai.geek.di.scope.PerFragment;
import com.xiaomai.geek.module.adapter.NewsContainerAdapter;
import com.xiaomai.geek.module.news.NewsContainerFragment;
import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.geek.presenter.news.NewsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by XiaoMai on 2017/3/27 18:27. 新闻主页
 */

@Module
public class NewsContainerModule {

    private final NewsContainerFragment mView;

    public NewsContainerModule(NewsContainerFragment mView) {
        this.mView = mView;
    }

    @PerFragment
    @Provides
    public BasePresenter provideMainPresenter(NewsDataSource newsDataSource) {
        return new NewsPresenter(newsDataSource);
    }

    @PerFragment
    @Provides
    public NewsContainerAdapter provideNewsMainAdapter() {
        return new NewsContainerAdapter(mView.getFragmentManager());
    }
}
