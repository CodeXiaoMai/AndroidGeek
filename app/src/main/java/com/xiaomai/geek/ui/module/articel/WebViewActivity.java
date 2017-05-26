package com.xiaomai.geek.ui.module.articel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.ShareUtils;
import com.xiaomai.geek.ui.base.BaseLoadActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/5/16.
 */

public class WebViewActivity extends BaseLoadActivity {

    public static final String EXTRA_URL = "extra_url";

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    protected String mTitle;

    protected String mUrl;

    public static void launch(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        initViews();
        loadData();
    }

    protected void loadData() {
        Intent intent = getIntent();
        if (null == intent)
            return;
        mUrl = intent.getStringExtra(EXTRA_URL);
        if (TextUtils.isEmpty(mUrl))
            return;
        WebViewFragment fragment = WebViewFragment.newInstance(mUrl);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, fragment, String.valueOf(fragment.hashCode()))
                .commit();
    }

    protected void initViews() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        mTitle = intent.getStringExtra(Intent.EXTRA_TITLE);
        toolBar.setTitle(mTitle);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_share:
                if (!TextUtils.isEmpty(mUrl)) {
                    ShareUtils.share(WebViewActivity.this, mTitle, mUrl);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
