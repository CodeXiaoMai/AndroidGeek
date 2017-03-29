package com.xiaomai.geek.view;

import com.xiaomai.geek.api.bean.NewsInfo;

import java.util.List;

/**
 * Created by XiaoMai on 2017/3/28 13:46.
 */

public interface NewsListView extends IBaseView<List<NewsInfo>> {

    void showAd(List<NewsInfo> ads);
}
