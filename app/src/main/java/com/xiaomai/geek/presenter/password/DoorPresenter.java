package com.xiaomai.geek.presenter.password;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xiaomai.geek.contract.password.DoorContract;
import com.xiaomai.geek.data.pref.PasswordPref;

/**
 * Created by XiaoMai on 2017/12/1.
 */

public class DoorPresenter extends DoorContract.Presenter {

    @NonNull
    private Context mContext;

    public DoorPresenter(@NonNull Context context) {
        this.mContext = context;
    }

    @Override
    public void init() {
        final String password = getPassword();
        getMvpView().configView(TextUtils.isEmpty(password));
    }

    @Override
    public void savePassword(@NonNull String password) {
        PasswordPref.savePassword(mContext, password);
        getMvpView().onSavePasswordFinish();
    }

    @Override
    public void verifyPassword(@NonNull String password) {
        final String password1 = getPassword();
        final boolean result = TextUtils.equals(password, password1);
        getMvpView().onVerifyFinish(result);
    }

    private String getPassword() {
        return PasswordPref.getPassword(mContext);
    }
}
