package com.xiaomai.geek.data.net;

import android.app.Application;

import com.xiaomai.geek.data.api.AccountApi;
import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.data.net.client.AuthRetrofit;
import com.xiaomai.geek.data.net.request.CreateAuthorization;
import com.xiaomai.geek.data.net.response.AuthorizationResp;
import com.xiaomai.geek.data.pref.AccountPref;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XiaoMai on 2017/4/26.
 */

public class AccountDataSource implements AccountApi {

    public static final String NOTE = "GitHubApp";

    public static final String AUTHOR_NAME = "codexiaomai";

    // client id/secret
    public static final String CLIENT_ID = "a874b7a32f1f69b6730b";

    public static final String CLIENT_SECRET = "3f0b588e46edcdac37db5ce032228c130dbaa56d";

    // scopes
    public static final String[] SCOPES = {
            "user", "repo", "notifications", "gist", "admin:org"
    };

    @Inject
    AuthRetrofit retrofit;

    @Inject
    Application context;

    @Inject
    public AccountDataSource() {
    }

    @Override
    public Observable<User> login(String userName, String password) {
        retrofit.setAuthInfo(userName, password);
        final AccountService accountService = retrofit.get().create(AccountService.class);
        CreateAuthorization createAuthorization = new CreateAuthorization();
        createAuthorization.note = NOTE;
        createAuthorization.client_id = CLIENT_ID;
        createAuthorization.client_secret = CLIENT_SECRET;
        createAuthorization.scopes = SCOPES;
        return accountService.createAuthorization(createAuthorization).flatMap(
                new Func1<AuthorizationResp, Observable<User>>() {
                    @Override
                    public Observable<User> call(AuthorizationResp authorizationResp) {
                        String token = authorizationResp.getToken();
                        AccountPref.saveLoginToken(context, token);
                        return accountService.getUserInfo(token);
                    }
                });
    }
}
