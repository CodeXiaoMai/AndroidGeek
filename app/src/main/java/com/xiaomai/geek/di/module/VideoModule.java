package com.xiaomai.geek.di.module;

import com.xiaomai.geek.data.api.VideoApi;
import com.xiaomai.geek.data.net.VideoDataSource;

import dagger.Module;
import dagger.Provides;

/**
 * Created by XiaoMai on 2017/6/23.
 */

@Module
public class VideoModule {

    @Provides
    VideoApi provideVideoApi(VideoDataSource dataSource) {
        return dataSource;
    }
}
