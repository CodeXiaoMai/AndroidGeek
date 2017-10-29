package com.xiaomai.mvp;

/**
 * Created by xiaomai on 2017/10/25.
 */

public class BaseMvpPresenter<V extends IMvpView> implements IMvpPresenter<V> {

    protected final String TAG = getClass().getSimpleName();

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
