package com.xiaomai.geek.event;

import android.support.annotation.IntDef;

import com.xiaomai.geek.data.module.Password;

/**
 * Created by XiaoMai on 2017/11/15.
 */

public class PasswordEvent {

    @PasswordEventType
    private int type;

    private Password password;

    public PasswordEvent(@PasswordEventType int type) {
        this.type = type;
    }

    public PasswordEvent(@PasswordEventType int type, Password password) {
        this.type = type;
        this.password = password;
    }

    public @PasswordEventType int getType() {
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

    public static final int TYPE_ADD = 1;

    public static final int TYPE_UPDATE = 2;

    public static final int TYPE_DELETE = 3;

    public static final int TYPE_CLEAR = 4;

    public static final int TYPE_IMPORT = 5;

    @IntDef({TYPE_ADD, TYPE_UPDATE, TYPE_DELETE, TYPE_CLEAR, TYPE_IMPORT})
    @interface PasswordEventType {
    }
}
