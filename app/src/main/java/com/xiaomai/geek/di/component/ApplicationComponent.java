
package com.xiaomai.geek.di.component;

import android.app.Application;
import android.content.Context;

import com.xiaomai.geek.api.INewsApi;
import com.xiaomai.geek.api.NewsService;
import com.xiaomai.geek.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by XiaoMai on 2017/3/24 18:23.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context getContext();

    Application application();

    NewsService newsService();

    INewsApi I_NEWS_API();
}
