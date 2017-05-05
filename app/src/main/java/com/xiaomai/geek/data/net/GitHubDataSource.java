package com.xiaomai.geek.data.net;

import android.text.TextUtils;

import com.xiaomai.geek.common.utils.StringUtil;
import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.data.module.RepoDetail;
import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.data.net.response.Content;
import com.xiaomai.geek.data.net.response.SearchResultResp;

import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func5;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public class GitHubDataSource implements GitHubApi {
    private static final String SORT_BY_STARTS = "stars";
    private static final String SORT_BY_UPDATED = "updated";
    private static final String ORDER_BY_DESC = "desc";
    public static final int PAGE_SIZE = 30;

    GitHubService mGitHubService;

    @Inject
    public GitHubDataSource(GitHubService gitHubService) {
        mGitHubService = gitHubService;
    }

    @Override
    public Observable<ArrayList<Repo>> getTrendingRepos(@LanguageType String language, int page) {
        String queryParams = "";
        if (!TextUtils.isEmpty(language)) {
            if (TextUtils.equals(language, GitHubApi.LANG_ANDROID)) {
                queryParams = "android+language:java";
            } else {
                queryParams = "language:" + language;
            }
        }
        return mGitHubService.searchRepo(queryParams, SORT_BY_STARTS, ORDER_BY_DESC, page, PAGE_SIZE)
                .map(new Func1<SearchResultResp, ArrayList<Repo>>() {
                    @Override
                    public ArrayList<Repo> call(SearchResultResp searchResultResp) {
                        return searchResultResp.getItems();
                    }
                });
    }

    @Override
    public Observable<User> getSingleUser(String name) {
        return mGitHubService.getSingleUser(name);
    }

    @Override
    public Observable<ArrayList<Repo>> getMyRepos() {
        return mGitHubService.getMyRepos(SORT_BY_UPDATED, "all");
    }

    @Override
    public Observable<ArrayList<Repo>> getUserRepos(String userName) {
        return mGitHubService.getUserRepos(userName, SORT_BY_UPDATED);
    }

    @Override
    public Observable<ArrayList<Repo>> getMyStarredRepos() {
        return mGitHubService.getMyStarredRepos(SORT_BY_UPDATED);
    }

    @Override
    public Observable<ArrayList<Repo>> getUserStarredRepos(String userName) {
        return mGitHubService.getUserStarredRepos(userName, SORT_BY_UPDATED);
    }

    @Override
    public Observable<ArrayList<User>> getMyFollowers() {
        return mGitHubService.getMyFollowers();
    }

    @Override
    public Observable<ArrayList<User>> getUserFollowers(String userName) {
        return mGitHubService.getUserFollowers(userName);
    }

    @Override
    public Observable<ArrayList<User>> getMyFollowing() {
        return mGitHubService.getMyFollowing();
    }

    @Override
    public Observable<ArrayList<User>> getUserFollowing(String userName) {
        return mGitHubService.getUserFollowing(userName);
    }

    @Override
    public Observable<RepoDetail> getRepoDetail(String owner, String name) {
        return Observable.zip(mGitHubService.get(owner, name),
                mGitHubService.contributors(owner, name),
                mGitHubService.listForks(owner, name, "newest"),
                mGitHubService.readme(owner, name),
                isStarred(owner, name),
                new Func5<Repo, ArrayList<User>, ArrayList<Repo>, Content, Boolean, RepoDetail>() {
                    @Override
                    public RepoDetail call(Repo repo, ArrayList<User> users, ArrayList<Repo> forks, Content readme, Boolean isStarred) {
                        RepoDetail repoDetail = new RepoDetail();

                        repo.setStarred(isStarred);
                        repoDetail.setBaseRepo(repo);
                        repoDetail.setForks(forks);

                        readme.content = StringUtil.base64Decode(readme.content);
                        repoDetail.setReadme(readme);
                        repoDetail.setContributors(users);
                        return repoDetail;
                    }
                }
        );
    }

    @Override
    public Observable<Boolean> isStarred(String owner, String repoName) {
        return mGitHubService.checkIfRepoIsStarred(owner, repoName)
                .map(new Func1<Response<ResponseBody>, Boolean>() {
                    @Override
                    public Boolean call(Response<ResponseBody> responseBodyResponse) {
                        return responseBodyResponse != null && responseBodyResponse.code() == 204;
                    }
                });
    }

    @Override
    public Observable<Boolean> starRepo(String owner, final String repo) {
        return mGitHubService.starRepo(owner, repo)
                .map(new Func1<Response<ResponseBody>, Boolean>() {
                    @Override
                    public Boolean call(Response<ResponseBody> responseBodyResponse) {
                        return responseBodyResponse != null && responseBodyResponse.code() == 204;
                    }
                });
    }

    @Override
    public Observable<Boolean> unStarRepo(String owner, String repo) {
        return mGitHubService.unStarRepo(owner, repo)
                .map(new Func1<Response<ResponseBody>, Boolean>() {
                    @Override
                    public Boolean call(Response<ResponseBody> responseBodyResponse) {
                        return responseBodyResponse != null && responseBodyResponse.code() == 204;
                    }
                });
    }

    @Override
    public Observable<ArrayList<Repo>> searchRepo(String key, String language, int page) {
        StringBuilder queryParams = new StringBuilder(key);
        if (!TextUtils.isEmpty(language)) {
            queryParams.append("+language:");
            queryParams.append(language);
        }
        return mGitHubService.searchRepo(queryParams.toString(), SORT_BY_STARTS, ORDER_BY_DESC, page, PAGE_SIZE)
                .map(new Func1<SearchResultResp, ArrayList<Repo>>() {
                    @Override
                    public ArrayList<Repo> call(SearchResultResp searchResultResp) {
                        searchResultResp.getTotal_count();
                        return searchResultResp.getItems();
                    }
                });
    }
}
