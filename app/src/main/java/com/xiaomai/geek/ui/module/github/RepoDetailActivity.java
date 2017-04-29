package com.xiaomai.geek.ui.module.github;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.di.IComponent;
import com.xiaomai.geek.di.component.DaggerGitHubComponent;
import com.xiaomai.geek.di.component.GitHubComponent;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.GitHubModule;
import com.xiaomai.geek.ui.base.BaseLoadActivity;
import com.xiaomai.geek.ui.widget.RepoItemView;
import com.xiaomai.geek.view.IRepoView;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.code_layout)
    LinearLayout codeLayout;
    @BindView(R.id.readme_label)
    TextView readmeLabel;
    @BindView(R.id.readme_layout)
    LinearLayout readmeLayout;

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

    }

    private void initViews() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar)
            actionBar.setDisplayHomeAsUpEnabled(true);

        contributorList.setLayoutManager(new GridLayoutManager(this, 1, LinearLayoutManager.HORIZONTAL, false));
        
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
    public void starSuccess() {

    }

    @Override
    public void starFailed() {

    }

    @Override
    public void unStarSuccess() {

    }

    @Override
    public void unStarFailed() {

    }

    @Override
    public void showContent(Repo data) {

    }

    @Override
    public void showError(Throwable e) {

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
}
