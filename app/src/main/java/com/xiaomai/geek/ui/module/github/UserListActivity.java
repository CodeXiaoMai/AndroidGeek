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
import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.data.pref.AccountPref;
import com.xiaomai.geek.di.IComponent;
import com.xiaomai.geek.di.component.DaggerGitHubComponent;
import com.xiaomai.geek.di.component.GitHubComponent;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.GitHubModule;
import com.xiaomai.geek.presenter.github.UserListPresenter;
import com.xiaomai.geek.ui.base.BaseLoadActivity;
import com.xiaomai.geek.ui.base.EndlessRecyclerOnScrollListener;
import com.xiaomai.geek.view.ILoadMoreView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/4/29.
 */

public class UserListActivity extends BaseLoadActivity implements ILoadMoreView<ArrayList<User>>, IComponent<GitHubComponent> {

    private static final String EXTRA_USER_NAME = "extra_user_name";

    private static final String ACTION_FOLLOWING = "com.xiaomai.geek.ACTION_FOLLOWING";

    private static final String ACTION_FOLLOWERS = "com.xiaomai.geek.ACTION_FOLLOWERS";

    @Inject
    UserListPresenter mPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.empty_title)
    TextView emptyTitle;
    @BindView(R.id.empty_desc)
    TextView emptyDesc;
    @BindView(R.id.empty_root_layout)
    RelativeLayout emptyRootLayout;

    private View mFooterView;
    private TextView mFooterViewContent;

    private UserListAdapter mAdapter;
    private String mUserName;
    private boolean mIsSelf;
    private int mType;
    private int mCurrentPage = 1;

    public static void launchToShowFollowing(Context context, String username) {
        Intent intent = new Intent(context, UserListActivity.class);
        intent.putExtra(EXTRA_USER_NAME, username);
        intent.setAction(ACTION_FOLLOWING);
        context.startActivity(intent);
    }

    public static void launchToShowFollowers(Context context, String username) {
        Intent intent = new Intent(context, UserListActivity.class);
        intent.putExtra(EXTRA_USER_NAME, username);
        intent.setAction(ACTION_FOLLOWERS);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        initViews();
        mPresenter.attachView(this);
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void loadData() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        mUserName = intent.getStringExtra(EXTRA_USER_NAME);
        String action = intent.getAction();
        mIsSelf = AccountPref.isSelf(this, mUserName);
        if (ACTION_FOLLOWING.equals(action)) {
            setTitle(mIsSelf ? getString(R.string.my_following)
                    : getString(R.string.following, mUserName));
            mType = GitHubApi.FOLLOWING;
        } else if (ACTION_FOLLOWERS.equals(action)) {
            setTitle(mIsSelf ? getString(R.string.my_followers)
                    : getString(R.string.followers, mUserName));
            mType = GitHubApi.FOLLOWER;
        }
        mPresenter.loadUsers(mUserName, mIsSelf, mType);
    }

    private void initViews() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar)
            actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UserListAdapter(null);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                User user = mAdapter.getItem(i);
                UserActivity.launch(mContext, user.getLogin());
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void loadMore() {
                mPresenter.loadUsers(mUserName, mIsSelf, mType, ++mCurrentPage, true);
            }
        });
        mFooterView = getLayoutInflater().inflate(R.layout.layout_load_more, recyclerView, false);
        mFooterViewContent = (TextView) mFooterView.findViewById(R.id.tv_content);
    }

    @Override
    public void showContent(ArrayList<User> data) {
        mAdapter.setNewData(data);
        recyclerView.setVisibility(View.VISIBLE);
        emptyRootLayout.setVisibility(View.GONE);
        // 当结果不足 PAGE_SIZE 时很明显没有更多数据了。
        if (data.size() >= Const.PAGE_SIZE) {
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
        recyclerView.setVisibility(View.GONE);
        emptyRootLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoreResult(ArrayList<User> result) {
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
    public GitHubComponent getComponent() {
        return DaggerGitHubComponent.builder()
                .applicationComponent(GeekApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .gitHubModule(new GitHubModule())
                .build();
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
