package com.gkzxhn.gank_kotlin.mvp.model

import com.gkzxhn.gank_kotlin.api.ApiService
import com.gkzxhn.gank_kotlin.bean.info.HomeBean
import com.gkzxhn.gank_kotlin.mvp.contract.KaiyanContract
import com.gkzxhn.gank_kotlin.ui.fragment.KaiyanVideoFragment
import rx.Observable
import javax.inject.Inject

/**
 * Created by 方 on 2017/6/21.
 */
class KaiyanModel
@Inject constructor(private val api: ApiService): KaiyanContract.Model {

    override fun getData(date: String, num: String, type: String): Observable<HomeBean> {
        when (type) {
            KaiyanVideoFragment.ISFIRST -> {
                return api.getHomeData()
            }
            KaiyanVideoFragment.ISMORE -> {
                return api.getHomeMoreData(date, "2")
            }
            else -> {
                return api.getHomeData()
            }
        }
    }
}