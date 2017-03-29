
package com.xiaomai.geek.presenter.news;

import com.xiaomai.geek.api.INewsApi;
import com.xiaomai.geek.api.bean.NewsInfo;
import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.geek.view.NewsView;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/3/27 11:41.
 */

public class NewsPresenter extends BasePresenter<NewsView> {

    private final INewsApi mNewsApi;

    @Inject
    public NewsPresenter(INewsApi mNewsApi) {
        this.mNewsApi = mNewsApi;
    }

    public void getNewList(String newsId, int page) {
        mCompositeSubscription.add(mNewsApi.getNewsList(newsId, page)
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
                        getMvpView().disMissLoading();
                    }
                })
                .subscribe(new Action1<NewsInfo>() {
                    @Override
                    public void call(NewsInfo newsInfo) {
                        getMvpView().showContent(newsInfo);
                    }
                }));
    }
}
