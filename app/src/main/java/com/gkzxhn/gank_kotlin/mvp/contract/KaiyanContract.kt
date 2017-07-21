package com.gkzxhn.gank_kotlin.mvp.contract

import com.gkzxhn.gank_kotlin.bean.info.HomeBean
import rx.Observable

/**
 *
 * Created by 方 on 2017/6/20.
 */

interface KaiyanContract {
    interface View {
        fun  setData(results: HomeBean, type: String)

    }

    interface Model {

        fun getData(date: String, num: String, type: String): Observable<HomeBean>
    }

    interface Presenter {

        fun getData(date: String, num: String, type: String)
    }
}