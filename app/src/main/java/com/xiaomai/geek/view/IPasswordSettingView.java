
package com.xiaomai.geek.view;

import com.xiaomai.mvp.MvpView;

/**
 * Created by XiaoMai on 2017/4/1 18:17.
 */

public interface IPasswordSettingView extends MvpView {

    /**
     * @param count 删除的总条数
     */
    void onDeleteAllPasswords(int count);

    void onBackupComplete(int count);
}
