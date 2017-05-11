package com.xiaomai.geek.presenter.password;


import android.content.Context;

import com.xiaomai.geek.data.db.PasswordDBHelper;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.presenter.BaseRxPresenter;
import com.xiaomai.mvp.lce.ILceView;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/3/30 17:18.
 */

public class PasswordListPresenter extends BaseRxPresenter<ILceView<List<Password>>> {

    @Inject
    public PasswordListPresenter() {
    }

    public void getAllPasswords(final Context context) {
        mCompositeSubscription.add(Observable.create(new Observable.OnSubscribe<List<Password>>() {
            @Override
            public void call(Subscriber<? super List<Password>> subscriber) {
                try {
                    List<Password> passwords = PasswordDBHelper.getInstance(context).getAllPasswords();
                    subscriber.onNext(passwords);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
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

    public void getPasswordsByKeywords(final Context context, final String keywords) {
        mCompositeSubscription.add(
                Observable.create(new Observable.OnSubscribe<List<Password>>() {
                    @Override
                    public void call(Subscriber<? super List<Password>> subscriber) {
                        try {
                            List<Password> passwords = PasswordDBHelper.getInstance(context).getPasswordByKeywords(keywords);
                            subscriber.onNext(passwords);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<Password>>() {
                            @Override
                            public void call(List<Password> passwords) {
                                if (passwords.size() > 0) {
                                    getMvpView().showContent(passwords);
                                } else {
                                    getMvpView().showEmpty();
                                }
                            }
                        })
        );
    }
}
