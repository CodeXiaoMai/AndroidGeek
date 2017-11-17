package com.xiaomai.geek.common.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Created by XiaoMai on 2017/11/17.
 */

public class ShareUtils {

    public static void share(Context context, String title, String content) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(title)) {
            stringBuilder.append(title);
        }
        if (!TextUtils.isEmpty(content)) {
            stringBuilder.append("\n").append(content);
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        context.startActivity(Intent.createChooser(intent, "分享到"));
    }

    public static void share(Context context, String content) {
        share(context, content, "");
    }
}
