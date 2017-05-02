
package com.xiaomai.geek.view;

import com.xiaomai.geek.data.module.RepoDetail;
import com.xiaomai.mvp.lce.ILceView;

/**
 * Created by XiaoMai on 2017/3/17 18:49.
 */

public interface IRepoView extends ILceView<RepoDetail> {

    /**
     * Star 成功
     */
    void starSuccess();

    /**
     * Star 失败
     */
    void starFailed();

    /**
     * unStar 成功
     */
    void unStarSuccess();

    /**
     * unStart 失败
     */
    void unStarFailed();
}
