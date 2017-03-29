
package com.xiaomai.geek.module.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;
import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.di.component.ApplicationComponent;
import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.geek.widget.EmptyView;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/3/27 14:29.
 * 
 * @param <T>
 */

public abstract class BaseFragment<T extends BasePresenter> extends RxFragment {

    @Nullable
    @BindView(R.id.layout_empty)
    EmptyView mEmptyView;

    @android.support.annotation.Nullable
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @Inject
    protected T mPresenter;

    protected Context mContext;

    private View mRootView;

    @Override
    public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @android.support.annotation.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
            @android.support.annotation.Nullable ViewGroup container,
            @android.support.annotation.Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(attachLayoutRes(), null);
            ButterKnife.bind(this, mRootView);
            initInjector();
            initViews();
            initSwipeRefresh();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null)
            parent.removeAllViews();
        return mRootView;
    }

    /**
     * 绑定布局文件
     * 
     * @return 布局文件ID
     */
    protected abstract int attachLayoutRes();

    /**
     * Dagger 注入
     */
    protected abstract void initInjector();

    /**
     * 初始化视图控件
     */
    protected abstract void initViews();

    private void initSwipeRefresh() {
        if (mSwipeRefresh != null) {
            // SwipeRefreshH
        }
    }

    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnable, String title) {
        ((BaseActivity) getActivity()).initToolBar(toolbar, homeAsUpEnable, title);
    }

    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnable, @StringRes int resId) {
        initToolBar(toolbar, homeAsUpEnable, getString(resId));
    }

    protected ApplicationComponent getApplicationComponent() {
        return GeekApplication.get(mContext).getApplicationComponent();
    }

    /**
     * @param isRefresh 用来判断是否为下拉刷新调用，下拉刷新的时候不应该再显示加载界面和异常界面
     */
    protected abstract void updateViews(boolean isRefresh);

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }


}
