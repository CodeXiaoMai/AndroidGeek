
package com.xiaomai.geek.module.news;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.xiaomai.geek.R;
import com.xiaomai.geek.api.bean.NewsInfo;
import com.xiaomai.geek.di.component.DaggerNewsContainerComponent;
import com.xiaomai.geek.di.modules.NewsContainerModule;
import com.xiaomai.geek.module.adapter.NewsContainerAdapter;
import com.xiaomai.geek.module.base.BaseFragment;
import com.xiaomai.geek.presenter.news.NewsPresenter;
import com.xiaomai.geek.view.NewsView;
import com.xiaomai.geek.widget.EmptyView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by XiaoMai on 2017/3/27 14:28.
 */

public class NewsContainerFragment extends BaseFragment<NewsPresenter> implements NewsView {
    @BindView(R.id.too_bar)
    Toolbar tooBar;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Inject
    NewsContainerAdapter mAdapter;

    List<Fragment> fragments;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_news_container;
    }

    @Override
    protected void initInjector() {
        DaggerNewsContainerComponent.builder().applicationComponent(getApplicationComponent())
                .newsContainerModule(new NewsContainerModule(this)).build().inject(this);
    }

    @Override
    protected void initViews() {
        mPresenter.attachView(this);
        initToolBar(tooBar, true, R.string.news);
        fragments = new ArrayList<>();
        NewsListFragment fragment = NewsListFragment.newInstance("");
        NewsListFragment fragment2 = NewsListFragment.newInstance("");
        fragments.add(fragment);
        fragments.add(fragment2);
        mAdapter.setItems(fragments, new String[]{"头条", "@"});
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        // mPresenter.
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void disMissLoading() {

    }

    @Override
    public void finishRefresh() {

    }

    @Override
    public void showContent(NewsInfo data) {

    }

    @Override
    public void showError(EmptyView.OnRetryListener onRetryListener) {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return null;
    }

}
