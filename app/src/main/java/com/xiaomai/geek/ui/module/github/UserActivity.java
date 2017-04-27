package com.xiaomai.geek.ui.module.github;

import android.content.Context;
import android.content.Intent;

import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.ui.base.BaseLoadActivity;

/**
 * Created by XiaoMai on 2017/4/26.
 */

public class UserActivity extends BaseLoadActivity {

    private static final String EXTRA_USER_NAME = "extra_user_name";

    private static final String EXTRA_USER = "extra_user";

    public static void launch(Context context, String userName) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(EXTRA_USER_NAME, userName);
        context.startActivity(intent);
    }

    public static void launch(Context context, User user) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(EXTRA_USER, user);
        context.startActivity(intent);
    }
}
