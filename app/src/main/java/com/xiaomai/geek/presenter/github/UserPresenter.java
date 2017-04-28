package com.xiaomai.geek.presenter.github;

import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.User;
import com.xiaomai.geek.data.net.response.BaseResponseObserver;
import com.xiaomai.geek.presenter.BaseRxPresenter;
import com.xiaomai.mvp.lce.ILceView;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/4/27.
 */

public class UserPresenter extends BaseRxPresenter<ILceView<User>> {

    private final GitHubApi gitHubApi;

    @Inject
    public UserPresenter(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public void getSingleUser(String name) {
        mCompositeSubscription.add(
                gitHubApi.getSingleUser(name)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                getMvpView().showLoading();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                getMvpView().dismissLoading();
                            }
                        })
                        .subscribe(new BaseResponseObserver<User>() {
                            @Override
                            public void onSuccess(User user) {
                                getMvpView().showContent(user);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().showError(e);
                            }
                        })
        );
    }
}
