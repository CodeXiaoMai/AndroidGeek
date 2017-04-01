
package com.xiaomai.geek.common.utils;

import android.content.Context;

/**
 * Created by XiaoMai on 2016/12/23 11:15.
 */
public class DisplayUtils {

    /**
     * Dip转换为像素
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        /**
         * 获取屏幕的像素密度
         */
        final float scale = context.getResources().getDisplayMetrics().density;
        if (dpValue > 0) {
            return (int) (dpValue * scale + 0.5f);
        } else {
            return (int) (dpValue * scale - 0.5f);
        }
    }

    /**
     * Px转换为Dip
     * 
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        if (pxValue > 0) {
            return (int) (pxValue / scale + 0.5f);
        } else {
            return (int) (pxValue / scale - 0.5f);
        }
    }

    /**
     * Sp转换为Px
     * 
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        /**
         * 获取屏幕的像素密度
         */
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        if (spValue > 0) {
            return (int) (spValue * scale + 0.5f);
        } else {
            return (int) (spValue * scale - 0.5f);
        }
    }

    /**
     * Px转换为Sp
     * 
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        if (pxValue > 0) {
            return (int) (pxValue / scaledDensity + 0.5f);
        } else {
            return (int) (pxValue / scaledDensity - 0.5f);
        }
    }
}
