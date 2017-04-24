package com.xiaomai.geek.ui.module.github;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Repo;

import java.util.List;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public class TrendingListAdapter extends BaseQuickAdapter<Repo> {

    public TrendingListAdapter(List<Repo> data) {
        super(R.layout.item_repo, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Repo repo) {
        holder.setText(R.id.tv_title, repo.getName());
        holder.setText(R.id.tv_description, repo.getDescription());
    }
}
