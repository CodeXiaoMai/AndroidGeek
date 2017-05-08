package com.xiaomai.geek.data.net.response;

import rx.Subscriber;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public abstract class BaseResponseObserver<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {
//        AppLog.d("onCompleted");
    }

    @Override
    public void onNext(T t) {
//        AppLog.d("onNext");
        onSuccess(t);
    }

    public abstract void onSuccess(T t);
}
