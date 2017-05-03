package com.xiaomai.geek.ui.module.github;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaomai.geek.R;
import com.xiaomai.geek.common.wrapper.ImageLoader;
import com.xiaomai.geek.data.module.User;

import java.util.List;

/**
 * Created by XiaoMai on 2017/4/29.
 */

public class UserListAdapter extends BaseQuickAdapter<User> {
    public UserListAdapter(List<User> data) {
        super(R.layout.item_user, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, User user) {
        ImageView icon = holder.getView(R.id.user_icon);
        ImageLoader.loadWithCircle(icon.getContext(), user.getAvatar_url(), icon, R.drawable.github_blue);
        holder.setText(R.id.userName, user.getLogin());
    }
}
