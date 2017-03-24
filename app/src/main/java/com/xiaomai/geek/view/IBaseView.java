
package com.xiaomai.geek.view;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.xiaomai.geek.wedget.EmptyView;

/**
 * Created by XiaoMai on 2017/3/24 15:21.
 */

public interface IBaseView {

    void showLoading();

    void disMissLoading();

    /**
     * 完成刷新, 新增控制刷新
     */
    void finishRefresh();

    /**
     * 显示网络错误
     * 
     * @param onRetryListener 点击监听
     */
    void showError(EmptyView.OnRetryListener onRetryListener);

    /**
     * 绑定生命周期
     * 
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindToLife();

}
