package com.xiaomai.geek.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.xiaomai.geek.data.pref.ThemePref;
import com.xiaomai.mvp.MvpView;

/**
 * Created by XiaoMai on 2017/3/29 18:40.
 */

public abstract class BaseActivity extends AppCompatActivity implements MvpView {

    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        mContext = this;
    }

    private void initTheme() {
        setTheme(ThemePref.Companion.getTheme(this));
    }

    protected void initToolBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        initToolBar(actionBar, true, title);
    }

    protected void initToolBar(ActionBar actionBar, boolean homeAsUpEnabled, String title) {
        if (actionBar != null) {
        }
    }

    private boolean mIsFinish;
    private int mStartX;
    private int mLastX = 0;
    private int mLastY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!allowSlideSlip()) {
            return super.dispatchTouchEvent(event);
        }
        int action = event.getAction();
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 如果当前标记位不为退出，并且手指从屏幕左侧开始滑动，则初步认为要退出当前页面，并记录下开始滑动的位置
                if (!mIsFinish) {
                    mIsFinish = event.getRawX() <= 20;
                    if (mIsFinish) {
                        mStartX = (int) event.getRawX();
                        mLastX = x;
                        mLastY = y;
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsFinish) {
                    // 横向滑动的距离
                    int deltaX = x - mLastX;
                    // 如果滑动的距离 > 0，并且横向滑动的距离大于纵向滑动的距离，则认为还在继续触发退出，否则取消触发侧滑退出
                    if (deltaX > 0 && deltaX > Math.abs(y - mLastY)) {
                        // 如果当前手指在屏幕的横向位置减去初始位置大于某个固定值，就成功触发退出
                        if (event.getRawX() - mStartX >= 300) {
                            finish();
                        }
                        mLastX = x;
                        mLastY = y;
                        return true;
                    } else {
                        mIsFinish = false;
                        mStartX = 0;
                        mLastX = 0;
                        mLastY = 0;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsFinish = false;
                mStartX = 0;
                mLastX = 0;
                mLastY = 0;
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(event);
    }



    /**
     * 是否允许侧滑退出，默认是允许，如果不允许重写该方法，返回false即可
     * @return
     */
    protected boolean allowSlideSlip() {
        return true;
    }
}
