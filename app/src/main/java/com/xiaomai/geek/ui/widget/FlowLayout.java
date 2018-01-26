package com.xiaomai.geek.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangce on 2018/1/24.
 */

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int lineWidth = 0, lineHeight = 0;
        int width = 0, height = 0;
        int childWidth = 0, childHeight = 0;
        MarginLayoutParams childLayoutParams = null;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            childLayoutParams = (MarginLayoutParams) child.getLayoutParams();
            childWidth = child.getMeasuredWidth() + childLayoutParams.leftMargin + childLayoutParams.rightMargin;
            childHeight = child.getMeasuredHeight() + childLayoutParams.topMargin + childLayoutParams.bottomMargin;

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
                lineHeight = childHeight;
                lineWidth = childWidth;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == childCount - 1) {
                height += lineHeight;
                width = Math.max(width, lineWidth);
            }
        }

        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height
        );
    }

    private List<List<View>> mAllViews = new ArrayList<>();
    private List<View> mLineViews = new ArrayList<>();
    private List<Integer> mLineHeights = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l += getPaddingLeft();
        t += getPaddingTop();

        mAllViews.clear();
        mLineViews.clear();
        mLineHeights.clear();

        int childWidth = 0, childHeight = 0;
        int lineWidth = l, lineHeight = 0;
        MarginLayoutParams layoutParams = null;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            layoutParams = (MarginLayoutParams) child.getLayoutParams();
            childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

            if (lineWidth + childWidth > getWidth() - getPaddingLeft() - getPaddingRight()) {
                mAllViews.add(mLineViews);
                mLineHeights.add(lineHeight);
                lineWidth = l;
                lineHeight = 0;
                mLineViews = new ArrayList<>();
            }

            mLineViews.add(child);
            lineHeight = Math.max(lineHeight, childHeight);
            lineWidth += childWidth;

            if (i == childCount - 1) {
                mAllViews.add(mLineViews);
                mLineHeights.add(lineHeight);
            }
        }

        int left = l, top = t;
        int childLeft = 0, childRight = 0, childTop = 0, childBottom = 0;
        int lineCount = mAllViews.size();
        for (int i = 0; i < lineCount; i++) {
            mLineViews = mAllViews.get(i);
            lineHeight = mLineHeights.get(i);
            left = l;
            int childs = mLineViews.size();
            for (int j = 0; j < childs; j++) {
                View view = mLineViews.get(j);
                layoutParams = (MarginLayoutParams) view.getLayoutParams();
                childLeft = left + layoutParams.leftMargin;
                childTop = top + layoutParams.topMargin;
                view.layout(childLeft, childTop, childLeft + view.getMeasuredWidth(), childTop + view.getMeasuredHeight());
                left += layoutParams.leftMargin + view.getMeasuredWidth() + layoutParams.rightMargin;
            }
            top += lineHeight;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
