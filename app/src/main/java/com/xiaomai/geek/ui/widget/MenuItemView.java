package com.xiaomai.geek.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
import com.xiaomai.geek.common.utils.WidgetUtils;

/**
 * Created by xiaomai on 2017/10/31.
 */

public class MenuItemView extends FrameLayout {

    private ImageView mIconView;
    private TextView mTitleView;

    private String mTitle;
    private Drawable mIconDrawable;

    public MenuItemView(Context context) {
        this(context, null);
    }

    public MenuItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuItemView);
        Drawable drawable = typedArray.getDrawable(R.styleable.MenuItemView_iconDrawable);
        mTitle = typedArray.getString(R.styleable.MenuItemView_menuTitle);
        typedArray.recycle();

        View rootView = LayoutInflater.from(context).inflate(R.layout.menu_item_layout, this, true);

        mIconView = (ImageView) rootView.findViewById(R.id.icon);
        mTitleView = (TextView) rootView.findViewById(R.id.title);

        if (drawable != null) {
            setIconDrawable(drawable);
        }
        if (!TextUtils.isEmpty(mTitle)) {
            mTitleView.setText(mTitle);
        }

        mTitleView.setTextColor(WidgetUtils.createColorStateList(
                context,
                android.R.color.black,
                R.color.blue,
                android.R.color.darker_gray));
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

        mIconView.setImageDrawable(WidgetUtils.createStateListDrawable(getContext(), mIconDrawable,
                android.R.color.darker_gray, R.color.blue));
    }

    @Override
    public void setSelected(boolean selected) {
        if (isSelected() == selected) {
            return;
        }
        super.setSelected(selected);

        if (selected) {
            mTitleView.setSelected(true);
            mIconView.setSelected(true);
        } else {
            mTitleView.setSelected(false);
            mIconView.setSelected(false);
        }
    }
}
