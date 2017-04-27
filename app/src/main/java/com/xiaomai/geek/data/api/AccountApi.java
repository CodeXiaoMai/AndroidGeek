package com.xiaomai.geek.data.api;

import com.xiaomai.geek.data.module.User;

import rx.Observable;

/**
 * Created by XiaoMai on 2017/4/26.
 */

public interface AccountApi {

    Observable<User> login(String userName, String password);
}
