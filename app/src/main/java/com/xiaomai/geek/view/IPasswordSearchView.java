package com.xiaomai.geek.view;

import com.xiaomai.geek.data.module.Password;
import com.xiaomai.mvp.lce.ILceView;

import java.util.List;

/**
 * Created by XiaoMai on 2017/4/4 18:08.
 */

public interface IPasswordSearchView extends ILceView<List<Password>>{

    void onSearchEmpty();

}
