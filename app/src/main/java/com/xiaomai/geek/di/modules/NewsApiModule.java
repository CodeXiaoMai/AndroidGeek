
package com.xiaomai.geek.di.modules;

import com.xiaomai.geek.api.INewsApi;
import com.xiaomai.geek.api.NewsDataSource;
import com.xiaomai.geek.api.NewsService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by XiaoMai on 2017/3/28 10:25.
 */

@Module
public class NewsApiModule {

    private final NewsService newsService;

    public NewsApiModule(NewsService newsService) {
        this.newsService = newsService;
    }

    @Provides
    public INewsApi provideNewsApi() {
        return new NewsDataSource(newsService);
    }
}
