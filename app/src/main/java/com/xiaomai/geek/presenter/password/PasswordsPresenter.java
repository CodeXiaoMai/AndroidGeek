package com.xiaomai.geek.presenter.password;

import android.text.TextUtils;

import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.geek.contract.password.PasswordsContract;
import com.xiaomai.geek.data.IPasswordDataSource;
import com.xiaomai.geek.data.PasswordRepository;
import com.xiaomai.geek.data.module.Password;

import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
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
    public void savePassword(Password password) {
        mPasswordRepository.savePassword(password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean) {
                            getMvpView().showError(new Throwable("撤销成功"));
                        } else {
                            getMvpView().showError(new Throwable("撤销失败"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(new Throwable("撤销失败"));
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void loadPasswords() {
        Disposable disposable = mPasswordRepository.getPasswords()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        getMvpView().showLoading();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        getMvpView().dismissLoading();
                    }
                })
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
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getMvpView().showLoading();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        getMvpView().dismissLoading();
                    }
                })
                .subscribe(new Observer<List<Password>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<Password> passwords) {
                        AppLog.i(TAG, TextUtils.join(";\n", passwords));
                        if (passwords.size() <= 0) {
                            getMvpView().showEmpty();
                        } else {
                            getMvpView().showContent(passwords);
                        }
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

    @Override
    public void detachView() {
        super.detachView();
        PasswordRepository.destroyInstance();
    }
}