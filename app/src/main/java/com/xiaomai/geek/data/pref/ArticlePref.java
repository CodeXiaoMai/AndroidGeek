package com.xiaomai.geek.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by XiaoMai on 2017/5/17.
 */

public class ArticlePref {

    private static final String PREFERENCE_ARTICLE = "article_preference";

    private static SharedPreferences getPreference(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFERENCE_ARTICLE,
                Context.MODE_PRIVATE);
    }

    public static void saveReadProgress(Context context, String url, int progress) {
        getPreference(context).edit().putInt(url, progress).apply();
    }

    public static int getReadProgress(Context context, String url) {
        return getPreference(context).getInt(url, 0);
    }
}
