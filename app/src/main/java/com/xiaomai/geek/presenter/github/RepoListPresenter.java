package com.xiaomai.geek.presenter.github;

import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.data.net.response.BaseResponseObserver;
import com.xiaomai.geek.presenter.BaseRxPresenter;
import com.xiaomai.mvp.lce.ILceView;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/4/28.
 */

public class RepoListPresenter extends BaseRxPresenter<ILceView<ArrayList<Repo>>> {

    private final GitHubApi gitHubApi;

    @Inject
    public RepoListPresenter(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public void loadRepos(String userName, boolean isSelf, @GitHubApi.RepoType int repoType) {
        Observable<ArrayList<Repo>> observable = null;
        switch (repoType) {
            case GitHubApi.OWNER_REPO:
                if (isSelf)
                    observable = gitHubApi.getMyRepos();
                else
                    observable = gitHubApi.getUserRepos(userName);
                break;
            case GitHubApi.STARRED_REPO:
                if (isSelf)
                    observable = gitHubApi.getMyStarredRepos();
                else
                    observable = gitHubApi.getUserStarredRepos(userName);
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
                        getMvpView().showLoading();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        getMvpView().dismissLoading();
                    }
                })
                .subscribe(new BaseResponseObserver<ArrayList<Repo>>() {
                    @Override
                    public void onSuccess(ArrayList<Repo> repos) {
                        if (repos == null || repos.size() == 0)
                            getMvpView().showEmpty();
                        else
                            getMvpView().showContent(repos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }
}
