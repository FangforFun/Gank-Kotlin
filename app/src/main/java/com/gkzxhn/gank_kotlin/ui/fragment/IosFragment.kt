package com.gkzxhn.gank_kotlin.ui.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.databinding.FragmentIosBinding
import com.gkzxhn.gank_kotlin.di.component.GankModule
import com.gkzxhn.gank_kotlin.mvp.contract.GankContract
import com.gkzxhn.gank_kotlin.mvp.presenter.GankPresenter
import com.gkzxhn.gank_kotlin.ui.adapter.MyRvAdapter
import com.wingsofts.gankclient.bean.FuckGoods
import com.wingsofts.gankclient.getMainComponent
import kotlinx.android.synthetic.main.fragment_ios.*
import javax.inject.Inject

/**
 * Created by 方 on 2017/6/21.
 *
 */
class IosFragment : GankContract.View, BaseFragment<FragmentIosBinding>(){

    @Inject lateinit var mPresenter: GankPresenter

    private var page = 1
    var mList = arrayListOf<FuckGoods>()
    private lateinit var myRvAdapter : MyRvAdapter

    override fun initView() {
        srl_shipin.setOnRefreshListener {
            page = 1
            mPresenter.getData(10, page, SHIPIN)
        }
        srl_shipin.isRefreshing = true
        rv_shipin.layoutManager = LinearLayoutManager(context)
        myRvAdapter = MyRvAdapter(context, mList)
        rv_shipin.adapter = myRvAdapter
        mPresenter.getData(10, page, SHIPIN)
        rv_shipin.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView?.canScrollVertically(1)!!) {
                    Log.i(tag, "rv_shipin,  cantScrollVertically")
                    mPresenter.getData(10, ++page, IosFragment.SHIPIN)
                }
            }
        })
        fab2top1.setOnClickListener {
            view ->
            //回到顶部
            rv_shipin.smoothScrollToPosition(0)
            ViewCompat.animate(fab2top1).scaleX(0.0f).scaleY(0.0f).alpha(0.0f)
                    .setInterpolator(FastOutSlowInInterpolator()).withLayer()
                    .start()
        }
    }


    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentIosBinding{
        context.getMainComponent().plus(GankModule(this)).inject(this)
        return DataBindingUtil.inflate<FragmentIosBinding>(inflater, R.layout.fragment_ios, container, false)
    }

    override fun setData(results: List<FuckGoods>, type: String) {
        when (type) {
            SHIPIN -> {
                if (srl_shipin.isRefreshing) {
                    mList.clear()
                    srl_shipin.isRefreshing = false
                }
                mList.addAll(results)
                myRvAdapter.notifyDataSetChanged()
            }
            else -> {
            }
        }
    }

    companion object {
        val IOS = "IOS"
        val SHIPIN = "shipin"
        fun newInstance(): IosFragment {
            val fragment = IosFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}