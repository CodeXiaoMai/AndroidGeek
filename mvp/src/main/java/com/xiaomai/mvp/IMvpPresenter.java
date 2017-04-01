
package com.xiaomai.mvp;

import android.support.annotation.UiThread;

/**
 * Created by XiaoMai on 2017/3/29 17:14.
 */

public interface IMvpPresenter<V extends MvpView> {

    @UiThread
    void attachView(V view);

    @UiThread
    void detachView();
}
