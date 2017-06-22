package com.gkzxhn.gank_kotlin.mvp.presenter

import android.util.Log
import com.gkzxhn.gank_kotlin.mvp.contract.GankContract
import com.gkzxhn.gank_kotlin.mvp.model.GankModel
import com.wingsofts.gankclient.mvp.presenter.BasePresenter
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 *
 * Created by 方 on 2017/6/21.
 */
class GankPresenter
@Inject constructor(private val mModel: GankModel, private val mView: GankContract.View)
    :GankContract.Presenter, BasePresenter(){
    override fun getData(page: Int, type: String) {
        addSubscription(mModel.getData(page, type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    res ->
                    if (!res.error) {
                        mView.setData(res.results, type)
                    }

                }, { e -> Log.e("wing", "error android Presenter" + e.message) }))
    }


}