
package com.xiaomai.geek.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomai.geek.R;
import com.xiaomai.geek.common.wrapper.ImageLoader;
import com.xiaomai.geek.data.module.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by XiaoMai on 2017/3/21 13:08.
 */

public class UserCard extends FrameLayout {

    @BindView(R.id.user_icon)
    ImageView userIcon;

    @BindView(R.id.userName)
    TextView userName;

    @BindView(R.id.bio)
    TextView bio;

    @BindView(R.id.company)
    TextView company;

    @BindView(R.id.location)
    TextView location;

    @BindView(R.id.blog)
    TextView blog;

    @BindView(R.id.email)
    TextView email;

    public UserCard(Context context) {
        this(context, null);
    }

    public UserCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.card_user, this);
        ButterKnife.bind(this);
    }

    public void setUser(User user) {
        ImageLoader.loadWithCircle(getContext(), user.getAvatar_url(), userIcon, R.drawable.github);

        String displayName = TextUtils.isEmpty(user.getName()) ? user.getLogin() : user.getName();
        userName.setText(displayName);

        if (TextUtils.isEmpty(user.getBio()))
            bio.setVisibility(GONE);
        else {
            bio.setVisibility(VISIBLE);
            bio.setText(user.getBio());
        }

        if (TextUtils.isEmpty(user.getCompany()))
            company.setVisibility(GONE);
        else {
            company.setText(user.getCompany());
            company.setVisibility(VISIBLE);
        }

        if (TextUtils.isEmpty(user.getBlog()))
            blog.setVisibility(GONE);
        else {
            blog.setVisibility(VISIBLE);
            blog.setText(user.getBlog());
        }

        if (TextUtils.isEmpty(user.getLocation()))
            location.setVisibility(GONE);
        else {
            location.setVisibility(VISIBLE);
            location.setText(user.getLocation());
        }

        if (TextUtils.isEmpty(user.getEmail()))
            email.setVisibility(GONE);
        else {
            email.setVisibility(VISIBLE);
            email.setText(user.getEmail());
        }
    }
}
