package com.xiaomai.geek.data.net;

import android.app.Application;

import com.alibaba.fastjson.JSON;
import com.xiaomai.geek.data.api.VideoApi;
import com.xiaomai.geek.data.module.Video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XiaoMai on 2017/6/23.
 */
public class VideoDataSource implements VideoApi {

    @Inject
    Application context;

    private final VideoService mVideoService;

    @Inject
    public VideoDataSource(VideoService videoService) {
        mVideoService = videoService;
    }

    @Override
    public Observable<List<Video>> getVideosFromAssets() {
        return Observable.create(new Observable.OnSubscribe<List<Video>>() {
            @Override
            public void call(Subscriber<? super List<Video>> subscriber) {
                InputStreamReader inputStreamReader = null;
                BufferedReader bufferedReader = null;
                try {
                    inputStreamReader = new InputStreamReader(context.getAssets().open("videos"));
                    bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    List<Video> videos = JSON.parseArray(stringBuilder.toString(), Video.class);
                    subscriber.onNext(videos);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    if (inputStreamReader != null) {
                        try {
                            inputStreamReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public Observable<List<Video>> getVideos() {
        return null;
    }
}