package com.xiaomai.geek.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xiaomai.geek.R
import com.xiaomai.geek.article.model.Article
import com.xiaomai.geek.article.model.ArticleRemoteDataSource
import com.xiaomai.geek.article.model.ArticleRepository
import com.xiaomai.geek.article.model.ArticleResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscriber
import rx.schedulers.Schedulers

/**
 * Created by wangce on 2018/1/26.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            ArticleRepository(ArticleRemoteDataSource()).getArticles()
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : Subscriber<List<ArticleResponse>>() {
                        override fun onNext(t: List<ArticleResponse>?) {
                        }

                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable?) {
                            e?.printStackTrace()
                        }
                    })
        }

    }
}