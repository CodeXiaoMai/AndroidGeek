package com.xiaomai.geek.presenter.github;

import com.xiaomai.geek.common.utils.Const;
import com.xiaomai.geek.data.api.GitHubApi;
import com.xiaomai.geek.data.module.User;
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
 * Created by XiaoMai on 2017/4/29.
 */

public class UserListPresenter extends BaseRxPresenter<ILoadMoreView<ArrayList<User>>> {

    private final GitHubApi gitHubApi;

    @Inject
    public UserListPresenter(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public void loadUsers(String userName, boolean isSelf, @GitHubApi.UserType int type) {
        loadUsers(userName, isSelf, type, 1, false);
    }

    public void loadUsers(final String userName, boolean isSelf, @GitHubApi.UserType int type, int page, final boolean loadMore) {
        Observable<ArrayList<User>> observable = null;
        switch (type) {
            case GitHubApi.FOLLOWER:
                if (isSelf)
                    observable = gitHubApi.getMyFollowers(page);
                else
                    observable = gitHubApi.getUserFollowers(userName, page);
                break;
            case GitHubApi.FOLLOWING:
                if (isSelf)
                    observable = gitHubApi.getMyFollowing(page);
                else
                    observable = gitHubApi.getUserFollowing(userName, page);
                break;
        }

        if (observable == null)
            return;

        mCompositeSubscription.add(observable.subscribeOn(Schedulers.io())
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
                .subscribe(new BaseResponseObserver<ArrayList<User>>() {
                    @Override
                    public void onSuccess(ArrayList<User> users) {
                        if (loadMore) {
                            if (users == null || users.size() == 0)
                                getMvpView().loadComplete();
                            else {
                                getMvpView().showMoreResult(users);
                                if (users.size() < Const.PAGE_SIZE)
                                    getMvpView().loadComplete();
                            }
                        } else {
                            if (users == null || users.size() == 0)
                                getMvpView().showEmpty();
                            else
                                getMvpView().showContent(users);
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
