package com.xiaomai.geek.ui.widget;

import android.app.AlertDialog;
import android.content.Context;

import dmax.dialog.SpotsDialog;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public class LoadingView {

    private AlertDialog mLoadingDialog;

    public LoadingView(Context context, String message) {
        mLoadingDialog = new SpotsDialog(context, message);
    }

    public void show() {
        mLoadingDialog.show();
    }

    public void dismiss() {
        mLoadingDialog.dismiss();
    }
}
