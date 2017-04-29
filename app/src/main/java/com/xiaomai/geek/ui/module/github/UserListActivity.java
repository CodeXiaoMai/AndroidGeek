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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
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
import com.xiaomai.mvp.lce.ILceView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/4/29.
 */

public class UserListActivity extends BaseLoadActivity implements ILceView<ArrayList<User>>, IComponent<GitHubComponent> {

    private static final String EXTRA_USER_NAME = "extra_user_name";

    private static final String ACTION_FOLLOWING = "com.xiaomai.geek.ACTION_FOLLOWING";

    private static final String ACTION_FOLLOWERS = "com.xiaomai.geek.ACTION_FOLLOWERS";

    @Inject
    UserListPresenter mPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    private UserListAdapter mAdapter;

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
        String userName = intent.getStringExtra(EXTRA_USER_NAME);
        String action = intent.getAction();
        boolean isSelf = AccountPref.isSelf(this, userName);
        if (ACTION_FOLLOWING.equals(action)) {
            setTitle(isSelf ? getString(R.string.my_following)
                    : getString(R.string.following, userName));
            mPresenter.loadUsers(userName, isSelf, GitHubApi.FOLLOWING);
        } else if (ACTION_FOLLOWERS.equals(action)) {
            setTitle(isSelf ? getString(R.string.my_followers)
                    : getString(R.string.followers, userName));
            mPresenter.loadUsers(userName, isSelf, GitHubApi.FOLLOWER);
        }
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
    }

    @Override
    public void showContent(ArrayList<User> data) {
        mAdapter.setNewData(data);
    }

    @Override
    public void showError(Throwable e) {
        showError(e);
    }

    @Override
    public void showEmpty() {

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
