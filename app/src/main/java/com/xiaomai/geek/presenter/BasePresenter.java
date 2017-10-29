package com.xiaomai.geek.presenter;

import com.xiaomai.mvp.BaseMvpPresenter;
import com.xiaomai.mvp.IMvpView;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by xiaomai on 2017/10/26.
 */

public class BasePresenter<V extends IMvpView> extends BaseMvpPresenter<V> {
    
    protected CompositeDisposable mCompositeDisposable;

    @Override
    public void attachView(V view) {
        super.attachView(view);

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        super.detachView();

        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }
}
