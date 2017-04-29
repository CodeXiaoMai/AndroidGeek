package com.xiaomai.geek.presenter.github;

import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.User;
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
 * Created by XiaoMai on 2017/4/29.
 */

public class UserListPresenter extends BaseRxPresenter<ILceView<ArrayList<User>>> {

    private final GitHubApi gitHubApi;

    @Inject
    public UserListPresenter(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public void loadUsers(String userName, boolean isSelf, @GitHubApi.UserType int type) {
        Observable<ArrayList<User>> observable = null;
        switch (type) {
            case GitHubApi.FOLLOWER:
                if (isSelf)
                    observable = gitHubApi.getMyFollowers();
                else
                    observable = gitHubApi.getUserFollowers(userName);
                break;
            case GitHubApi.FOLLOWING:
                if (isSelf)
                    observable = gitHubApi.getMyFollowing();
                else
                    observable = gitHubApi.getUserFollowing(userName);
                break;
        }

        if (observable == null)
            return;

        mCompositeSubscription.add(observable.subscribeOn(Schedulers.io())
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
                .subscribe(new BaseResponseObserver<ArrayList<User>>() {
                    @Override
                    public void onSuccess(ArrayList<User> users) {
                        getMvpView().showContent(users);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }
}
