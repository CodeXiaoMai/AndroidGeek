
package com.xiaomai.mvp.lce;

import android.support.annotation.UiThread;

import com.xiaomai.mvp.MvpView;

/**
 * Created by XiaoMai on 2017/3/29 17:18.
 */

public interface ILceView<M> extends MvpView {

    @UiThread
    void showLoading();

    @UiThread
    void dismissLoading();

    @UiThread
    void showContent(M data);

    @UiThread
    void showError(Throwable e);

    @UiThread
    void showEmpty();
}
