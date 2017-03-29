package com.xiaomai.geek.di.modules;

import com.xiaomai.geek.api.INewsApi;
import com.xiaomai.geek.api.bean.NewsInfo;
import com.xiaomai.geek.di.scope.PerFragment;
import com.xiaomai.geek.module.adapter.NewsListAdapter;
import com.xiaomai.geek.module.news.NewsListFragment;
import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.geek.presenter.news.NewsListPresenter;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by XiaoMai on 2017/3/28 13:43.
 */

@Module
public class NewsListModule {

    private final NewsListFragment fragment;

    private final List<NewsInfo> data;

    public NewsListModule(NewsListFragment fragment, List<NewsInfo> data) {
        this.fragment = fragment;
        this.data = data;
    }

    @PerFragment
    @Provides
    public NewsListAdapter provideNewsListAdapter() {
        return new NewsListAdapter(data);
    }

    @PerFragment
    @Provides
    public BasePresenter providePresenter(INewsApi newsApi) {
        return new NewsListPresenter(newsApi);
    }
}
