package com.xiaomai.geek.di.module;

import com.xiaomai.geek.data.api.AccountApi;
import com.xiaomai.geek.data.net.AccountDataSource;

import dagger.Module;
import dagger.Provides;

/**
 * Created by XiaoMai on 2017/4/26.
 */

@Module
public class AccountModule {

    @Provides
    AccountApi provideAccountApi(AccountDataSource dataSource) {
        return dataSource;
    }
}
