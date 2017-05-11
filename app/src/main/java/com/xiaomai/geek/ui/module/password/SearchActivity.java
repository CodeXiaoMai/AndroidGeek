package com.xiaomai.geek.ui.module.password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.GeekApplication;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.NotificationUtils;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.di.IComponent;
import com.xiaomai.geek.di.component.DaggerPasswordComponent;
import com.xiaomai.geek.di.component.PasswordComponent;
import com.xiaomai.geek.di.module.ActivityModule;
import com.xiaomai.geek.di.module.PasswordModule;
import com.xiaomai.geek.presenter.password.PasswordListPresenter;
import com.xiaomai.geek.ui.base.BaseLoadActivity;
import com.xiaomai.mvp.lce.ILceView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/5/11.
 */

public class SearchActivity extends BaseLoadActivity implements ILceView<List<Password>>, IComponent {

    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.empty_root_layout)
    RelativeLayout emptyRootLayout;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    private String mKeywords;

    @Inject
    PasswordListPresenter mPresenter;

    private PasswordListAdapter mAdapter;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_search_password);
        ButterKnife.bind(this);
        initViews();
        mPresenter.attachView(this);
    }

    private void initViews() {
        setSupportActionBar(toolBar);
        setTitle("搜索");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mKeywords = query;
                mPresenter.getPasswordsByKeywords(mContext, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mKeywords = newText;
                if (!TextUtils.isEmpty(newText)) {
                    mPresenter.getPasswordsByKeywords(mContext, newText);
                }
                return false;
            }
        });
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        mAdapter = new PasswordListAdapter(null);
        mAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Password password = mAdapter.getItem(i);
                        PasswordDetailActivity.launch(mContext, password);
                    }
                });
        mAdapter.setOnPublishClickListener(new PasswordListAdapter.OnPublishClickListener() {
            @Override
            public void onPublicClick(Password password) {
                NotificationUtils.showNotification(mContext, password,
                        NotificationUtils.TYPE_PASSWORD);
                NotificationUtils.showNotification(mContext, password,
                        NotificationUtils.TYPE_USER_NAME);
                Snackbar.make(recyclerView, "账号密码已发送到通知栏", Snackbar.LENGTH_LONG).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showContent(List<Password> data) {
        mAdapter.setNewData(data);
        mAdapter.setKeyWords(mKeywords);
        recyclerView.setVisibility(View.VISIBLE);
        emptyRootLayout.setVisibility(View.GONE);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public PasswordComponent getComponent() {
        return DaggerPasswordComponent.builder()
                .applicationComponent(GeekApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .passwordModule(new PasswordModule())
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
