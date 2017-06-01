package com.xiaomai.geek.presenter.article;

import com.xiaomai.geek.BuildConfig;
import com.xiaomai.geek.data.api.ArticleApi;
import com.xiaomai.geek.data.module.Chapter;
import com.xiaomai.geek.data.net.response.BaseResponseObserver;
import com.xiaomai.geek.presenter.BaseRxPresenter;
import com.xiaomai.mvp.lce.ILceView;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/5/17.
 */

public class ChapterPresenter extends BaseRxPresenter<ILceView<List<Chapter>>> {

    private final ArticleApi articleApi;

    @Inject
    public ChapterPresenter(ArticleApi articleApi) {
        this.articleApi = articleApi;
    }

    public void getChapters() {
        Observable<List<Chapter>> observable = BuildConfig.DEBUG ? articleApi.getChaptersFromAssets() : articleApi.getChapters();
        mCompositeSubscription.add(
                observable
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
                        .subscribe(new BaseResponseObserver<List<Chapter>>() {
                            @Override
                            public void onSuccess(List<Chapter> chapters) {
                                getMvpView().showContent(chapters);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().showError(e);
                            }
                        }));

    }
}
