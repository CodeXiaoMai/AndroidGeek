
package com.xiaomai.geek.api;

import com.xiaomai.geek.api.bean.NewsDetailInfo;
import com.xiaomai.geek.api.bean.NewsInfo;

import rx.Observable;

/**
 * Created by XiaoMai on 2017/3/24 18:44.
 */

public interface INewsApi {

    /**
     * 获取新闻列表
     * 
     * @param newsId
     * @param page
     * @return
     */
    Observable<NewsInfo> getNewsList(String newsId, int page);

    Observable<NewsDetailInfo> getNewsDetail(String newsId);
}
