package com.xiaomai.geek.contract.password;

import android.support.annotation.Nullable;

import com.xiaomai.geek.data.module.Password;
import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.mvp.lce.ILceView;

import java.util.List;

/**
 * Created by xiaomai on 2017/10/26.
 */

public interface AddEditPasswordContract {

    interface View extends ILceView<List<Password>>{

    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void savePassword(@Nullable String platform, @Nullable String userName, @Nullable String pwd,
                                   @Nullable String note);
    }
}
