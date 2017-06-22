package com.gkzxhn.gank_kotlin.mvp.contract

import com.wingsofts.gankclient.bean.FuckGoods
import com.wingsofts.gankclient.bean.JsonResult
import rx.Observable

/**
 *
 * Created by 方 on 2017/6/20.
 */

interface GankContract {
    interface View {
        fun  setData(results: List<FuckGoods>, type: String)

    }

    interface Model {

        fun getData(page: Int,type:String): Observable<JsonResult<List<FuckGoods>>>
    }

    interface Presenter {

        fun getData(page: Int, type: String)
    }
}