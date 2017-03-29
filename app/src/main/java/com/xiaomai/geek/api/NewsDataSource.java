
package com.xiaomai.geek.api;

import android.text.TextUtils;

import com.xiaomai.geek.api.bean.NewsDetailInfo;
import com.xiaomai.geek.api.bean.NewsInfo;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/3/27 11:01.
 */

public class NewsDataSource implements INewsApi {

    private static final String HEAD_LINE_NEWS = "T1348647909107";

    // 递增页码
    private static final int INCREASE_PAGE = 20;

    NewsService mNewsService;

    @Inject
    public NewsDataSource(NewsService mNewsService) {
        this.mNewsService = mNewsService;
    }

    /**
     * 获取新闻列表
     * 
     * @param newsId
     * @param page
     * @return
     */
    @Override
    public Observable<NewsInfo> getNewsList(final String newsId, int page) {
        String type;
        if (TextUtils.equals(HEAD_LINE_NEWS, newsId))
            type = "headline";
        else
            type = "list";
        return mNewsService.getNewsList(type, newsId, page * INCREASE_PAGE)
                .flatMap(new Func1<Map<String, List<NewsInfo>>, Observable<NewsInfo>>() {
                    @Override
                    public Observable<NewsInfo> call(Map<String, List<NewsInfo>> newsListMap) {
                        return Observable.from(newsListMap.get(newsId));
                    }
                });
    }

    @Override
    public Observable<NewsDetailInfo> getNewsDetail(String newsId) {
        return null;
    }
}
