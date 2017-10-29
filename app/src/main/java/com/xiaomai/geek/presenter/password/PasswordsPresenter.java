package com.xiaomai.geek.presenter.password;

import android.text.TextUtils;

import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.geek.contract.password.PasswordsContract;
import com.xiaomai.geek.data.IPasswordDataSource;
import com.xiaomai.geek.data.module.Password;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiaomai on 2017/10/29.
 */

public class PasswordsPresenter extends PasswordsContract.Presenter {

    @NonNull
    private final IPasswordDataSource mPasswordRepository;

    public PasswordsPresenter(@NonNull IPasswordDataSource passwordRepository) {
        this.mPasswordRepository = passwordRepository;
    }

    @Override
    public void loadPasswords() {
        Disposable disposable = mPasswordRepository.getPasswords()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Password>>() {
                    @Override
                    public void accept(List<Password> passwords) throws Exception {
                        AppLog.i(TAG, TextUtils.join(";\n", passwords));
                        getMvpView().showContent(passwords);
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadPasswords(@NonNull String keyword) {
        mPasswordRepository.getPasswords(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Password>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<Password> passwords) {
                        AppLog.i(TAG, TextUtils.join(";\n", passwords));
                        getMvpView().showContent(passwords);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getMvpView().showError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}