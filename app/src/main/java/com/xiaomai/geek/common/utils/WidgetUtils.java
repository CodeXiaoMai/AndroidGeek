package com.xiaomai.geek.common.utils;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;

/**
 * Created by xiaomai on 2017/10/31.
 */

public class WidgetUtils {

    public static ColorStateList createColorStateList(
            @ColorInt int normal, @ColorInt int pressed, @ColorInt int focused, @ColorInt int unable) {
        int[] colors = {pressed, pressed, focused, normal, focused, unable, normal};
        // 颜色数组对应的状态
        int[][] states = new int[7][];
        int i = 0;
        // enable 并且 按下时的状态
        states[i++] = new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed};
        // enable 并且获得焦点时的状态
        // 虽然和上面的状态用的是同一个颜色，但是不能这样写
        // states[i++] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused, android.R.attr.state_pressed};
        // 这样代表三种状态同时满足，才会响应
        states[i++] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[i++] = new int[]{android.R.attr.state_enabled, android.R.attr.state_selected};
        states[i++] = new int[]{android.R.attr.state_enabled};
        states[i++] = new int[]{android.R.attr.state_focused};
        states[i++] = new int[]{android.R.attr.state_window_focused};
        states[i] = new int[]{};

        return new ColorStateList(states, colors);
    }
}