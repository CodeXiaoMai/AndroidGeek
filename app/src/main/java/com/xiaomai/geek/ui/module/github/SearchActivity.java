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
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.Const;
import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.di.IComponent;
import com.xiaomai.geek.di.component.DaggerGitHubComponent;
import com.xiaomai.geek.di.component.GitHubComponent;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.GitHubModule;
import com.xiaomai.geek.presenter.github.SearchPresenter;
import com.xiaomai.geek.ui.base.BaseLoadActivity;
import com.xiaomai.geek.ui.base.EndlessRecyclerOnScrollListener;
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
    @BindView(R.id.empty_root_layout)
    RelativeLayout emptyRootLayout;
    @BindView(R.id.search_view)
    SearchView searchView;

    private RepoListAdapter mAdapter;

    private String mCurrentKey;

    private String mCurrentLanguage;

    @Inject
    SearchPresenter mPresenter;
    private int mCurrentPage;
    private TextView mFooterViewContent;
    private View mFooterView;

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
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void loadMore() {
                if (!TextUtils.isEmpty(mCurrentKey))
                    mPresenter.searchRepo(mCurrentKey, mCurrentLanguage, ++mCurrentPage, true);
            }
        });
        mFooterView = getLayoutInflater().inflate(R.layout.layout_load_more, recyclerView, false);
        mFooterViewContent = (TextView) mFooterView.findViewById(R.id.tv_content);
        mCurrentLanguage = "Java";

        searchView.setIconified(false);//输入框内icon不显示
        searchView.requestFocusFromTouch();//模拟焦点点击事件
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
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
        mAdapter.addFooterView(null);
        AppLog.e("clearfocus");
        searchView.clearFocus();
        invalidateOptionsMenu();
        if (result != null && result.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyRootLayout.setVisibility(View.GONE);
            mAdapter.setNewData(result);
            // 当结果不足 PAGE_SIZE 时很明显没有更多数据了。
            if (result.size() >= Const.PAGE_SIZE) {
                mFooterViewContent.setText("加载更多...");
            } else {
                mFooterViewContent.setText("加载完毕！");
            }
            mAdapter.addFooterView(mFooterView);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyRootLayout.setVisibility(View.VISIBLE);
        }
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mCurrentLanguage = item.getTitle().toString();
        drawerLayout.closeDrawer(GravityCompat.START);
        search();
        return true;
    }

    private void search() {
        mCurrentPage = 1;
        if (!TextUtils.isEmpty(mCurrentKey))
            mPresenter.searchRepo(mCurrentKey, mCurrentLanguage, 1);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
