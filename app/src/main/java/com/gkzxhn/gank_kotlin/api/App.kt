package com.gkzxhn.gank_kotlin.api

import android.app.Application
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.dao.GreenDaoHelper
import com.gkzxhn.gank_kotlin.di.component.ApiComponent
import com.gkzxhn.gank_kotlin.di.component.DaggerApiComponent
import com.gkzxhn.gank_kotlin.di.module.ApiModule
import com.iflytek.cloud.SpeechUtility
import com.wingsofts.gankclient.di.module.AppModule
import javax.inject.Inject

/**
 * Created by 方 on 2017/6/20.
 */

class App : Application() {

    init {
        instance = this
    }

    @Inject lateinit var apiComponent: ApiComponent

    override fun onCreate() {
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.xunfei_id))

        super.onCreate()

        GreenDaoHelper.initDatabase()

        DaggerApiComponent.builder().apiModule(ApiModule()).appModule(AppModule(this)).build().inject(this)
    }

    companion object {
        lateinit var instance: App
    }
}