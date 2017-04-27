package com.xiaomai.geek.data.net;

import android.text.TextUtils;

import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.data.net.response.SearchResultResp;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public class GitHubDataSource implements GitHubApi {
    private static final String SORT_BY_STARTS = "stars";
    private static final String ORDER_BY_DESC = "desc";
    private static final int PAGE_SIZE = 30;

    GitHubService mGitHubService;

    @Inject
    public GitHubDataSource(GitHubService gitHubService) {
        mGitHubService = gitHubService;
    }

    @Override
    public Observable<ArrayList<Repo>> getTrendingRepos(@LanguageType String language) {
        String queryParams = "";
        if (!TextUtils.isEmpty(language)) {
            if (TextUtils.equals(language, GitHubApi.LANG_ANDROID)) {
                queryParams = "android+language:java";
            } else {
                queryParams = "language:" + language;
            }
        }
        return mGitHubService.searchRepo(queryParams, SORT_BY_STARTS, ORDER_BY_DESC, 1, PAGE_SIZE)
                .map(new Func1<SearchResultResp, ArrayList<Repo>>() {
            @Override
            public ArrayList<Repo> call(SearchResultResp searchResultResp) {
                return searchResultResp.getItems();
            }
        });
    }
}
