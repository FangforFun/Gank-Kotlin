package com.gkzxhn.gank_kotlin.di.component

import com.gkzxhn.gank_kotlin.api.App
import com.gkzxhn.gank_kotlin.di.module.ApiModule
import dagger.Component

/**
 * Created by 方 on 2017/6/20.
 */

@Component(modules = arrayOf(ApiModule::class))
interface ApiComponent {

    fun inject(app: App)

    fun plus(module: GankModule):GankComponent
    fun plus(module: KaiyanModule):KaiyanComponent
//    fun plus(module: RandomModule):RandomComponent
}