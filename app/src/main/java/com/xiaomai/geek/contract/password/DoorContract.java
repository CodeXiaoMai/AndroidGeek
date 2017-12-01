package com.xiaomai.geek.contract.password;

import android.support.annotation.NonNull;

import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.mvp.IMvpView;

/**
 * Created by XiaoMai on 2017/12/1.
 */

public interface DoorContract {

    interface View extends IMvpView {
        /**
         * @param isInit true 设置密码，false 输入密码
         */
        void configView(boolean isInit);

        void onSavePasswordFinish();

        /**
         * 密码验证结果
         *
         * @param pass true 密码正确，false 密码错误
         */
        void onVerifyFinish(boolean pass);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void init();

        public abstract void savePassword(@NonNull String password);

        public abstract void verifyPassword(@NonNull String password);
    }
}
