package com.xiaomai.geek.ui.module.articel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.xiaomai.geek.R;
import com.xiaomai.geek.ui.base.BaseLoadActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/5/16.
 */

public class BaseWebViewActivity extends BaseLoadActivity {

    public static final String EXTRA_URL = "extra_url";

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    public static void launch(Context context, String url, String title) {
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        initViews();
    }

    protected void initViews() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(EXTRA_URL);
        String title = intent.getStringExtra(Intent.EXTRA_TITLE);
        toolBar.setTitle(title);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        WebViewFragment fragment = WebViewFragment.newInstance(url);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, fragment, String.valueOf(fragment.hashCode()))
                .commit();
    }
}
