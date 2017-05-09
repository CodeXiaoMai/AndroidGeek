package com.xiaomai.geek.ui.base;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.xiaomai.geek.common.utils.Const;

/**
 * Created by XiaoMai on 2017/5/9.
 */

public abstract class EndlessStaggeredGridOnScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotalItemCount = 0;
    private int[] firstVisibleItemPositions;
    // item的总数 = 可见的 + 不可见的
    private int totalItemCount;
    // 屏幕内可见的item的个数
    private int visibleItemCount;

    private boolean mIsLoading;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (mStaggeredGridLayoutManager == null) {
            mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
        }

        totalItemCount = mStaggeredGridLayoutManager.getItemCount();
        /** 如果item总数小于每页加载的数目，那么肯定没有更多数据了 **/
        if (totalItemCount < Const.PAGE_SIZE) {
            return;
        }

        visibleItemCount = recyclerView.getChildCount();
        firstVisibleItemPositions = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItemPositions);

        if (mIsLoading) {
            if (totalItemCount > previousTotalItemCount) {
                mIsLoading = false;
                previousTotalItemCount = totalItemCount;
            }
        }
        // 如果当前不在加载中,就去判断是否需要加载，如果需要的话就去加载，并把当前状态改为加载中
        if (!mIsLoading) {
            if ((totalItemCount - visibleItemCount) <= firstVisibleItemPositions[0]) {
                loadMore();
                mIsLoading = true;
            }
        }
    }

    public abstract void loadMore();
}
