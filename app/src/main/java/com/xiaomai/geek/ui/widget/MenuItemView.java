package com.xiaomai.geek.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomai.geek.R;
import com.xiaomai.geek.common.utils.WidgetUtils;

/**
 * Created by xiaomai on 2017/10/31.
 */

public class MenuItemView extends FrameLayout {

    private Context mContext;
    private ImageView mIconView;
    private TextView mTitleView;

    @NonNull
    private String mTitle;
    @NonNull
    private Drawable mIconDrawable;
    private boolean mSelected;

    public MenuItemView(Context context) {
        this(context, null);
    }

    public MenuItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuItemView);
        mIconDrawable = typedArray.getDrawable(R.styleable.MenuItemView_iconDrawable);
        typedArray.recycle();

        View rootView = LayoutInflater.from(context).inflate(R.layout.menu_item_layout, this, true);
        mIconView = rootView.findViewById(R.id.icon);

        mIconView.setImageDrawable(WidgetUtils.createStateListDrawable(mContext, mIconDrawable,
                android.R.color.darker_gray, R.color.blue));

        mTitleView = rootView.findViewById(R.id.title);

        mTitleView.setTextColor(WidgetUtils.createColorStateList(
                mContext,
                android.R.color.black,
                R.color.blue,
                android.R.color.darker_gray));

        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        setSelected(true);
    }

    public void setTitle(@NonNull String title) {
        mTitle = title;
        mTitleView.setText(mTitle);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setIconDrawable(@NonNull Drawable iconDrawable) {
        mIconDrawable = iconDrawable;
        mIconView.setImageDrawable(mIconDrawable);
    }

    @Override
    public void setSelected(boolean selected) {
        if (isSelected() == selected) {
            return;
        }

        mSelected = selected;
        if (mSelected) {
            mTitleView.setSelected(true);
        }

    }
}
