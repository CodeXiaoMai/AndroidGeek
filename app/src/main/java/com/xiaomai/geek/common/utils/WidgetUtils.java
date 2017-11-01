package com.xiaomai.geek.common.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by xiaomai on 2017/10/31.
 */

public class WidgetUtils {

    public static ColorStateList createColorStateList(@NonNull Context context, @ColorRes int normal,
                                                      @ColorRes int selected, @ColorRes int unable) {
        int[] colors = {
                ContextCompat.getColor(context, selected),
                ContextCompat.getColor(context, selected),
                ContextCompat.getColor(context, normal),
                ContextCompat.getColor(context, unable),
                ContextCompat.getColor(context, normal)
        };
        // 颜色数组对应的状态
        int[][] states = new int[5][];
        int i = 0;
        // enable 并且 按下时的状态
        states[i++] = new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed};
        // enable 并且获得焦点时的状态
        // 虽然和上面的状态用的是同一个颜色，但是不能这样写
        // states[i++] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused, android.R.attr.state_pressed};
        // 这样代表三种状态同时满足，才会响应
//        states[i++] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[i++] = new int[]{android.R.attr.state_enabled, android.R.attr.state_selected};
        states[i++] = new int[]{android.R.attr.state_enabled};
//        states[i++] = new int[]{android.R.attr.state_focused};
        states[i++] = new int[]{android.R.attr.state_window_focused};
        states[i] = new int[]{};

        return new ColorStateList(states, colors);
    }

    public static Drawable createStateListDrawable(@NonNull Context context, @NonNull Drawable drawable,
                                                   @ColorRes int normal, @ColorRes int selected) {
        int[] colors = new int[]{ContextCompat.getColor(context, selected), ContextCompat.getColor(context, normal)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(states[0], drawable);//注意顺序
        stateListDrawable.addState(states[1], drawable);
        Drawable.ConstantState state = stateListDrawable.getConstantState();
        drawable = DrawableCompat.wrap(state == null ? stateListDrawable : state.newDrawable()).mutate();
        DrawableCompat.setTintList(drawable, colorList);

        return drawable;
    }
}