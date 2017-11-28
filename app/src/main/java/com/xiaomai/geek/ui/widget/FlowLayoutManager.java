package com.xiaomai.geek.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by XiaoMai on 2017/11/27.
 */

public class FlowLayoutManager extends RecyclerView.LayoutManager {

    // 当前垂直方向滚动的距离
    private int mVerticalOffset;
    private int mFirstVisiblePosition;
    private int mLastVisiblePosition;

    private SparseArray<Rect> mItemRects;
    private int mTopOffset;

    public FlowLayoutManager() {
        mItemRects = new SparseArray<>();
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        // 如果没有子View，就返回
        if (getItemCount() <= 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }

        // 如果当前没有任何子View可见，并且正在准备动画，也返回
        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }

        // 因为onLayoutChildren 方法在初始化时，会被执行两次
        detachAndScrapAttachedViews(recycler);

        mVerticalOffset = 0;
        mFirstVisiblePosition = 0;
        mLastVisiblePosition = getItemCount() - 1;

        fill(recycler, state);
    }

    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        fill(recycler, state, 0);
    }

    /**
     * @param recycler
     * @param state
     * @param dy       dy 大于 0 表示手指向上滑动，dy 小于 0 表示手指向下滑动
     * @return
     */
    private int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
        mTopOffset = getPaddingTop();

        // 当前可见的子View 个数 > 0，说明是在滚动,将不再显示的子 View 回收
        if (getChildCount() > 0) {
            for (int i = getChildCount() - 1; i >= 0; i--) {
                final View child = getChildAt(i);
                if (dy > 0) {
                    if (getDecoratedBottom(child) - dy < mTopOffset) {
                        removeAndRecycleView(child, recycler);
                        mFirstVisiblePosition++;
                    }
                } else if (dy < 0) {
                    if (getDecoratedTop(child) - dy > getHeight() - getPaddingBottom()) {
                        removeAndRecycleView(child, recycler);
                        mLastVisiblePosition--;
                    }
                }
            }
        }

        int leftOffset = getPaddingLeft();
        // 每一行的最大高度
        int lineMaxHeight = 0;

        // 手指向上滑或者首次添加时，顺序添加子View
        if (dy >= 0) {
            int minPosition = mFirstVisiblePosition;
            mLastVisiblePosition = getItemCount() - 1;

            // 当前可见的子View 个数 > 0，说明是在滚动,需要向下填充子 View
            if (getChildCount() > 0) {
                final View lastView = getChildAt(getChildCount() - 1);
                // 将要添加的子View的起始位置是当前可见的子View 的下一个
                minPosition = getPosition(lastView) + 1;
                mTopOffset = getDecoratedTop(lastView);
                leftOffset = getDecoratedRight(lastView);

                lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasurementVertical(lastView));
            }

            for (int i = minPosition; i <= mLastVisiblePosition; i++) {
                //向recycler要一个childItemView,我们不管它是从scrap里取，
                // 还是从RecyclerViewPool里取，亦或是onCreateViewHolder里拿。
                final View child = recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child, 0, 0);
                // 当前行还可以放下一个子View
                if (leftOffset + getDecoratedMeasurementHorizontal(child) <= getHorizontalSpace()) {
                    layoutDecoratedWithMargins(child,
                            leftOffset,
                            mTopOffset,
                            leftOffset + getDecoratedMeasurementHorizontal(child),
                            mTopOffset + getDecoratedMeasurementVertical(child));

                    Rect rect = new Rect(leftOffset,
                            mTopOffset + mVerticalOffset,
                            leftOffset + getDecoratedMeasurementHorizontal(child),
                            mTopOffset + mVerticalOffset + getDecoratedMeasurementVertical(child));

                    mItemRects.put(i, rect);

                    leftOffset += getDecoratedMeasurementHorizontal(child);
                    lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasurementVertical(child));
                } else {
                    // 换行
                    leftOffset = getPaddingLeft();
                    mTopOffset += lineMaxHeight;
                    lineMaxHeight = 0;

                    if (mTopOffset - dy > getHeight() - getPaddingBottom()) {
                        removeAndRecycleView(child, recycler);
                        mLastVisiblePosition = i - 1;
                    } else {
                        layoutDecoratedWithMargins(child,
                                leftOffset,
                                mTopOffset,
                                leftOffset + getDecoratedMeasurementHorizontal(child),
                                mTopOffset + getDecoratedMeasurementVertical(child));

                        Rect rect = new Rect(leftOffset,
                                mTopOffset + mVerticalOffset,
                                leftOffset + getDecoratedMeasurementHorizontal(child),
                                mTopOffset + mVerticalOffset + getDecoratedMeasurementVertical(child));

                        mItemRects.put(i, rect);

                        leftOffset += getDecoratedMeasurementHorizontal(child);
                        lineMaxHeight = getDecoratedMeasurementVertical(child);
                    }
                }
            }

            // 如果最后一个子View已经显示出来了，滑动时需要处理
            /*final View lastChild = getChildAt(getChildCount() - 1);
            if (getPosition(lastChild) == getItemCount() - 1) {
                final int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    dy = -gap;
                }
            }*/
        } else {
            /**
             * 手指向上滑动，逆序填充子View
             */
            int maxPosition = getItemCount() - 1;
            mFirstVisiblePosition = 0;
            if (getChildCount() > 0) {
                final View firstView = getChildAt(0);
                maxPosition = getPosition(firstView) - 1;
            }

            for (int i = maxPosition; i >= mFirstVisiblePosition; i--) {
                final Rect rect = mItemRects.get(i);
                if (rect.bottom - dy < getPaddingTop()) {
                    mFirstVisiblePosition = i + 1;
                    break;
                } else {
                    final View child = recycler.getViewForPosition(i);
                    addView(child, 0);
                    measureChildWithMargins(child, 0, 0);
                    layoutDecoratedWithMargins(child, rect.left,
                            rect.top - mVerticalOffset,
                            rect.right,
                            rect.bottom - mVerticalOffset);
                }
            }
        }
        Log.d("TAG", "count= [" + getChildCount() + "]" + ",[recycler.getScrapList().size():" + recycler.getScrapList().size() + ", dy:" + dy + ",  mVerticalOffset" + mVerticalOffset + ", ");
        return dy;
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dy == 0 || getChildCount() <= 0) {
            return 0;
        }

        int realOffset = dy;
        // 如果要从当前位置滚动到一个偏移为负的位置
        if (mVerticalOffset + realOffset < 0) {
            realOffset = -mVerticalOffset;
        } else if (realOffset > 0) {
            final View lastChild = getChildAt(getChildCount() - 1);
            // 如果当前所有的子View可以显示下，就不能滑动了
            if (getChildCount() == getItemCount() &&
                    (getDecoratedBottom(lastChild) <= getHeight() - getPaddingBottom())) {
                realOffset = 0;
            } else if (getPosition(lastChild) == getItemCount() - 1) {
                // 如果最后一个子View已经显示了，
                final int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    realOffset = -gap;
                } else if (gap == 0) {
                    realOffset = 0;
                } else {
                    realOffset = Math.min(realOffset, -gap);
                }
            }
        }

        if (realOffset == 0) {
            return 0;
        }

        realOffset = fill(recycler, state, realOffset);

        mVerticalOffset += realOffset;

        offsetChildrenVertical(-realOffset);

        return realOffset;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    private int getDecoratedMeasurementHorizontal(View view) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin;
    }

    private int getDecoratedMeasurementVertical(View view) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin;
    }
}