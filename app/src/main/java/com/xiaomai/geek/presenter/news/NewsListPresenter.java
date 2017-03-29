
package com.xiaomai.geek.presenter.news;

import com.xiaomai.geek.api.INewsApi;
import com.xiaomai.geek.api.bean.NewsInfo;
import com.xiaomai.geek.presenter.BasePresenter;
import com.xiaomai.geek.view.NewsListView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by XiaoMai on 2017/3/28 13:45.
 */

public class NewsListPresenter extends BasePresenter<NewsListView> {

    private final INewsApi newsApi;

    @Inject
    public NewsListPresenter(INewsApi newsApi) {
        this.newsApi = newsApi;
    }

    public void getNewList(final String newsId, int page, final boolean isRefresh) {
        mCompositeSubscription.add(newsApi.getNewsList(newsId, page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (!isRefresh)
                            getMvpView().showLoading();
                    }
                }).doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        getMvpView().disMissLoading();
                    }
                })
//                .filter(new Func1<NewsInfo, Boolean>() {
//                    @Override
//                    public Boolean call(NewsInfo newsInfo) {
//                        if (NewsUtils.isAbNews(newsInfo))
//                            getMvpView().showAd(newsInfo);
//                        return !NewsUtils.isAbNews(newsInfo);
//                    }
//                })
                .toList()
//                .compose(getMvpView().<List<NewsInfo>>bindToLife())
                .subscribe(new Subscriber<List<NewsInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<NewsInfo> newsInfo) {
                        getMvpView().showContent(newsInfo);
                    }
                }));
    }
}
