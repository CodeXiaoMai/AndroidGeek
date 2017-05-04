package com.xiaomai.geek.presenter.github;

import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.Repo;
import com.xiaomai.geek.data.net.response.BaseResponseObserver;
import com.xiaomai.geek.presenter.BaseRxPresenter;
import com.xiaomai.geek.view.ISearchView;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/5/4.
 */

public class SearchPresenter extends BaseRxPresenter<ISearchView<ArrayList<Repo>>> {

    private final GitHubApi gitHubApi;

    @Inject
    public SearchPresenter(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public void searchRepo(String key, String language) {
        mCompositeSubscription.add(
                gitHubApi.searchRepo(key, language)
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
                                getMvpView().showSearchResult(repos);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().error(e);
                            }
                        })
        );
    }
}
