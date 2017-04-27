package com.xiaomai.geek.view;

import com.xiaomai.geek.data.module.User;
import com.xiaomai.mvp.lce.ILoadView;

/**
 * Created by XiaoMai on 2017/4/26.
 */

public interface ILoginView extends ILoadView {

    void loginSuccess(User user);
}
