
package com.xiaomai.geek.presenter;

import com.xiaomai.mvp.BaseMvpPresenter;
import com.xiaomai.mvp.MvpView;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by XiaoMai on 2017/3/29 17:51.
 */

public class BaseRxPresenter<V extends MvpView> extends BaseMvpPresenter<V> {

    protected CompositeSubscription mCompositeSubscription;

    @Override
    public void attachView(V view) {
        super.attachView(view);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
            mCompositeSubscription = null;
        }
    }
}
