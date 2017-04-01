package com.xiaomai.geek.view;

import com.xiaomai.geek.data.module.Password;
import com.xiaomai.mvp.MvpView;

/**
 * Created by XiaoMai on 2017/3/30 15:36.
 */

public interface IEditAccountView extends MvpView {

    void onSaveComplete(boolean save);

    void onUpdateComplete(boolean update);

    void onPlatformError();

    void onUserNameError();

    void onPasswordError();

    Password generateRandomPassword();
}
