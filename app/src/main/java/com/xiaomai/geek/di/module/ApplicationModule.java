package com.xiaomai.geek.di.module;

import android.app.Application;
import android.content.Context;

import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.data.net.GitHubService;
import com.xiaomai.geek.data.net.client.GitHubTrendingRetrofit;
import com.xiaomai.geek.di.scope.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by XiaoMai on 2017/3/29 17:38.
 */

@Module
public class ApplicationModule {

    private final GeekApplication application;

    public ApplicationModule(GeekApplication application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    GitHubService provideGitHubService(GitHubTrendingRetrofit retrofit) {
        return retrofit.get().create(GitHubService.class);
    }
}
