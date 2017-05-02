package com.xiaomai.geek.presenter.github;

import com.xiaomai.geek.common.wrapper.AppLog;
import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.RepoDetail;
import com.xiaomai.geek.data.net.response.BaseResponseObserver;
import com.xiaomai.geek.presenter.BaseRxPresenter;
import com.xiaomai.geek.view.IRepoView;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/5/2.
 */

public class RepoDetailPresenter extends BaseRxPresenter<IRepoView> {

    private final GitHubApi gitHubApi;

    @Inject
    public RepoDetailPresenter(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public void loadRepoDetails(String owner, String repoName) {
        mCompositeSubscription.add(
                gitHubApi.getRepoDetail(owner, repoName)
                .subscribeOn(Schedulers.io())
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
                .subscribe(new BaseResponseObserver<RepoDetail>() {
                    @Override
                    public void onSuccess(RepoDetail repoDetail) {
                        getMvpView().showContent(repoDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }

    public void starRepo(String owner, String repo) {
        mCompositeSubscription.add(
                gitHubApi.starRepo(owner, repo)
                .subscribeOn(Schedulers.io())
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
                .subscribe(new BaseResponseObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean)
                            getMvpView().starSuccess();
                        else
                            getMvpView().starFailed();
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.e(e);
                        getMvpView().showError(e);
                        getMvpView().starFailed();
                    }
                })
        );
    }

    public void unStarRepo(String owner, String repo) {
        mCompositeSubscription.add(
                gitHubApi.unStarRepo(owner, repo)
                .subscribeOn(Schedulers.io())
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
                .subscribe(new BaseResponseObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean)
                            getMvpView().unStarSuccess();
                        else
                            getMvpView().unStarFailed();
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.e(e);
                        getMvpView().showError(e);
                        getMvpView().unStarFailed();
                    }
                })
        );
    }
}
