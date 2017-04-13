
package com.xiaomai.geek.common.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.xiaomai.geek.R;
import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.service.NotificationService;

/**
 * Created by XiaoMai on 2017/4/8 13:36.
 */

public class NotificationUtils {

    private static int sID = 0;

    public static final int TYPE_USER_NAME = 0;

    public static final int TYPE_PASSWORD = 1;

    public static void showNotification(Context context, Password password,
            @NotificationType int type) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(NotificationService.ACTION_NOTIFICATION);
        if (type == TYPE_USER_NAME) {
            intent.putExtra(NotificationService.EXTRA_CONTENT, password.getUserName());
        } else if (type == TYPE_PASSWORD) {
            intent.putExtra(NotificationService.EXTRA_CONTENT, password.getPassword());
        }
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentText("点击复制到剪切板")
                .setAutoCancel(true)
                .setContentIntent(
                        PendingIntent.getService(context, sID, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle(password.getPlatform() + (type == TYPE_USER_NAME ? "--账号" : "--密码"))
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(sID++, notification);
    }

    @IntDef({
            TYPE_USER_NAME, TYPE_PASSWORD
    })
    @interface NotificationType {

    }
}
