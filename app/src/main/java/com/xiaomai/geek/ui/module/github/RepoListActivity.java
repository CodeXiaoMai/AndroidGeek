package com.xiaomai.geek.ui.module.github;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.Const;
import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.data.pref.AccountPref;
import com.xiaomai.geek.di.IComponent;
import com.xiaomai.geek.di.component.DaggerGitHubComponent;
import com.xiaomai.geek.di.component.GitHubComponent;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.GitHubModule;
import com.xiaomai.geek.presenter.github.RepoListPresenter;
import com.xiaomai.geek.ui.base.BaseLoadActivity;
import com.xiaomai.geek.ui.base.EndlessRecyclerOnScrollListener;
import com.xiaomai.geek.view.ILoadMoreView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/4/28.
 */

public class RepoListActivity extends BaseLoadActivity implements ILoadMoreView<ArrayList<Repo>>, IComponent<GitHubComponent> {

    private static final String EXTRA_USER_NAME = "extra_user_name";

    private static final String ACTION_REPOS = "com.xiaomai.geek.ACTION_REPOS";

    private static final String ACTION_STARRED_REPOS = "com.xiaomai.geek.ACTION_STARRED_REPOS";
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.repo_list)
    RecyclerView repoList;

    @Inject
    RepoListPresenter mPresenter;
    @BindView(R.id.empty_root_layout)
    RelativeLayout emptyRootLayout;

    private RepoListAdapter mAdapter;
    private String mUserName;
    private boolean mIsSelf;
    private int mType;
    private int mCurrentPage = 1;
    private View mFooterView;
    private TextView mFooterViewContent;

    public static void launchToShowRepos(Context context, String username) {
        Intent intent = new Intent(context, RepoListActivity.class);
        intent.putExtra(EXTRA_USER_NAME, username);
        intent.setAction(ACTION_REPOS);
        context.startActivity(intent);
    }

    public static void launchToShowStars(Context context, String username) {
        Intent intent = new Intent(context, RepoListActivity.class);
        intent.putExtra(EXTRA_USER_NAME, username);
        intent.setAction(ACTION_STARRED_REPOS);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_repo_list);
        ButterKnife.bind(this);

        initView();
        mPresenter.attachView(this);
        loadRepos();
    }

    private void loadRepos() {
        Intent intent = getIntent();
        if (null == intent)
            return;
        String action = intent.getAction();
        mUserName = intent.getStringExtra(EXTRA_USER_NAME);
        mIsSelf = AccountPref.isSelf(this, mUserName);
        if (ACTION_REPOS.equals(action)) {
            setTitle(mIsSelf ? getString(R.string.my_repositories)
                    : getString(R.string.repositories, mUserName));
            mType = GitHubApi.OWNER_REPO;
        } else if (ACTION_STARRED_REPOS.equals(action)) {
            setTitle(mIsSelf ? getString(R.string.my_stars)
                    : getString(R.string.your_stars, mUserName));
            mType = GitHubApi.STARRED_REPO;
        }
        mPresenter.loadRepos(mUserName, mIsSelf, mType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initView() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar)
            actionBar.setDisplayHomeAsUpEnabled(true);

        repoList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RepoListAdapter(null);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Repo repo = mAdapter.getItem(i);
                RepoDetailActivity.launch(mContext, repo.getOwner().getLogin(), repo.getName());
            }
        });
        repoList.setAdapter(mAdapter);
        repoList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void loadMore() {
                mPresenter.loadRepos(mUserName, mIsSelf, mType, ++mCurrentPage, true);
            }
        });
        mFooterView = getLayoutInflater().inflate(R.layout.layout_load_more, repoList, false);
        mFooterViewContent = (TextView) mFooterView.findViewById(R.id.tv_content);
    }

    @Override
    public GitHubComponent getComponent() {
        return DaggerGitHubComponent.builder()
                .applicationComponent(GeekApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .gitHubModule(new GitHubModule())
                .build();
    }

    @Override
    public void showContent(ArrayList<Repo> result) {
        mAdapter.setNewData(result);
        repoList.setVisibility(View.VISIBLE);
        emptyRootLayout.setVisibility(View.GONE);
        // 当结果不足 PAGE_SIZE 时很明显没有更多数据了。
        if (result.size() >= Const.PAGE_SIZE) {
            mFooterViewContent.setText("加载更多...");
        } else {
            mFooterViewContent.setText("加载完毕！");
        }
        mAdapter.addFooterView(mFooterView);
    }

    @Override
    public void showError(Throwable e) {
        error(e);
    }

    @Override
    public void showEmpty() {
        repoList.setVisibility(View.GONE);
        emptyRootLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoreResult(ArrayList<Repo> result) {
        mAdapter.addFooterView(null);
        if (result != null && result.size() > 0) {
            // 当结果不足 PAGE_SIZE 时很明显没有更多数据了。
            if (result.size() >= Const.PAGE_SIZE) {
                mFooterViewContent.setText("加载更多...");
            } else {
                mFooterViewContent.setText("加载完毕！");
            }
            mAdapter.notifyDataChangedAfterLoadMore(result, false);
        } else {
            mFooterViewContent.setText("加载完毕！");
        }
        mAdapter.addFooterView(mFooterView);
    }

    @Override
    public void loadComplete() {
        mFooterViewContent.setText("加载完毕！");
        mAdapter.addFooterView(mFooterView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
