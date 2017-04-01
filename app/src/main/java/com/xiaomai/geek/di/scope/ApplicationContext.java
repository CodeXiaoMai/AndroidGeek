package com.xiaomai.geek.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by XiaoMai on 2017/3/29 17:37.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationContext {
}
