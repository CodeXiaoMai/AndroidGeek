package com.xiaomai.geek.ui.module.articel;

import android.content.Context;
import android.content.Intent;

import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Article;

/**
 * Created by XiaoMai on 2017/5/16.
 */

public class ArticleDetailActivity extends WebViewActivity {

    public static final String EXTRA_ARTICLE = "extra_article";

    public static void launch(Context context, Article article) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(EXTRA_ARTICLE, article);
        context.startActivity(intent);
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        Article article = intent.getParcelableExtra(EXTRA_ARTICLE);
        toolBar.setTitle(article.getName());
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(article.getUrl());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, fragment)
                .commit();
    }

    public void showToolBar(int duration) {
//        if (toolBar.getTop() < top) {
//            toolBar.setTop(toolBar.getTop() + duration);
//            flContainer.setTop(flContainer.getTop() + duration);
//        }
    }

    public void hideToolBar(int duration) {
//        if (toolBar.getTop() > -top && toolBar.getTop() <= 0) {
//            toolBar.setTop(toolBar.getTop() - duration);
//            flContainer.setTop(flContainer.getTop() - duration);
//        }
    }
}
