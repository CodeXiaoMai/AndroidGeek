package com.xiaomai.geek.di.modules;

import android.app.Application;
import android.content.Context;

import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.api.INewsApi;
import com.xiaomai.geek.api.NewsDataSource;
import com.xiaomai.geek.api.NewsService;
import com.xiaomai.geek.api.client.NewsRetrofit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by XiaoMai on 2017/3/24 18:23.
 */

@Module
public class ApplicationModule {

    protected final GeekApplication application;

    public ApplicationModule(GeekApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    NewsService provideNewsService(NewsRetrofit retrofit) {
        return retrofit.get().create(NewsService.class);
    }

    @Provides
    @Singleton
    public INewsApi provideNewsApi(NewsService newsService) {
        return new NewsDataSource(newsService);
    }
}
