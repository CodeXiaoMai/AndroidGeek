
package com.xiaomai.geek.api;

import com.xiaomai.geek.api.bean.NewsDetailInfo;
import com.xiaomai.geek.api.bean.NewsInfo;
import com.xiaomai.geek.api.bean.SpecialInfo;
import com.xiaomai.geek.common.constant.Const;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by XiaoMai on 2017/3/27 11:05.
 */

public interface NewsService {

    /**
     * 获取新闻列表 eg:
     * http://c.m.163.com/nc/article/headline/T1348647909107/60-20.html
     * http://c.m.163.com/nc/article/list/T1348647909107/60-20.html
     * 
     * @param type 新闻类型
     * @param id 新闻ID
     * @param startPage 起始页码
     */
    @Headers(Const.CACHE_CONTROL_NETWORK)
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewsInfo>>> getNewsList(@Path("type") String type,
            @Path("id") String id, @Path("startPage") int startPage);

    /**
     * 获取专题 eg: http://c.3g.163.com/nc/special/S1451880983492.html
     * 
     * @param specialId 专题ID
     */
    @Headers(Const.CACHE_CONTROL_NETWORK)
    @GET("nc/special/{specialId}.html")
    Observable<Map<String, SpecialInfo>> getSpecial(@Path("specialId") String specialId);

    /**
     * 获取新闻详情
     * eg: http://c.3g.163.com/nc/article/BV56RVG600011229/full.html
     *
     * @param newsId 专题ID
     * @return
     */
    @Headers(Const.AVOID_HTTP403_FORBIDDEN)
    @GET("nc/article/{newsId}/full.html")
    Observable<Map<String, NewsDetailInfo>> getNewsDetail(@Path("newsId") String newsId);
}
