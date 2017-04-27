
package com.xiaomai.geek.data.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.ui.module.LoginActivity;

/**
 * Created by XiaoMai on 2017/3/14 16:14.
 */

public class AccountPref {

    private static final String KEY_LOGIN_TOKEN = "login_token";

    private static final String KEY_LOGIN_USER = "login_user";

    private static final String PREFERENCE_ACCOUNT = "account_preference";

    private static SharedPreferences getPreference(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFERENCE_ACCOUNT,
                Context.MODE_PRIVATE);
    }

    public static void saveLoginToken(Context context, String loginToken) {
        getPreference(context).edit().putString(KEY_LOGIN_TOKEN, loginToken).apply();
    }

    public static String getLoginToken(Context context) {
        return getPreference(context).getString(KEY_LOGIN_TOKEN, "");
    }

    public static void saveLoginUser(Context context, User user) {
        String userJson = new Gson().toJson(user);
        getPreference(context).edit().putString(KEY_LOGIN_USER, userJson).apply();
    }

    public static User getLoginUser(Context context) {
        User user = null;
        String userJson = getPreference(context).getString(KEY_LOGIN_USER, "");
        if (!TextUtils.isEmpty(userJson)) {
            user = new Gson().fromJson(userJson, User.class);
        }
        return user;
    }

    public static void removeLoginUser(Context context) {
        getPreference(context).edit().remove(KEY_LOGIN_USER).apply();
    }

    public static boolean isLogin(Context context) {
        return !TextUtils.isEmpty(getLoginToken(context)) && getLoginUser(context) != null;
    }

    public static boolean isSelf(Context context, String userName) {
        User user = getLoginUser(context);
        return user != null && !TextUtils.isEmpty(userName) && userName.equals(user.getLogin());
    }

    public static boolean checkLogin(Context context) {
        if (isLogin(context)) {
            return true;
        }
        LoginActivity.launch(context);
        return false;
    }

}
