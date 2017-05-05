package com.xiaomai.geek.view;

import com.xiaomai.mvp.MvpView;

/**
 * Created by XiaoMai on 2017/5/5.
 */

public interface ILoadMoreView<M> extends MvpView {

    void showLoading();

    void dismissLoading();

    void showContent(M result);

    void showMoreResult(M result);

    void loadComplete();

    void showEmpty();

    void showError(Throwable e);
}
