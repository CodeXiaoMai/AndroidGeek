package com.xiaomai.geek.presenter.password;

import android.support.annotation.NonNull;

import com.xiaomai.geek.contract.password.PasswordDetailContract;
import com.xiaomai.geek.data.IPasswordDataSource;
import com.xiaomai.geek.data.PasswordRepository;
import com.xiaomai.geek.data.module.Password;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/11/17.
 */

public class PasswordDetailPresenter extends PasswordDetailContract.Presenter {

    @NonNull
    private final IPasswordDataSource mPasswordRepository;

    public PasswordDetailPresenter(@NonNull IPasswordDataSource passwordRepository) {
        mPasswordRepository = passwordRepository;
    }

    public void deletePassword(@NonNull final Password password) {
        String id = password.getId();
        mPasswordRepository.deletePassword(id)
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
                            getMvpView().deleteSuccess(password);
                        } else {
                            getMvpView().showError(new Throwable("删除失败！"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
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
