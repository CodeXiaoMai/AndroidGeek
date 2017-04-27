package com.xiaomai.geek.ui.module.github;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.StringUtil;
import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.di.component.MainComponent;
import com.xiaomai.geek.presenter.github.TrendingPresenter;
import com.xiaomai.geek.ui.base.BaseFragment;
import com.xiaomai.mvp.lce.ILceView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by XiaoMai on 2017/4/21.
 */

public class TrendingFragment extends BaseFragment implements ILceView<ArrayList<Repo>> {

    public static final String EXTRA_LANGUAGE = "extra_language";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.empty_title)
    TextView emptyTitle;
    @BindView(R.id.empty_desc)
    TextView emptyDesc;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    @BindView(R.id.empty_root_layout)
    RelativeLayout emptyRootLayout;
    @BindView(R.id.error_title)
    TextView errorTitle;
    @BindView(R.id.error_desc)
    TextView errorDesc;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Inject
    TrendingPresenter mPresenter;
    @BindView(R.id.error_root_layout)
    RelativeLayout errorRootLayout;
    private String mCurrentLanguage;
    private TrendingListAdapter mAdapter;

    public static TrendingFragment newInstance(String tag) {
        TrendingFragment fragment = new TrendingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_LANGUAGE, tag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);

        Bundle arguments = getArguments();
        if (arguments == null) {
            mCurrentLanguage = GitHubApi.LANG_JAVA;
        } else {
            mCurrentLanguage = arguments.getString(EXTRA_LANGUAGE, GitHubApi.LANG_JAVA);
        }
        mPresenter.attachView(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_github_trending, null);
        unbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reloadData();
    }

    private void initViews() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });

        mAdapter = new TrendingListAdapter(null);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Repo repo = mAdapter.getItem(i);
                String repoName = StringUtil.replaceAllBlank(repo.getName());
                String owner = repo.getOwner().getLogin();
                RepoDetailActivity.launch(getActivity(), owner, repoName);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    private void reloadData() {
        recyclerView.setVisibility(View.VISIBLE);
        errorRootLayout.setVisibility(View.GONE);
        emptyRootLayout.setVisibility(View.GONE);
        mPresenter.loadTrendingRepos(mCurrentLanguage);
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
                reloadData();
                break;
            case R.id.error_root_layout:
                reloadData();
                break;
        }
    }

    @Override
    public void showLoading() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    @Override
    public void dismissLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showContent(ArrayList<Repo> data) {
        if (data != null) {
            recyclerView.setVisibility(View.VISIBLE);
            mAdapter.setNewData(data);
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
        errorRootLayout.setVisibility(View.GONE);
        emptyRootLayout.setVisibility(View.VISIBLE);
    }

}
