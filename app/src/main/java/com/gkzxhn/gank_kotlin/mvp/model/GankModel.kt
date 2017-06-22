package com.gkzxhn.gank_kotlin.mvp.model

import com.gkzxhn.gank_kotlin.api.GankApi
import com.gkzxhn.gank_kotlin.mvp.contract.GankContract
import com.gkzxhn.gank_kotlin.ui.fragment.AndroidFragment
import com.gkzxhn.gank_kotlin.ui.fragment.GirlFragment
import com.gkzxhn.gank_kotlin.ui.fragment.IosFragment
import com.wingsofts.gankclient.bean.FuckGoods
import com.wingsofts.gankclient.bean.JsonResult
import rx.Observable
import javax.inject.Inject

/**
 * Created by 方 on 2017/6/21.
 */
class GankModel
@Inject constructor(private val api: GankApi): GankContract.Model {

    override fun getData(page: Int, type: String): Observable<JsonResult<List<FuckGoods>>> {
        when (type) {
            AndroidFragment.ANDROID -> {
                return api.getAndroidData(page)
            }
            IosFragment.IOS -> {
                return api.getiOSData(page)
            }
            GirlFragment.GIRL -> {
                return api.getGirlData(page)
            }
            else -> {
                return api.getAndroidData(page)
            }
        }
    }
}