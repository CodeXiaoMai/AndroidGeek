package com.xiaomai.geek.data.net;

import android.app.Application;

import com.alibaba.fastjson.JSON;
import com.xiaomai.geek.data.api.ArticleApi;
import com.xiaomai.geek.data.module.Chapter;
import com.xiaomai.geek.data.net.response.ArticleService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by XiaoMai on 2017/5/17.
 */

public class ArticleDataSource implements ArticleApi {

    @Inject
    Application context;

    ArticleService mArticleService;

    @Inject
    public ArticleDataSource(ArticleService articleService) {
        mArticleService = articleService;
    }

    @Override
    public Observable<List<Chapter>> getChaptersFromAssets() {
        return Observable.create(new Observable.OnSubscribe<List<Chapter>>() {
            @Override
            public void call(Subscriber<? super List<Chapter>> subscriber) {
                InputStreamReader inputStreamReader;
                BufferedReader bufferedReader;
                try {
                    inputStreamReader = new InputStreamReader(context.getAssets().open("chapters"));
                    bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    // 使用FastJson module类要实现默认的构造方法，因为Chapter中引用Article 所以 Article 也要实现默认的构造方法
                    List<Chapter> chapters = JSON.parseArray(stringBuilder.toString(), Chapter.class);
                    subscriber.onNext(chapters);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Chapter>> getChapters() {
        return mArticleService.getChapters();
    }
}
