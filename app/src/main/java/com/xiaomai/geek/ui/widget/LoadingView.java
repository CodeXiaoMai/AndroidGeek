package com.xiaomai.geek.ui.widget;

import android.app.AlertDialog;
import android.content.Context;

import com.xiaomai.geek.R;
import com.xiaomai.geek.data.pref.ThemePref;

import dmax.dialog.SpotsDialog;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public class LoadingView {

    private AlertDialog mLoadingDialog;

    public LoadingView(Context context, String message) {
        if (ThemePref.Companion.getTheme(context) == R.style.AppTheme) {
            mLoadingDialog = new SpotsDialog(context, message);
        } else {
            mLoadingDialog = new SpotsDialog(context, R.style.NightSpotsDialog);
        }
    }

    public void show() {
        mLoadingDialog.show();
    }

    public void dismiss() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }
}
