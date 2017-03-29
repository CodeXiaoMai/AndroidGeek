
package com.xiaomai.geek.module.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.api.bean.NewsInfo;
import com.xiaomai.geek.di.component.DaggerNewsListComponent;
import com.xiaomai.geek.di.modules.NewsListModule;
import com.xiaomai.geek.module.adapter.NewsListAdapter;
import com.xiaomai.geek.module.base.BaseFragment;
import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.geek.presenter.news.NewsListPresenter;
import com.xiaomai.geek.view.NewsListView;
import com.xiaomai.geek.widget.EmptyView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by XiaoMai on 2017/3/28 11:21.
 */

public class NewsListFragment extends BaseFragment<BasePresenter> implements NewsListView {

    public static final String KEY_NEWS_TYPE = "key_news_type";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String mNewId;

    @Inject
    NewsListAdapter mAdapter;

    public static NewsListFragment newInstance(String newsId) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NEWS_TYPE, newsId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mNewId = bundle.getString(KEY_NEWS_TYPE);
        }
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initInjector() {
        DaggerNewsListComponent.builder()
                .applicationComponent(GeekApplication.get(mContext).getApplicationComponent())
                .newsListModule(new NewsListModule(this, null)).build().inject(this);
    }

    @Override
    protected void initViews() {
        mPresenter.attachView(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ((NewsListPresenter) mPresenter).getNewList("T1348647909107", 1, false);
    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }

    @Override
    public void showAd(List<NewsInfo> ads) {

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
    public void showContent(List<NewsInfo> data) {
        mAdapter.setNewData(data);
    }

    @Override
    public void showError(EmptyView.OnRetryListener onRetryListener) {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return null;
    }

}
