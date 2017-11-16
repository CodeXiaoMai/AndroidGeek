package com.xiaomai.geek.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xiaomai.geek.R;

/**
 * Created by XiaoMai on 2017/11/9.
 */

public class ErrorView extends FrameLayout {

    private TextView mTitleView;
    private TextView mDescView;

    public ErrorView(@NonNull Context context) {
        this(context, null);
    }

    public ErrorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View rootView = LayoutInflater.from(context).inflate(R.layout.error_view, this, true);

        mTitleView = (TextView) rootView.findViewById(R.id.error_title);
        mDescView = (TextView) rootView.findViewById(R.id.error_desc);

        setVisibility(GONE);
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    public void setDesc(String desc) {
        mDescView.setText(desc);
    }
}
