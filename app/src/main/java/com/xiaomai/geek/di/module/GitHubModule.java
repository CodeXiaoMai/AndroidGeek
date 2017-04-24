package com.xiaomai.geek.di.module;

import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.net.GitHubDataSource;

import dagger.Module;
import dagger.Provides;

/**
 * Created by XiaoMai on 2017/4/24.
 */

@Module
public class GitHubModule {

    @Provides
    GitHubApi provideGitHubApi(GitHubDataSource dataSource) {
        return dataSource;
    }
}
