
package com.xiaomai.geek.wedget;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaomai.geek.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XiaoMai on 2017/3/24 15:36.
 */

public class EmptyView extends RelativeLayout {

    public static final int STATUS_HIDE = 0;

    public static final int STATUS_LOADING = 1;

    public static final int STATUS_ERROR = 2;

    public static final int STATUS_EMPTY_DATA = 3;

    private int mCurrentStatus = STATUS_LOADING;

    @BindView(R.id.tv_error)
    TextView tvError;

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    @BindView(R.id.layout_empty)
    RelativeLayout layoutEmpty;

    private OnRetryListener onRetryListener;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.layout_empty_loading_eror, this);
        ButterKnife.bind(this);
        switchView();
    }

    /**
     * 隐藏
     */
    public void hide() {
        mCurrentStatus = STATUS_HIDE;
    }

    public void setStatus(@Status int status) {
        mCurrentStatus = status;
        switchView();
    }

    /**
     * 获取当前状态
     * 
     * @return
     */
    public int getStatus() {
        return mCurrentStatus;
    }

    private void switchView() {
        switch (mCurrentStatus) {
            case STATUS_HIDE:
                setVisibility(GONE);
                break;
            case STATUS_LOADING:
                layoutEmpty.setVisibility(GONE);
                break;
            case STATUS_ERROR:
                tvError.setVisibility(VISIBLE);
                tvEmpty.setVisibility(GONE);
                break;
            case STATUS_EMPTY_DATA:
                tvEmpty.setVisibility(VISIBLE);
                tvError.setVisibility(GONE);
                break;
        }
    }

    public interface OnRetryListener {
        void onRetry();
    }

    @OnClick(R.id.layout_empty)
    public void onClick() {
        if (onRetryListener != null) {
            onRetryListener.onRetry();
        }
    }

    public void setOnRetryListener(OnRetryListener listener) {
        onRetryListener = listener;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            STATUS_LOADING, STATUS_HIDE, STATUS_ERROR, STATUS_EMPTY_DATA
    })
    public @interface Status {
    }
}
