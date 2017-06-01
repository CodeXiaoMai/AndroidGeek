package com.xiaomai.geek.data.api;

import com.xiaomai.geek.data.module.Chapter;

import java.util.List;

import rx.Observable;

/**
 * Created by XiaoMai on 2017/5/17.
 */

public interface ArticleApi {

    Observable<List<Chapter>> getChaptersFromAssets();

    Observable<List<Chapter>> getChapters();
}
