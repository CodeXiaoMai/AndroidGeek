package com.xiaomai.geek.presenter.password;

import android.support.annotation.NonNull;

import com.xiaomai.geek.contract.password.AddEditPasswordContract;
import com.xiaomai.geek.data.IPasswordDataSource;
import com.xiaomai.geek.data.PasswordRepository;
import com.xiaomai.geek.data.module.Password;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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
    public void savePassword(@NonNull Password password) {
        if (password.isEmpty()) {
            getMvpView().showError(new Throwable("至少一项不为空"));
        } else {
            mPasswordRepository.savePassword(password);
            getMvpView().onSaveSuccess();
        }
    }

    @Override
    public void updatePassword(@NonNull final Password password) {
        mPasswordRepository.updatePassword(password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            getMvpView().onUpdateSuccess(password);
                        } else {
                            getMvpView().showError(new Throwable("密码修改失败"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(new Throwable("密码修改失败"));
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