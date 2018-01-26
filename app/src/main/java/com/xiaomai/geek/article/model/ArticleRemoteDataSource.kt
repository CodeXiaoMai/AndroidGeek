package com.xiaomai.geek.article.model

import android.util.Log
import com.alibaba.fastjson.JSON
import com.xiaomai.geek.common.utils.StringUtil
import com.xiaomai.geek.network.GeekApiService
import com.xiaomai.geek.network.GeekRetrofit
import rx.Observable

/**
 * Created by wangce on 2018/1/26.
 */
class ArticleRemoteDataSource : ArticleDataSource {

    override fun getArticles(): Observable<List<ArticleResponse>> {
        return GeekRetrofit.getInstance().create(GeekApiService::class.java)
                .getArticles()
                .map {
//                    Log.e("TAG", StringUtil.base64Decode(it.content))
                    it?.content?.let {
                        JSON.parseArray(StringUtil.base64Decode(it), ArticleResponse::class.java)
                    }
                }
    }
}