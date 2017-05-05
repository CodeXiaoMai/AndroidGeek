package com.xiaomai.geek.view;

import com.xiaomai.mvp.lce.ILoadView;

/**
 * Created by XiaoMai on 2017/5/4.
 */

public interface ISearchView<M> extends ILoadView{

    void showSearchResult(M result);

    void showMoreResult(M result);
}
