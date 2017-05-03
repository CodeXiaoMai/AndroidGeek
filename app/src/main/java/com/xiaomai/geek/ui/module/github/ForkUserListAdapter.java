package com.xiaomai.geek.ui.module.github;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.wrapper.ImageLoader;
import com.xiaomai.geek.data.module.Repo;

import java.util.List;


/**
 * Created by XiaoMai on 2017/5/2.
 */

public class ForkUserListAdapter extends BaseQuickAdapter<Repo> {

    public ForkUserListAdapter(List<Repo> data) {
        super(R.layout.item_user_head, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Repo repo) {
        ImageLoader.loadWithCircle(repo.getOwner().getAvatar_url(),
                (ImageView) baseViewHolder.getView(R.id.user_icon), R.drawable.github_blue);
    }
}
