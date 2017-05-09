package com.xiaomai.geek.presenter.github;

import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.data.net.response.BaseResponseObserver;
import com.xiaomai.geek.presenter.BaseRxPresenter;
import com.xiaomai.geek.view.ILoadMoreView;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/4/28.
 */

public class RepoListPresenter extends BaseRxPresenter<ILoadMoreView<ArrayList<Repo>>> {

    private final GitHubApi gitHubApi;

    @Inject
    public RepoListPresenter(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public void loadRepos(String userName, boolean isSelf, @GitHubApi.RepoType int repoType) {
        loadRepos(userName, isSelf, repoType, 1, false);
    }

    public void loadRepos(String userName, boolean isSelf, @GitHubApi.RepoType int repoType, int page, final boolean loadMore) {
        AppLog.e(userName + ",page:" + page + "," + loadMore);
        Observable<ArrayList<Repo>> observable = null;
        switch (repoType) {
            case GitHubApi.OWNER_REPO:
                if (isSelf)
                    observable = gitHubApi.getMyRepos(page);
                else
                    observable = gitHubApi.getUserRepos(userName, page);
                break;
            case GitHubApi.STARRED_REPO:
                if (isSelf)
                    observable = gitHubApi.getMyStarredRepos(page);
                else
                    observable = gitHubApi.getUserStarredRepos(userName, page);
                break;
        }
        if (observable == null)
            return;
        mCompositeSubscription.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if (!loadMore)
                                    getMvpView().showLoading();
                            }
                        })
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                if (!loadMore)
                                    getMvpView().dismissLoading();
                            }
                        })
                        .subscribe(new BaseResponseObserver<ArrayList<Repo>>() {
                            @Override
                            public void onSuccess(ArrayList<Repo> repos) {
                                if (loadMore) {
                                    if (repos != null && repos.size() > 0)
                                        getMvpView().showMoreResult(repos);
                                    else
                                        getMvpView().loadComplete();
                                } else {
                                    if (repos == null || repos.size() == 0) {
                                        getMvpView().showEmpty();
                                    } else
                                        getMvpView().showContent(repos);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().showError(e);
                            }
                        })
        );
    }
}
