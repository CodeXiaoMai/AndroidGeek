
package com.xiaomai.geek.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;

import com.xiaomai.geek.data.db.PasswordDBHelper;
import com.xiaomai.geek.view.IEditAccountView;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/3/30 15:44.
 */

public class EditAccountPresenter extends BaseRxPresenter<IEditAccountView> {

    @Inject
    public EditAccountPresenter() {

    }

    public void savePassword(final Context context, final String platform, final String userName,
            final String password, final String note) {
        if (TextUtils.isEmpty(platform)) {
            getMvpView().onPlatformError();
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            getMvpView().onUserNameError();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            getMvpView().onPasswordError();
            return;
        }
        mCompositeSubscription.add(Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                long id = PasswordDBHelper.getInstance(context).insert(platform, userName, password,
                        note, 0, 0);
                if (id > 0) {
                    subscriber.onNext(true);
                } else {
                    subscriber.onNext(false);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        getMvpView().onSaveComplete(aBoolean);
                    }
                }));

    }

    public void updatePassword(final Context context, final int passwordId, final String platform,
            final String userName, final String password, final String note) {
        mCompositeSubscription.add(Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(PasswordDBHelper.COLUMN_PLATFORM, platform);
                contentValues.put(PasswordDBHelper.COLUMN_USERNAME, userName);
                contentValues.put(PasswordDBHelper.COLUMN_PASSWORD, password);
                contentValues.put(PasswordDBHelper.COLUMN_NOTE, note);
                int id = PasswordDBHelper.getInstance(context).updatePasswordById(passwordId,
                        contentValues);
                subscriber.onNext(id);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        getMvpView().onUpdateComplete(integer > 0);
                    }
                }));
    }

}
