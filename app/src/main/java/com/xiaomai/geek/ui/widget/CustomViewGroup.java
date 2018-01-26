package com.xiaomai.geek.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.security.MessageDigest;

/**
 * Created by wangce on 2018/1/22.
 */

public class CustomViewGroup extends ViewGroup {

    public CustomViewGroup(Context context) {
        super(context);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int lHeight = 0, rHeight = 0, tWidth = 0, bWidth = 0;
        int childHeight, childWidth;
        MarginLayoutParams cParams;

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            if (i == 0 || i == 1) {
                tWidth += childWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if (i == 2 || i == 3) {
                bWidth += childWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if (i == 0 || i == 2) {
                lHeight += childHeight + cParams.topMargin + cParams.bottomMargin;
            }
            if (i == 1 || i == 3) {
                rHeight += childHeight + cParams.topMargin + cParams.bottomMargin;
            }
        }

        int width = Math.max(tWidth, bWidth) + getPaddingLeft() + getPaddingRight();
        int height = Math.max(lHeight, rHeight) + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height
        );

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        int cWidth = 0, cHeight = 0;
        int cLeft = 0, cTop = 0, cRight, cBottom;
        MarginLayoutParams cParams;

        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            switch (i) {
                case 0:
                    cLeft = cParams.leftMargin + getPaddingLeft();
                    cTop = cParams.topMargin + getPaddingTop();
                    break;
                case 1:
                    cLeft = getWidth() - getPaddingRight() - cParams.rightMargin - cWidth;
                    cTop = cParams.topMargin + getPaddingTop();
                    break;
                case 2:
                    cLeft = cParams.leftMargin + getPaddingLeft();
                    cTop = getHeight() - getPaddingBottom() - cParams.bottomMargin - cHeight;
                    break;
                case 3:
                    cLeft = getWidth() - getPaddingRight() - cParams.rightMargin - cWidth;
                    cTop = getHeight() - getPaddingBottom() - cParams.bottomMargin - cHeight;
                    break;
            }

            cRight = cLeft + cWidth;
            cBottom = cTop + cHeight;

            childView.layout(cLeft, cTop, cRight, cBottom);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
