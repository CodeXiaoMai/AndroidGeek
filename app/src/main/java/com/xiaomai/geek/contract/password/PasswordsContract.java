package com.xiaomai.geek.contract.password;

import android.support.annotation.NonNull;

import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.mvp.lce.ILceView;

import java.util.List;

/**
 * Created by xiaomai on 2017/10/29.
 */

public interface PasswordsContract {

    interface View extends ILceView<List<Password>> {

    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void savePassword(Password password);

        public abstract void loadPasswords();

        public abstract void loadPasswords(@NonNull String keyword);
    }
}
