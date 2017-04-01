
package com.xiaomai.mvp.lce;

import android.support.annotation.UiThread;

import com.xiaomai.mvp.MvpView;

/**
 * Created by XiaoMai on 2017/3/29 17:19.
 */

public interface ILoadView extends MvpView {

    @UiThread
    void showLoading();

    @UiThread
    void dismissLoading();

    @UiThread
    void error(Throwable e);
}
