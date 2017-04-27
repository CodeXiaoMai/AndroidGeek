package com.xiaomai.geek.data.net;

import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.data.net.request.CreateAuthorization;
import com.xiaomai.geek.data.net.response.AuthorizationResp;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by XiaoMai on 2017/4/26.
 */

public interface AccountService {


    @POST("/authorizations")
    Observable<AuthorizationResp> createAuthorization(
            @Body CreateAuthorization createAuthorization);

    @GET("/user")
    Observable<User> getUserInfo(@Query("access_token") String accessToken);
}
