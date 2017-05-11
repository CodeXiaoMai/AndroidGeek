package com.xiaomai.geek.ui.base;

import android.support.design.widget.Snackbar;

import com.xiaomai.geek.ui.widget.LoadingView;
import com.xiaomai.mvp.lce.ILoadView;

/**
 * Created by XiaoMai on 2017/5/11.
 */

public class BaseLoadFragment extends BaseFragment implements ILoadView {

    private LoadingView mLoadingView;

    @Override
    public void showLoading() {
        if (mLoadingView == null)
            mLoadingView = new LoadingView(mContext, getLoadingMessage());
        mLoadingView.show();
    }

    @Override
    public void dismissLoading() {
        if (mLoadingView != null)
            mLoadingView.dismiss();
    }

    @Override
    public void error(Throwable e) {
        Snackbar.make(getActivity().getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    public String getLoadingMessage() {
        return "加载中...";
    }
}
