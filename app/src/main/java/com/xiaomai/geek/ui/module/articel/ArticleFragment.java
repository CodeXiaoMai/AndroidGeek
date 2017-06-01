package com.xiaomai.geek.ui.module.articel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Chapter;
import com.xiaomai.geek.di.component.MainComponent;
import com.xiaomai.geek.presenter.article.ChapterPresenter;
import com.xiaomai.geek.ui.base.BaseFragment;
import com.xiaomai.mvp.lce.ILceView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by XiaoMai on 2017/5/16.
 */

public class ArticleFragment extends BaseFragment implements ILceView<List<Chapter>> {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.empty_root_layout)
    RelativeLayout emptyRootLayout;
    @BindView(R.id.error_root_layout)
    RelativeLayout errorRootLayout;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    Unbinder unbinder;

    @Inject
    ChapterPresenter mPresenter;

    ChapterListAdapter mAdapter;

    public static ArticleFragment newInstance() {
        ArticleFragment fragment = new ArticleFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
        mPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_article, null, false);
        unbinder = ButterKnife.bind(this, contentView);
        initViews();
        loadData();
        return contentView;
    }

    private void loadData() {
        recyclerView.setVisibility(View.VISIBLE);
        errorRootLayout.setVisibility(View.GONE);
        emptyRootLayout.setVisibility(View.GONE);
        mPresenter.getChapters();
    }

    private void initViews() {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new ChapterListAdapter(null);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Chapter chapter = mAdapter.getItem(i);
                ChapterActivity.launch(getActivity(), chapter);
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void dismissLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showContent(List<Chapter> data) {
        if (null != data && data.size() > 0) {
            mAdapter.setNewData(data);
            recyclerView.setVisibility(View.VISIBLE);
            emptyRootLayout.setVisibility(View.GONE);
            errorRootLayout.setVisibility(View.GONE);
        } else {
            showEmpty();
        }
    }

    @Override
    public void showError(Throwable e) {
        recyclerView.setVisibility(View.GONE);
        emptyRootLayout.setVisibility(View.GONE);
        errorRootLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmpty() {
        recyclerView.setVisibility(View.GONE);
        emptyRootLayout.setVisibility(View.VISIBLE);
        errorRootLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick({R.id.empty_root_layout, R.id.error_root_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.empty_root_layout:
                loadData();
                break;
            case R.id.error_root_layout:
                loadData();
                break;
        }
    }
}
