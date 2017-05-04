package com.xiaomai.geek.ui.module.github;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.di.IComponent;
import com.xiaomai.geek.di.component.DaggerGitHubComponent;
import com.xiaomai.geek.di.component.GitHubComponent;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.GitHubModule;
import com.xiaomai.geek.presenter.github.SearchPresenter;
import com.xiaomai.geek.ui.base.BaseLoadActivity;
import com.xiaomai.geek.view.ISearchView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/5/4.
 */

public class SearchActivity extends BaseLoadActivity implements ISearchView<ArrayList<Repo>>,
        IComponent<GitHubComponent>, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private RepoListAdapter mAdapter;

    private String mCurrentKey;

    private String mCurrentLanguage;

    @Inject
    SearchPresenter mPresenter;
    private SearchView mSearchView;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initViews();
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initViews() {
        setSupportActionBar(toolBar);
        setTitle("搜索");
        navView.setNavigationItemSelectedListener(this);
        mAdapter = new RepoListAdapter(null);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Repo repo = mAdapter.getItem(i);
                RepoDetailActivity.launch(SearchActivity.this, repo.getOwner().getLogin(), repo.getName());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mCurrentLanguage = "Java";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        mSearchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mCurrentKey = query;
                search();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public GitHubComponent getComponent() {
        return DaggerGitHubComponent.builder()
                .applicationComponent(GeekApplication.get(this).getComponent())
                .gitHubModule(new GitHubModule())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @Override
    public void showSearchResult(ArrayList<Repo> result) {
        mAdapter.setNewData(result);
        setTitle("关键词:\"" + mCurrentKey + "\"，语言:\"" + mCurrentLanguage + "\"");
        mSearchView.setActivated(false);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mCurrentLanguage = item.getTitle().toString();
        drawerLayout.closeDrawer(GravityCompat.START);
        search();
        return true;
    }

    private void search() {
        if (!TextUtils.isEmpty(mCurrentKey))
            mPresenter.searchRepo(mCurrentKey, mCurrentLanguage);
    }
}
