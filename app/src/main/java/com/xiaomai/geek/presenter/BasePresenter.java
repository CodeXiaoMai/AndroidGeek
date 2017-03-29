
package com.xiaomai.geek.presenter;

import android.support.annotation.UiThread;

import com.xiaomai.geek.view.IBaseView;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by XiaoMai on 2017/3/24 15:17.
 */

public abstract class BasePresenter<V extends IBaseView> {

    private V mMVpView;

    protected CompositeSubscription mCompositeSubscription;

    @UiThread
    public void attachView(V view) {
        mMVpView = view;
        mCompositeSubscription = new CompositeSubscription();
    }

    @UiThread
    public void detachView() {
        mMVpView = null;
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
            mCompositeSubscription = null;
        }
    }

    public V getMvpView() {
        return mMVpView;
    }

}
