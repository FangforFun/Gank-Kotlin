package com.gkzxhn.gank_kotlin.api

import com.wingsofts.gankclient.bean.FuckGoods
import com.wingsofts.gankclient.bean.JsonResult
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 *
 * Created by 方 on 2017/6/20.
 */

interface GankApi {

    /**
     * Android所有数据
     */
    @GET("data/Android/10/{page}")
    fun getAndroidData(@Path("page") page:Int): Observable<JsonResult<List<FuckGoods>>>

    /**
     * iOS所有数据
     */
    @GET("data/iOS/10/{page}")
    fun getiOSData(@Path("page") page:Int): Observable<JsonResult<List<FuckGoods>>>

    /**
     * 福利图片所有数据
     */
    @GET("data/福利/{count}/{page}")
    fun getGirlData(@Path("count") count: Int,@Path("page") page:Int): Observable<JsonResult<List<FuckGoods>>>


    /**
     * 手气不错
     */

    @GET("random/data/{type}/1")
    fun getRandom(@Path("type") type:String): Observable<JsonResult<List<FuckGoods>>>
}
