package com.xiaomai.geek.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by XiaoMai on 2017/11/24.
 */

public class LabelLayout extends ViewGroup {
    public LabelLayout(Context context) {
        super(context);
    }

    public LabelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        final int childCount = getChildCount();

        int heightResult = 0;
        int widthResult;
        int childWidth;
        int childHeight;
        MarginLayoutParams params;

        int currentLineWidth = 0;
        int currentLineHeight = 0;

        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

            params = (MarginLayoutParams) child.getLayoutParams();

            if (heightMode == MeasureSpec.EXACTLY) {
                heightResult = heightSize;
            } else {
                // 如果是包裹内容
                childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
                if (currentLineWidth + childWidth > getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) {
                    currentLineWidth = childWidth;
                    heightResult += currentLineHeight;
                    currentLineHeight = childHeight;
                    /*if (i == childCount - 1) {
                        heightResult += currentLineHeight;
                    }*/
                } else {
                    currentLineWidth += childWidth;
                    currentLineHeight = Math.max(currentLineHeight, childHeight);
                }
                if (i == childCount - 1) {
                    heightResult += currentLineHeight;
                    // 加上 padding
                    heightResult += getPaddingTop() + getPaddingBottom();
                }
            }
        }

        // 宽度无论如何都是 widthSize
        widthResult = widthSize;

        setMeasuredDimension(widthResult, heightResult);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        left += getPaddingLeft();
        top += getPaddingTop();

        final int childCount = getChildCount();

        int childWidth;
        int childHeight;
        int currentLineWidth = left;
        int currentLineHeight = 0;
        int currentHeight = top;

        int currentLineTop;
        int currentLineLeft;
        MarginLayoutParams params;

        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            params = (MarginLayoutParams) child.getLayoutParams();

            childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            if (currentLineWidth + childWidth > getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) {
                currentHeight += currentLineHeight;
                currentLineLeft = left + params.leftMargin;
                currentLineTop = currentHeight + params.topMargin;
                child.layout(currentLineLeft, currentLineTop, currentLineLeft + child.getMeasuredWidth(), currentLineTop + child.getMeasuredHeight());
                currentLineWidth = left + childWidth;
                currentLineHeight = childHeight;
            } else {
                currentLineLeft = currentLineWidth + params.leftMargin;
                currentLineTop = currentHeight + params.topMargin;
                child.layout(currentLineLeft, currentLineTop, currentLineLeft + child.getMeasuredWidth(), currentLineTop + child.getMeasuredHeight());
                currentLineWidth += childWidth;
                currentLineHeight = Math.max(currentLineHeight, childHeight);
            }
        }
    }
}
