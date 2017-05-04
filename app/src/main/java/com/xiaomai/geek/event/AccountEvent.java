package com.xiaomai.geek.event;

import android.support.annotation.IntDef;

/**
 * Created by XiaoMai on 2017/5/4.
 */

public class AccountEvent {

    public static final int LOGIN = 1;
    public static final int LOGOUT = 2;

    private int type;

    public AccountEvent(@AccountEventType int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @IntDef({
            LOGIN, LOGOUT
    })
    @interface AccountEventType {

    }
}
