package com.xiaomai.geek.ui.module.github;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.StringUtil;
import com.xiaomai.geek.data.module.Repo;

import java.util.List;

/**
 * Created by XiaoMai on 2017/4/28.
 */

public class RepoListAdapter extends BaseQuickAdapter<Repo> {

    public RepoListAdapter(List<Repo> data) {
        super(R.layout.item_repo, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Repo repo) {
        String repoName = repo.getName();
        String repoDescription = repo.getDescription();
        if (TextUtils.isEmpty(repoName)) {
            holder.getView(R.id.tv_title).setVisibility(View.GONE);
        } else {
            holder.setText(R.id.tv_title, StringUtil.replaceAllBlank(repoName));
        }
        if (TextUtils.isEmpty(repoDescription)) {
            holder.getView(R.id.tv_description).setVisibility(View.GONE);
        } else {
            holder.setText(R.id.tv_description, StringUtil.trimNewLine(repoDescription));
        }
    }
}
