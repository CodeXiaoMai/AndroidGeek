
package com.xiaomai.geek.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by XiaoMai on 2017/3/20 10:34.
 */

public class InputMethodUtils {

    private static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static void toggleSoftInput(Context context) {
        getInputMethodManager(context).toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean showSoftInput(View view) {
        return getInputMethodManager(view.getContext()).showSoftInput(view,
                InputMethodManager.SHOW_FORCED);
    }

    public static boolean showSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            return showSoftInput(view);
        }
        return false;
    }

    public static boolean hideSoftInput(View view) {
        return getInputMethodManager(view.getContext()).hideSoftInputFromWindow(
                view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static boolean hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            return hideSoftInput(view);
        }
        return false;
    }

    public static boolean isActive(Context context) {
        return getInputMethodManager(context).isActive();
    }

}
