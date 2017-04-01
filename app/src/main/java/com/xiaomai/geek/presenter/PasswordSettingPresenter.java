
package com.xiaomai.geek.presenter;

import android.content.Context;

import com.xiaomai.geek.data.db.PasswordDBHelper;
import com.xiaomai.geek.view.IPasswordSettingView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/4/1 18:19.
 */

public class PasswordSettingPresenter extends BaseRxPresenter<IPasswordSettingView> {

    public void deleteAllPasswords(final Context context) {
        mCompositeSubscription.add(Observable.create(
                new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        int count = PasswordDBHelper.getInstance(context).deleteAllPasswords();
                        subscriber.onNext(count);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        getMvpView().onDeleteAllPasswords(integer);
                    }
                }));

    }
}
