
package com.xiaomai.mvp;

/**
 * Created by XiaoMai on 2017/3/29 17:14.
 */

public class BaseMvpPresenter<V extends MvpView> implements IMvpPresenter<V> {

    private V mMvpView;

    @Override
    public void attachView(V view) {
        mMvpView = view;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }
}
