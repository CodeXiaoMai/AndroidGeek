package com.xiaomai.geek.presenter.video;

import com.xiaomai.geek.BuildConfig;
import com.xiaomai.geek.data.api.VideoApi;
import com.xiaomai.geek.data.module.Video;
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
 * Created by XiaoMai on 2017/6/23.
 */

public class VideoPresenter extends BaseRxPresenter<ILceView<List<Video>>> {

    private final VideoApi videoApi;

    @Inject
    public VideoPresenter(VideoApi videoApi) {
        this.videoApi = videoApi;
    }

    public void getVideos() {
        Observable<List<Video>> observable = BuildConfig.DEBUG ?
                videoApi.getVideosFromAssets() : videoApi.getVideos();
        observable.subscribeOn(Schedulers.io())
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseResponseObserver<List<Video>>() {
                    @Override
                    public void onSuccess(List<Video> videos) {
                        if (videos != null && !videos.isEmpty()) {
                            getMvpView().showContent(videos);
                        } else {
                            getMvpView().showEmpty();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                });
    }
}
