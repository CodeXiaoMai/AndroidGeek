package com.xiaomai.geek.ui.module.articel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.wrapper.ImageLoader;
import com.xiaomai.geek.data.module.Article;
import com.xiaomai.geek.data.module.Chapter;
import com.xiaomai.geek.ui.base.BaseActivity;
import com.xiaomai.geek.ui.module.github.RepoDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/5/16.
 */

public class ChapterActivity extends BaseActivity {

    private static final String EXTRA_CHAPTER = "extra_chapter";

    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.collapsingToolBar)
    CollapsingToolbarLayout collapsingToolBar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ArticleListAdapter mAdapter;

    public static void launch(Context context, Chapter chapter) {
        Intent intent = new Intent(context, ChapterActivity.class);
        intent.putExtra(EXTRA_CHAPTER, chapter);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArticleListAdapter(null);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Article article = mAdapter.getItem(i);
                ArticleDetailActivity.launch(ChapterActivity.this, article);
            }
        });
        Intent intent = getIntent();
        final Chapter chapter = intent.getParcelableExtra(EXTRA_CHAPTER);
        setTitle(chapter.getName());
        ImageLoader.load(this, chapter.getImage(), ivIcon, R.color.colorPrimary);
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String owner = chapter.getOwner();
                String repoName = chapter.getRepoName();
                if (!TextUtils.isEmpty(owner) && !TextUtils.isEmpty(repoName))
                    RepoDetailActivity.launch(ChapterActivity.this, owner, repoName);
            }
        });
        mAdapter.setNewData(chapter.getArticles());
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
