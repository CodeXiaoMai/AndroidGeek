package com.xiaomai.geek.model.main.model

import android.app.Application
import com.google.gson.Gson
import com.xiaomai.geek.application.GeekApplication
import com.xiaomai.geek.common.utils.AssetUtil
import com.xiaomai.geek.common.utils.StringUtil
import com.xiaomai.geek.common.wrapper.GeeKLog
import com.xiaomai.geek.db.Config
import com.xiaomai.geek.network.GeekApiService
import com.xiaomai.geek.network.GeekRetrofit
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by wangce on 2018/3/5.
 */
class ConfigRepository(private val context: Application) {
    private val FILE_NAME = "config"

    fun getConfigFromAsset(): Single<Config> {
        return Single.create {
            val config = AssetUtil.readFromAsset(context, FILE_NAME, Config::class.java)
            if (config != null) {
                it.onSuccess(config)
            } else {
                it.onError(Throwable())
            }
        }
    }

    fun getConfig(): Single<Config> {
        return GeekRetrofit.getInstance().create(GeekApiService::class.java)
                .getConfig()
                .map { contentResponse ->
                    contentResponse.content?.let {
                        val json = StringUtil.base64Decode(it)
                        GeeKLog.json(json)
                        Gson().fromJson<Config>(json, Config::class.java)
                    }
                }
    }

    fun saveConfig(config: Config): Completable {
        return Completable.fromAction {
            GeekApplication.DAO_SESSION.configDao.apply {
                val loadAll = loadAll()
                if (loadAll.isEmpty()) {
                    insert(config)
                } else {
                    update(config.apply {
                        id = 1
                    })
                }
            }
        }
    }
}