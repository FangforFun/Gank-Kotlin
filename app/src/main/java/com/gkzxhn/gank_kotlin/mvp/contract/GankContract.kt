package com.gkzxhn.gank_kotlin.mvp.contract

/**
 *
 * Created by 方 on 2017/6/20.
 */

interface GankContract {
    interface View {
//        fun  setData(results: List<FuckGoods>)

    }

    interface Model {

//        fun getData(page: Int,type:String): Observable<JsonResult<List<FuckGoods>>>
    }

    interface Presenter {

        fun getData(page: Int, type: String)
    }
}