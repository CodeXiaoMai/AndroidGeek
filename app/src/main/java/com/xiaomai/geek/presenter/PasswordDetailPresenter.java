
package com.xiaomai.geek.presenter;

import android.content.Context;
import android.support.annotation.IntDef;

import com.xiaomai.geek.data.db.PasswordDBHelper;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.view.IPasswordDetailView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/3/31 14:06.
 */

public class PasswordDetailPresenter extends BaseRxPresenter<IPasswordDetailView> {

    public void getPasswordDetail(final Context context, final int id) {
        mCompositeSubscription.add(Observable.create(new Observable.OnSubscribe<Password>() {
            @Override
            public void call(Subscriber<? super Password> subscriber) {
                Password password = PasswordDBHelper.getInstance(context).getPasswordById(id);
                subscriber.onNext(password);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Password>() {
                    @Override
                    public void call(Password password) {
                        getMvpView().showContent(password);
                    }
                }));
    }

    public void startPassword(@UpdateType
    final int type, final Context context, final Password password) {
        mCompositeSubscription.add(Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                int i = PasswordDBHelper.getInstance(context).updatePassword(password);
                subscriber.onNext(i);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        switch (type) {
                            case TYPE_STAR:
                                getMvpView().onStarComplete(integer > 0);
                                break;
                            case TYPE_UN_STAR:
                                getMvpView().onUnStarComplete(integer > 0);
                                break;
                        }
                    }
                }));
    }

    public void deletePassword(final Context context, final int id) {
        mCompositeSubscription.add(Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                int i = PasswordDBHelper.getInstance(context).deletePasswordById(id);
                subscriber.onNext(i);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        getMvpView().onDeleteComplete(integer > 0);
                    }
                }));
    }

    public static final int TYPE_STAR = 1;

    public static final int TYPE_UN_STAR = 2;

    @IntDef({
            TYPE_STAR, TYPE_UN_STAR
    })
    @interface UpdateType {
    }
}
