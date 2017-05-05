package com.xiaomai.geek.presenter.github;

import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.data.net.response.BaseResponseObserver;
import com.xiaomai.geek.presenter.BaseRxPresenter;
import com.xiaomai.geek.view.ILoadMoreView;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/4/24.
 */

public class TrendingPresenter extends BaseRxPresenter<ILoadMoreView<ArrayList<Repo>>> {

    private final GitHubApi gitHubApi;

    @Inject
    public TrendingPresenter(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public void loadTrendingRepos(@GitHubApi.LanguageType String languageType, int page) {
        loadTrendingRepos(languageType, page, false);
    }

    public void loadTrendingRepos(@GitHubApi.LanguageType String languageType, int page, final boolean loadMore) {
        mCompositeSubscription.add(gitHubApi.getTrendingRepos(languageType, page)
                .subscribeOn(Schedulers.io())
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
                            if (repos != null && repos.size() > 0)
                                getMvpView().showContent(repos);
                            else
                                getMvpView().showEmpty();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                }));
    }
}
