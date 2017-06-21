package com.gkzxhn.gank_kotlin.di.component

import com.gkzxhn.gank_kotlin.mvp.contract.GankContract
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 *
 * Created by 方 on 2017/6/20.
 */

@Subcomponent(modules = arrayOf(GankModule::class))
interface GankComponent {

}

@Module
class GankModule(private val mView: GankContract.View) {
    @Provides fun getView() = mView
}

