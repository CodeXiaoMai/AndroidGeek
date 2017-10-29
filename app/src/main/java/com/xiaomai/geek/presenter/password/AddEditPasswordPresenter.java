package com.xiaomai.geek.presenter.password;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xiaomai.geek.contract.password.AddEditPasswordContract;
import com.xiaomai.geek.data.IPasswordDataSource;
import com.xiaomai.geek.data.module.Password;


/**
 * Created by xiaomai on 2017/10/26.
 */

public class AddEditPasswordPresenter extends AddEditPasswordContract.Presenter {

    @NonNull
    private final IPasswordDataSource mPasswordRepository;

    public AddEditPasswordPresenter(@NonNull IPasswordDataSource passwordRepository) {
        mPasswordRepository = passwordRepository;
    }

    @Override
    public void savePassword(@Nullable String platform, @Nullable String userName, @Nullable String pwd,
                             @Nullable String note) {
        Password password = new Password(platform, userName, pwd, note);
        mPasswordRepository.savePassword(password);
    }
}