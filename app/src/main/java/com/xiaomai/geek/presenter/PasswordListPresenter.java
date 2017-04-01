package com.xiaomai.geek.presenter;


import android.content.Context;

import com.xiaomai.geek.data.db.PasswordDBHelper;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.mvp.lce.ILceView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/3/30 17:18.
 */

public class PasswordListPresenter extends BaseRxPresenter<ILceView<List<Password>>> {

    public void getPasswords(final Context context) {
        mCompositeSubscription.add(Observable.create(new Observable.OnSubscribe<List<Password>>() {
            @Override
            public void call(Subscriber<? super List<Password>> subscriber) {
                List<Password> passwords = PasswordDBHelper.getInstance(context).queryAllPasswords();
                subscriber.onNext(passwords);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Password>>() {
            @Override
            public void onCompleted() {
                getMvpView().dismissLoading();
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().showError(e);
            }

            @Override
            public void onNext(List<Password> passwords) {
                if (passwords.size() == 0) {
                    getMvpView().showEmpty();
                } else {
                    getMvpView().showContent(passwords);
                }
            }
        }));
    }
}
