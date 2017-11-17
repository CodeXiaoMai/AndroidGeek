package com.xiaomai.geek.contract.password;

import android.support.annotation.NonNull;

import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.mvp.lce.ILceView;

/**
 * Created by XiaoMai on 2017/11/17.
 */

public interface PasswordDetailContract {

    interface View extends ILceView<Password> {
        void deleteSuccess(@NonNull Password password);
    }

    abstract class Presenter extends BasePresenter<View> {
    }

}
