package com.gkzxhn.gank_kotlin.ui.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.databinding.FragmentAndroidBinding
import com.gkzxhn.gank_kotlin.di.component.GankModule
import com.gkzxhn.gank_kotlin.mvp.contract.GankContract
import com.gkzxhn.gank_kotlin.mvp.presenter.GankPresenter
import com.wingsofts.gankclient.bean.FuckGoods
import com.wingsofts.gankclient.getMainComponent
import kotlinx.android.synthetic.main.fragment_android.*
import javax.inject.Inject

/**
 * Created by 方 on 2017/6/21.
 *
 */
class AndroidFragment : GankContract.View, BaseFragment<FragmentAndroidBinding>(){

    @Inject lateinit var mPresenter: GankPresenter

    override fun initView() {
        mPresenter.getData(1, GirlFragment.GIRL)
    }

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentAndroidBinding{
        val binding = DataBindingUtil.inflate<FragmentAndroidBinding>(inflater, R.layout.fragment_android, container, false)
        context.getMainComponent().plus(GankModule(this)).inject(this)
        return binding
    }

    companion object {
        val ANDROID = "ANDROID"
        fun newInstance(): AndroidFragment {
            val fragment = AndroidFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun setData(results: List<FuckGoods>, type: String) {
        when (type) {
            GirlFragment.GIRL -> {
                Glide.with(iv.context).load(results[0].url)
                        .crossFade()
                        .into(iv)

            }
            else -> {
            }
        }
    }
}
