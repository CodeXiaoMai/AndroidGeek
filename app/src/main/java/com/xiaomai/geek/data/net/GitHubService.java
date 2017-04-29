package com.xiaomai.geek.data.net;

import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.data.net.response.SearchResultResp;

import java.util.ArrayList;

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

    @Headers("Cache-Control: public, max-age=600")
    @GET("user/repos")
    Observable<ArrayList<Repo>> getMyRepos(@Query("sort") String sort, @Query("type") String type);

    @Headers("Cache-Control: public, max-age=600")
    @GET("users/{name}/repos")
    Observable<ArrayList<Repo>> getUserRepos(@Path("name") String user, @Query("sort") String sort);

    @Headers("Cache-Control: public, max-age=600")
    @GET("user/starred")
    Observable<ArrayList<Repo>> getMyStarredRepos(@Query("sort") String sort);

    @Headers("Cache-Control: public, max-age=600")
    @GET("users/{name}/starred")
    Observable<ArrayList<Repo>> getUserStarredRepos(@Path("name") String user,
                                                    @Query("sort") String sort);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("users/{user}/following")
    Observable<ArrayList<User>> getUserFollowing(@Path("user") String user);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("user/following")
    Observable<ArrayList<User>> getMyFollowing();

    @Headers("Cache-Control: public, max-age=3600")
    @GET("users/{user}/followers")
    Observable<ArrayList<User>> getUserFollowers(@Path("user") String user);

    @Headers("Cache-Control: public, max-age=3600")
    @GET("user/followers")
    Observable<ArrayList<User>> getMyFollowers();
}
