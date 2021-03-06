package com.gkzxhn.gank_kotlin.di.component

import com.gkzxhn.gank_kotlin.mvp.contract.GankContract
import com.gkzxhn.gank_kotlin.ui.fragment.AndroidFragment
import com.gkzxhn.gank_kotlin.ui.fragment.IosFragment
import com.gkzxhn.gank_kotlin.ui.fragment.KaiyanVideoFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 *
 * Created by 方 on 2017/6/20.
 */

@Subcomponent(modules = arrayOf(GankModule::class, KaiyanModule::class))
interface GankComponent {
    fun inject(fragment: AndroidFragment)
    fun inject(fragment: IosFragment)

//    fun inject(fragment: GirlFragment)
}

@Module
class GankModule(private val mView: GankContract.View) {
    @Provides fun getView() = mView
}
