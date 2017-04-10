
package com.xiaomai.geek.data.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.xiaomai.geek.common.utils.SecretUtil;

/**
 * Created by XiaoMai on 2017/4/6 18:48.
 */

public class PasswordPref {

    private static final String PREF_NAME = "com.xiaomai.geek.password_preference.xml";

    private static final String KEY_PASSWORD = "password";

    private static SharedPreferences getPreferences(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
    }

    public static boolean isPasswordCorrect(Context context, String password) {
        return getPassword(context).equals(SecretUtil.md5(password));
    }

    public static void savePassword(Context context, String password) {
        getPreferences(context).edit().putString(KEY_PASSWORD, SecretUtil.md5(password)).apply();
    }

    private static String getPassword(Context context) {
        return getPreferences(context).getString(KEY_PASSWORD, "");
    }

    public static boolean hasPassword(Context context) {
        return !TextUtils.isEmpty(getPassword(context));
    }
}
