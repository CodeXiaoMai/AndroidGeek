package com.xiaomai.geek.data.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by XiaoMai on 2017/11/29.
 */

public class PasswordPref extends BasePreference {

    private static final String PREF_NAME = "password_preference";
    private static final String KEY_PASSWORD = "key_password";

    public static void savePassword(@NonNull Context context, String password) {
        final SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = preferences.edit();
        edit.putString(KEY_PASSWORD, password);
        edit.apply();
    }

    @NonNull
    public static String getPassword(@NonNull Context context) {
        final SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_PASSWORD, "");
    }
}
