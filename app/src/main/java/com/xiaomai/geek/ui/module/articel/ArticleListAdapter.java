package com.xiaomai.geek.ui.module.articel;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Article;

import java.util.List;

/**
 * Created by XiaoMai on 2017/5/16.
 */

public class ArticleListAdapter extends BaseQuickAdapter<Article> {

    public ArticleListAdapter(List<Article> data) {
        super(R.layout.item_article, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Article article) {
        TextView tvName = holder.getView(R.id.tv_name);
        String articleName = article.getName();
        tvName.setText(articleName);
    }
}
