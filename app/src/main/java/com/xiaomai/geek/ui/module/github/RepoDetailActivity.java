package com.xiaomai.geek.ui.module.github;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.data.module.RepoDetail;
import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.data.pref.AccountPref;
import com.xiaomai.geek.di.IComponent;
import com.xiaomai.geek.di.component.DaggerGitHubComponent;
import com.xiaomai.geek.di.component.GitHubComponent;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.GitHubModule;
import com.xiaomai.geek.presenter.github.RepoDetailPresenter;
import com.xiaomai.geek.ui.base.BaseLoadActivity;
import com.xiaomai.geek.ui.widget.RepoItemView;
import com.xiaomai.geek.view.IRepoView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepoDetailActivity extends BaseLoadActivity implements IRepoView, IComponent<GitHubComponent> {

    private static final String EXTRA_OWNER = "extra_owner";

    private static final String EXTRA_REPO_NAME = "extra_repo_name";
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.repoItemView)
    RepoItemView repoItemView;
    @BindView(R.id.contributors_count)
    TextView contributorsCount;
    @BindView(R.id.contributor_list)
    RecyclerView contributorList;
    @BindView(R.id.contributor_layout)
    LinearLayout contributorLayout;
    @BindView(R.id.forks_count)
    TextView forksCount;
    @BindView(R.id.fork_list)
    RecyclerView forkList;
    @BindView(R.id.fork_layout)
    LinearLayout forkLayout;
    @BindView(R.id.readme_label)
    TextView readmeLabel;
    @BindView(R.id.readme_layout)
    LinearLayout readmeLayout;

    @BindView(R.id.content_root_layout)
    ScrollView contentRootLayout;

    private String mOwner;
    private String mRepoName;
    private RepoDetail mRepoDetail;

    @Inject
    RepoDetailPresenter mPresenter;

    private ContributorListAdapter mContributorListAdapter;
    private ForkUserListAdapter mForkUserListAdapter;
    private String mRepoUrl;

    public static void launch(Context context, String owner, String repoName) {
        Intent intent = new Intent(context, RepoDetailActivity.class);
        intent.putExtra(EXTRA_OWNER, owner);
        intent.putExtra(EXTRA_REPO_NAME, repoName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_repo_detail);
        ButterKnife.bind(this);
        initViews();
        mPresenter.attachView(this);
        loadData();
    }

    private void loadData() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        mOwner = intent.getStringExtra(EXTRA_OWNER);
        mRepoName = intent.getStringExtra(EXTRA_REPO_NAME);
        if (TextUtils.isEmpty(mOwner) || TextUtils.isEmpty(mRepoName))
            return;
        mPresenter.loadRepoDetails(mOwner, mRepoName);
        mRepoUrl = "https://github.com/" + mOwner + "/" + mRepoName;
    }

    private void initViews() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar)
            actionBar.setDisplayHomeAsUpEnabled(true);
        contributorList.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false));
        mContributorListAdapter = new ContributorListAdapter(null);
        mContributorListAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                User user = mContributorListAdapter.getItem(i);
                UserActivity.launch(RepoDetailActivity.this, user.getLogin());
            }
        });
        contributorList.setAdapter(mContributorListAdapter);

        forkList.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false));
        mForkUserListAdapter = new ForkUserListAdapter(null);
        mForkUserListAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Repo repo = mForkUserListAdapter.getItem(i);
                UserActivity.launch(RepoDetailActivity.this, repo.getOwner().getLogin());
            }
        });
        forkList.setAdapter(mForkUserListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.repo_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mOwner + "/" + mRepoName + "\n" + mRepoUrl);
                startActivity(Intent.createChooser(intent, "分享到"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void starSuccess() {
        repoItemView.setStarred(true);
        Snackbar.make(repoItemView, "Star成功", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void starFailed() {
        Snackbar.make(repoItemView, "Star失败", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void unStarSuccess() {
        repoItemView.setStarred(false);
        Snackbar.make(repoItemView, "UnStar成功", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void unStarFailed() {
        Snackbar.make(repoItemView, "UnStar失败", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showContent(RepoDetail detail) {
        mRepoDetail = detail;
        mOwner = detail.getBaseRepo().getOwner().getLogin();
        mRepoName = detail.getBaseRepo().getName();

        setTitle(mRepoName);
        contentRootLayout.setVisibility(View.VISIBLE);
        repoItemView.setRepo(detail.getBaseRepo());
        repoItemView.setOnRepoActionListener(new RepoItemView.onRepoActionListener() {
            @Override
            public void onStarAction(Repo repo) {
                if (AccountPref.checkLogin(RepoDetailActivity.this))
                    mPresenter.starRepo(repo.getOwner().getLogin(), repo.getName());
            }

            @Override
            public void onUnStarAction(Repo repo) {
                if (AccountPref.checkLogin(RepoDetailActivity.this))
                    mPresenter.unStarRepo(repo.getOwner().getLogin(), repo.getName());
            }

            @Override
            public void onUserAction(String userName) {
                UserActivity.launch(RepoDetailActivity.this, userName);
            }
        });

        int forks_count = detail.getBaseRepo().getForks_count();
        if (forks_count == 0)
            forkLayout.setVisibility(View.GONE);
        else {
            forkLayout.setVisibility(View.VISIBLE);
            forksCount.setText(getResources().getString(R.string.forks_count, forks_count));
            mForkUserListAdapter.setNewData(detail.getForks());
        }

        contributorsCount.setText(getResources().getString(R.string.contributors_count, detail.getContributors().size()));
        mContributorListAdapter.setNewData(detail.getContributors());
    }

    @Override
    public void showError(Throwable e) {
        contentRootLayout.setVisibility(View.GONE);
        error(e);
    }

    @Override
    public void showEmpty() {
        contentRootLayout.setVisibility(View.GONE);
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
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick({R.id.readme_layout, R.id.repoItemView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.readme_layout:
                ReadmeActivity.launch(this, mRepoDetail.getReadme());
                break;
            case R.id.repoItemView:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mRepoUrl));
                startActivity(intent);
                break;
        }
    }

}
