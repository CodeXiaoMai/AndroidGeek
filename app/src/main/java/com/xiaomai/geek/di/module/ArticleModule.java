package com.xiaomai.geek.di.module;

import com.xiaomai.geek.data.api.ArticleApi;
import com.xiaomai.geek.data.net.ArticleDataSource;

import dagger.Module;
import dagger.Provides;

/**
 * Created by XiaoMai on 2017/5/17.
 */

@Module
public class ArticleModule {

    @Provides
    ArticleApi provideArticleApi(ArticleDataSource dataSource) {
        return dataSource;
    }
}
