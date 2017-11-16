package com.xiaomai.geek.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomai.geek.R;

/**
 * Created by XiaoMai on 2017/11/6.
 */

public class TitleView extends FrameLayout {

    private TextView mTitleView;
    private ImageView mBackView;
    private ImageView mMenuView;
    private ImageView mMoreView;

    private String mTitle;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View rootView = LayoutInflater.from(context).inflate(R.layout.title_layout, this, true);
        mTitleView = (TextView) rootView.findViewById(R.id.tv_title);
        mTitleView.setSelected(true);

        mBackView = (ImageView) rootView.findViewById(R.id.iv_back);
        mMenuView = (ImageView) rootView.findViewById(R.id.iv_menu);
        mMoreView = (ImageView) rootView.findViewById(R.id.iv_more);

        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        mTitle = typedArray.getString(R.styleable.TitleView_title);
        typedArray.recycle();

        if (!TextUtils.isEmpty(mTitle)) {
            mTitleView.setText(mTitle);
        }
    }

    public void setTitle(@NonNull String title) {
        mTitleView.setText(title);
    }

    public void setOnBackClickListener(View.OnClickListener listener) {
        if (listener == null) {
            mBackView.setVisibility(View.INVISIBLE);
        } else {
            mBackView.setVisibility(View.VISIBLE);
            mBackView.setOnClickListener(listener);
        }
    }

    public void setOnMenuClickListener(View.OnClickListener listener) {
        if (listener == null) {
            mMenuView.setVisibility(View.GONE);
        } else {
            mBackView.setVisibility(View.GONE);
            mMenuView.setVisibility(View.VISIBLE);
            mMenuView.setOnClickListener(listener);
        }
    }

    public void setOnMoreClickListener(View.OnClickListener listener) {
        if (listener == null) {
            mMoreView.setVisibility(View.INVISIBLE);
        } else {
            mMoreView.setVisibility(View.VISIBLE);
            mMoreView.setOnClickListener(listener);
        }
    }
}
