package com.xiaomai.geek.common.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaoMai on 2017/12/1.
 */

public class ActivityStacks {

    private static List<Activity> activities;

    public static void add(Activity activity) {
        if (activities == null) {
            activities = new ArrayList<>();
        }
        activities.add(activity);
    }

    public static void clear() {
        if (activities == null || activities.isEmpty()) {
            return;
        }
        for (Activity activity : activities) {
            activity.finish();
        }
        activities.clear();
    }

    public static void remove(Activity activity) {
        if (activities == null || activities.isEmpty()) {
            return;
        }

        for (Activity act : activities) {
            if (act == activity) {
                activities.remove(act);
                break;
            }
        }
    }
}
