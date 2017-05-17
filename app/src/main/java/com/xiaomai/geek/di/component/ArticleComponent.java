package com.xiaomai.geek.di.component;

import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.ArticleModule;
import com.xiaomai.geek.di.scope.PerActivity;
import com.xiaomai.geek.ui.module.articel.ArticleFragment;

import dagger.Component;

/**
 * Created by XiaoMai on 2017/5/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, ArticleModule.class})
public interface ArticleComponent extends ActivityComponent{


}
