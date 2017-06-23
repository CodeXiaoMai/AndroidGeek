package com.xiaomai.geek.data.api;

import com.xiaomai.geek.data.module.Video;

import java.util.List;

import rx.Observable;

/**
 * Created by HSEDU on 2017/6/23.
 */

public interface VideoApi {

    Observable<List<Video>> getVideosFromAssets();

    Observable<List<Video>> getVideos();
}
