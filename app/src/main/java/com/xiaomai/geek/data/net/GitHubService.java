package com.xiaomai.geek.data.net;

import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.data.net.response.SearchResultResp;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public interface GitHubService {

    @Headers("Cache-Control: public, max-age=600")
    @GET("search/repositories")
    Observable<SearchResultResp> searchRepo(@Query("q") String key,
                                            @Query("sort") String sort,
                                            @Query("order") String order,
                                            @Query("page") int page,
                                            @Query("per_page") int pageSize);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("users/{user}")
    Observable<User> getSingleUser(@Path("user") String user);
}
