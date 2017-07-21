package com.gkzxhn.gank_kotlin.di.component

import com.gkzxhn.gank_kotlin.mvp.contract.GankContract
import com.gkzxhn.gank_kotlin.mvp.contract.KaiyanContract
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

@Subcomponent(modules = arrayOf(KaiyanModule::class))
interface KaiyanComponent {

    fun inject(fragment: KaiyanVideoFragment)

//    fun inject(fragment: GirlFragment)
}

@Module
class KaiyanModule(private val mView: KaiyanContract.View) {
    @Provides fun getView() = mView
}

