
package com.xiaomai.geek.view;

import android.support.annotation.UiThread;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.xiaomai.geek.widget.EmptyView;

/**
 * Created by XiaoMai on 2017/3/24 15:21.
 */

public interface IBaseView<M>{

    @UiThread
    void showLoading();

    @UiThread
    void disMissLoading();

    /**
     * 完成刷新, 新增控制刷新
     */
    @UiThread
    void finishRefresh();

    @UiThread
    void showContent(M data);

    /**
     * 显示错误
     * 
     * @param onRetryListener 点击监听
     */
    @UiThread
    void showError(EmptyView.OnRetryListener onRetryListener);

    /**
     * 绑定生命周期
     * 
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindToLife();

}
