package com.xiaomai.geek.presenter.github;

import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.data.net.response.BaseResponseObserver;
import com.xiaomai.geek.presenter.BaseRxPresenter;
import com.xiaomai.mvp.lce.ILceView;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public class TrendingPresenter extends BaseRxPresenter<ILceView<ArrayList<Repo>>> {

    private final GitHubApi gitHubApi;

    @Inject
    public TrendingPresenter(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public void loadTrendingRepos(@GitHubApi.LanguageType String languageType) {
        mCompositeSubscription.add(gitHubApi.getTrendingRepos(languageType)
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
                .subscribe(new BaseResponseObserver<ArrayList<Repo>>() {
                    @Override
                    public void onSuccess(ArrayList<Repo> repos) {
                        if (repos != null && repos.size() > 0)
                            getMvpView().showContent(repos);
                        else
                            getMvpView().showEmpty();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                }));
    }
}
