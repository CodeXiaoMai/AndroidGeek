package com.xiaomai.geek.data;

import android.support.annotation.NonNull;

import com.xiaomai.geek.data.module.Password;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by xiaomai on 2017/10/25.
 */

public interface IPasswordDataSource {

    /**
     * 获取所有的密码列表
     *
     * @return
     */
    Flowable<List<Password>> getPasswords();

    /**
     * 根据关键词查找密码
     *
     * @param keyword
     * @return
     */
    Observable<List<Password>> getPasswords(@NonNull String keyword);

    /**
     * 保存密码
     *
     * @param password
     */
    Observable<Boolean> savePassword(@NonNull Password password);

    /**
     * 更新密码
     *
     * @param password
     * @return
     */
    Observable<Boolean> updatePassword(@NonNull Password password);

    /**
     * 删除指定 ID 的密码
     *
     * @param passwordId
     */
    Observable<Boolean> deletePassword(@NonNull String passwordId);
}
