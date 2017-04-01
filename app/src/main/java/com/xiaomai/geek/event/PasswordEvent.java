
package com.xiaomai.geek.event;

import android.support.annotation.IntDef;

import com.xiaomai.geek.data.module.Password;

/**
 * Created by XiaoMai on 2017/3/31 17:56.
 */

public class PasswordEvent {

    public static final int TYPE_UPDATE = 1;

    public static final int TYPE_DELETE = 2;

    public static final int TYPE_ADD = 3;

    private int type;

    private Password password;

    public PasswordEvent(@PasswordEventType int type, Password password) {
        this.type = type;
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    @IntDef({
            TYPE_UPDATE, TYPE_DELETE, TYPE_ADD
    })
    @interface PasswordEventType {

    }
}
