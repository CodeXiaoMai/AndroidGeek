
package com.xiaomai.geek.module.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.xiaomai.geek.R;
import com.xiaomai.geek.presenter.IBasePresenter;
import com.xiaomai.geek.view.IBaseView;
import com.xiaomai.geek.wedget.EmptyView;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/3/24 15:01.
 * 
 * @param <T>
 */

public abstract class BaseActivity<T extends IBasePresenter> extends RxAppCompatActivity
        implements IBaseView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        ButterKnife.bind(this);
        initInjector();
        initViews();
        initSwipeRefresh();
        updateViews(false);
    }

    /**
     * 把 EmptyLayout 放在基类统一处理，
     * 
     * @Nullable 表明 View 可以为 null，详细可看 ButterKnife
     */
    @Nullable
    @BindView(R.id.layout_empty)
    protected EmptyView mEmptyView;

    /**
     * 把 Presenter 提取到基类需要配合基类的 initInjector() 进行注入，</br>
     * 如果继承这个基类则必定要提供一个 Presenter 注入方法， 该 APP 所有 Presenter 都是在 Module
     * 提供注入实现，也可以选择提供另外不带 Presenter 的基类
     */
    @Inject
    protected T mPresenter;

    @Nullable
    // @BindView(R.id.swip_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    /**
     * 绑定布局文件
     * 
     * @return 布局文件id
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    /**
     * Dagger 注入
     */
    protected abstract void initInjector();

    protected abstract void initViews();

    protected abstract void updateViews(boolean isRefresh);

    private void initSwipeRefresh() {
        if (mSwipeRefresh != null) {
            // SwipeRefreshH
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading() {
        if (mEmptyView != null) {
            mEmptyView.setStatus(EmptyView.STATUS_LOADING);
        }
    }

    @Override
    public void disMissLoading() {
        if (mEmptyView != null)
            mEmptyView.hide();
    }

    @Override
    public void showError(EmptyView.OnRetryListener onRetryListener) {
        if (mEmptyView != null) {
            mEmptyView.setStatus(EmptyView.STATUS_ERROR);
            mEmptyView.setOnRetryListener(onRetryListener);
        }
    }

    @Override
    public void finishRefresh() {
        if (mSwipeRefresh != null) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnable, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnable);
    }

    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnable, int resTitle) {
        initToolBar(toolbar, homeAsUpEnable, getString(resTitle));
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
