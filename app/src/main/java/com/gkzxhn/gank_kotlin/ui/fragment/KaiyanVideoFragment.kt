package com.gkzxhn.gank_kotlin.ui.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.bean.info.HomeBean
import com.gkzxhn.gank_kotlin.databinding.FragmentKaiyanVideoBinding
import com.gkzxhn.gank_kotlin.di.component.KaiyanModule
import com.gkzxhn.gank_kotlin.mvp.contract.KaiyanContract
import com.gkzxhn.gank_kotlin.mvp.presenter.KaiyanPresenter
import com.gkzxhn.gank_kotlin.ui.adapter.KaiyanAdapter
import com.wingsofts.gankclient.getMainComponent
import kotlinx.android.synthetic.main.fragment_kaiyan_video.*
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by 方 on 2017/6/21.
 *
 */
class KaiyanVideoFragment : KaiyanContract.View,BaseFragment<FragmentKaiyanVideoBinding>(){

    @Inject lateinit var mPresenter: KaiyanPresenter

    private lateinit var kaiyanAdapter: KaiyanAdapter

    private var mList = ArrayList<HomeBean.IssueListBean.ItemListBean>()

    var date: String? = null

    override fun setData(results: HomeBean, type: String) {

        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(results?.nextPageUrl)
        date = m.replaceAll("").subSequence(1, m.replaceAll("").length - 1).toString()

        if (refreshLayout.isRefreshing) {
            refreshLayout.isRefreshing = false
        }

        results.issueList!!
                .flatMap {
                    if (ISFIRST == type) {
                        mList.clear()
                    }
                    it.itemList!! }
                .filter { it.type.equals("video") }
                .forEach { mList.add(it) }

        kaiyanAdapter.notifyDataSetChanged()
    }

    override fun initView() {

        mPresenter.getData("", "", ISFIRST)
        refreshLayout.setOnRefreshListener {
            mPresenter.getData("", "", ISFIRST)
        }
        refreshLayout.isRefreshing = true
        recyclerView.layoutManager = LinearLayoutManager(context)
        kaiyanAdapter = KaiyanAdapter(context, mList)
        recyclerView.adapter = kaiyanAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView?.canScrollVertically(1)!!) {
                    Log.i(tag, "kaiyanvideo,  cantScrollVertically")
                    mPresenter.getData(date!!, "2", ISMORE)
                }
            }
        })
    }

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentKaiyanVideoBinding{
        val binding = DataBindingUtil.inflate<FragmentKaiyanVideoBinding>(inflater, R.layout.fragment_kaiyan_video, container, false)
        context.getMainComponent().plus(KaiyanModule(this)).inject(this)
        return binding
    }

    companion object {
        val ISFIRST = "isFirst"
        val ISMORE = "isMore"
        fun newInstance(): KaiyanVideoFragment {
            val fragment = KaiyanVideoFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}