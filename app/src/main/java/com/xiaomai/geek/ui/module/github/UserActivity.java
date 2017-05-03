package com.xiaomai.geek.ui.module.github;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.data.pref.AccountPref;
import com.xiaomai.geek.di.IComponent;
import com.xiaomai.geek.di.component.DaggerGitHubComponent;
import com.xiaomai.geek.di.component.GitHubComponent;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.GitHubModule;
import com.xiaomai.geek.presenter.github.UserPresenter;
import com.xiaomai.geek.ui.base.BaseLoadActivity;
import com.xiaomai.geek.ui.widget.UserCard;
import com.xiaomai.mvp.lce.ILceView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XiaoMai on 2017/4/26.
 */

public class UserActivity extends BaseLoadActivity implements ILceView<User>, IComponent<GitHubComponent> {

    private static final String EXTRA_USER_NAME = "extra_user_name";

    private static final String EXTRA_USER = "extra_user";
    @BindView(R.id.userCard)
    UserCard userCard;
    @BindView(R.id.repo)
    TextView repo;
    @BindView(R.id.starred)
    TextView starred;
    @BindView(R.id.following)
    TextView following;
    @BindView(R.id.followers)
    TextView followers;
    @BindView(R.id.contentView)
    LinearLayout contentView;

    @Inject
    UserPresenter mPresenter;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.bt_logout)
    Button btLogout;
    private String mUserName;

    public static void launch(Context context, String userName) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(EXTRA_USER_NAME, userName);
        context.startActivity(intent);
    }

    public static void launch(Context context, User user) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(EXTRA_USER, user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        initViews();

        mPresenter.attachView(this);
        loadUser();
    }

    private void initViews() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadUser() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        User user = intent.getParcelableExtra(EXTRA_USER);
        if (user == null) {
            mUserName = intent.getStringExtra(EXTRA_USER_NAME);
            setTitle(mUserName);
            mPresenter.getSingleUser(mUserName);
        } else {
            mUserName = user.getLogin();
            setTitle(mUserName);
            userCard.setUser(user);
        }
        if (!AccountPref.isSelf(this, mUserName))
            btLogout.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        contentView.setVisibility(View.GONE);
    }

    @Override
    public void showContent(User data) {
        contentView.setVisibility(View.VISIBLE);
        userCard.setUser(data);
    }

    @Override
    public void showError(Throwable e) {
        error(e);
    }

    @Override
    public void showEmpty() {

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

    @Override
    public GitHubComponent getComponent() {
        return DaggerGitHubComponent.builder()
                .applicationComponent(GeekApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .gitHubModule(new GitHubModule())
                .build();
    }

    @OnClick({R.id.repo, R.id.starred, R.id.following, R.id.followers, R.id.bt_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.repo:
                RepoListActivity.launchToShowRepos(this, mUserName);
                break;
            case R.id.starred:
                RepoListActivity.launchToShowStars(this, mUserName);
                break;
            case R.id.following:
                UserListActivity.launchToShowFollowing(this, mUserName);
                break;
            case R.id.followers:
                UserListActivity.launchToShowFollowers(this, mUserName);
                break;
            case R.id.bt_logout:
                showLogoutDialog();
                break;
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setMessage("退出登录")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AccountPref.removeLoginUser(UserActivity.this);
                        finish();
                    }
                })
                .create().show();
    }

}
