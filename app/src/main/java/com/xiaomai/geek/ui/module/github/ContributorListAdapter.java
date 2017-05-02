package com.xiaomai.geek.ui.module.github;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.wrapper.ImageLoader;
import com.xiaomai.geek.data.module.User;

import java.util.List;

/**
 * Created by XiaoMai on 2017/5/2.
 */

public class ContributorListAdapter extends BaseQuickAdapter<User> {

    public ContributorListAdapter(List<User> data) {
        super(R.layout.item_user_head, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, User user) {
        ImageLoader.loadWithCircle(user.getAvatar_url(),
                (ImageView) baseViewHolder.getView(R.id.user_icon));
    }
}
