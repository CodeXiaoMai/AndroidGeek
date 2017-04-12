package com.xiaomai.geek.view;

import com.xiaomai.mvp.MvpView;

import java.util.List;

/**
 * Created by XiaoMai on 2017/4/12 13:57.
 */

public interface IFileExploreView extends MvpView {

    void showSelectStorageView(List<String> storages);

    void showFiles(String path);
}
