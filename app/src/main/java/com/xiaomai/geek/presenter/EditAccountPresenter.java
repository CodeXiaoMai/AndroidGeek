
package com.xiaomai.geek.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.xiaomai.geek.data.db.PasswordDBHelper;
import com.xiaomai.geek.view.IEditAccountView;

import java.util.Random;

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
                        note, 0);
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

    public String generatePassword(@PasswordType int type, int length) {
        switch (type) {
            case TYPE_ALL:
                return getPassword(all, length);
            case TYPE_NUM:
                return getPassword(nums, length);
            case TYPE_LETTER:
                return getPassword(letters, length);
            case TYPE_NUM_LETTER:
                return getPassword(numAndLetters, length);
        }
        return "";
    }

    private String getPassword(char[] source, int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        while (i < length) {
            stringBuilder.append(source[random.nextInt(source.length)]);
            i++;
        }
        return stringBuilder.toString();
    }

    public static final int TYPE_ALL = 0;

    public static final int TYPE_NUM = 1;

    public static final int TYPE_LETTER = 2;

    public static final int TYPE_NUM_LETTER = 3;

    @IntDef({
            TYPE_ALL, TYPE_NUM, TYPE_LETTER, TYPE_NUM_LETTER
    })
    public @interface PasswordType {
    }

    private char[] nums = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    private char[] letters = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private char[] numAndLetters = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private char[] all = {
            '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '-', '=', '[', ']',
            '{', '}', ':', ';', '"', '\'', '|', '\\', '<', '>', ',', '.', '?', '/', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
}
