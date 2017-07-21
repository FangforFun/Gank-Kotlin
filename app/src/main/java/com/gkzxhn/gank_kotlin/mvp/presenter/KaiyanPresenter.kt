package com.gkzxhn.gank_kotlin.mvp.presenter

import android.util.Log
import com.gkzxhn.gank_kotlin.mvp.contract.KaiyanContract
import com.gkzxhn.gank_kotlin.mvp.model.KaiyanModel
import com.wingsofts.gankclient.mvp.presenter.BasePresenter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 *
 * Created by 方 on 2017/6/21.
 */
class KaiyanPresenter
@Inject constructor(private val mModel: KaiyanModel, private val mView: KaiyanContract.View)
    :KaiyanContract.Presenter, BasePresenter(){
    override fun getData(date: String, num: String, type: String) {
        addSubscription(mModel
                .getData(date, num, type)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    t ->
                    mView.setData(t, type)
                },
                        {
                            e -> Log.e("kaiyan", "KaiyanPresenter " + e.message)
                        }))
    }


}