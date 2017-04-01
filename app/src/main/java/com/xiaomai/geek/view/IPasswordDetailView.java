package com.xiaomai.geek.view;

import com.xiaomai.geek.data.module.Password;
import com.xiaomai.mvp.lce.ILceView;

/**
 * Created by XiaoMai on 2017/3/31 14:07.
 */

public interface IPasswordDetailView extends ILceView<Password> {

    void onStarComplete(boolean success);

    void onUnStarComplete(boolean success);

    void onDeleteComplete(boolean success);
}
