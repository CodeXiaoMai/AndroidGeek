package com.xiaomai.geek.presenter.github

import android.content.Context
import com.alibaba.fastjson.JSON
import com.xiaomai.geek.GeekApplication
import com.xiaomai.geek.data.api.GitHubApi
import com.xiaomai.geek.data.net.response.BaseResponseObserver
import com.xiaomai.geek.data.pref.AccountPref
import com.xiaomai.geek.presenter.BaseRxPresenter
import com.xiaomai.geek.view.IReportView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by XiaoMai on 2017/6/6.
 */
class ReportPresenter @Inject constructor(private val gitHubApi: GitHubApi) : BaseRxPresenter<IReportView>() {

    fun report(context: Context) {
        mCompositeSubscription.add(
                gitHubApi.createIssue("this is title", "this is body", arrayOf("bug"), arrayOf(
                        JSON.toJSONString(AccountPref.getLoginUser(GeekApplication.get(context)))
                ))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { ->
                            mvpView.showLoading()
                        }
                        .doOnTerminate { ->
                            mvpView.dismissLoading()
                        }
                        .subscribe(object : BaseResponseObserver<Boolean>() {
                            override fun onError(e: Throwable?) {
                                mvpView.error(e)
                            }

                            override fun onSuccess(t: Boolean?) {
                                mvpView.reportResult(t!!)
                            }

                        })
        )
    }
}