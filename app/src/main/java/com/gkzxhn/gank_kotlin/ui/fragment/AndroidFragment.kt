package com.gkzxhn.gank_kotlin.ui.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.databinding.FragmentAndroidBinding
import com.gkzxhn.gank_kotlin.di.component.GankModule
import com.gkzxhn.gank_kotlin.mvp.contract.GankContract
import com.gkzxhn.gank_kotlin.mvp.presenter.GankPresenter
import com.gkzxhn.gank_kotlin.ui.adapter.MyRvAdapter
import com.wingsofts.gankclient.bean.FuckGoods
import com.wingsofts.gankclient.getMainComponent
import com.wingsofts.gankclient.toast
import kotlinx.android.synthetic.main.fragment_android.*
import java.util.*
import javax.inject.Inject

/**
 * Created by 方 on 2017/6/21.
 *
 */
class AndroidFragment : GankContract.View, BaseFragment<FragmentAndroidBinding>(){

    @Inject lateinit var mPresenter: GankPresenter

    var page: Int = 1
    private lateinit var myRvAdapter : MyRvAdapter

    override fun initView() {
        mPresenter.getData(5, Random().nextInt(102), GirlFragment.GIRL)
        mPresenter.getData(10, page, AndroidFragment.ANDROID)
        fab.setOnClickListener {
            view ->
            mPresenter.getData(5, Random().nextInt(102), GirlFragment.GIRL)
            context.toast("看看手气~")
        }
        rv_android.layoutManager = LinearLayoutManager(context)
        myRvAdapter = MyRvAdapter(context, mList)
        rv_android.adapter = myRvAdapter
        rv_android.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView?.canScrollVertically(1)!!) {
                    mPresenter.getData(10, ++page, ANDROID)
                }
            }
        }
        )
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

    var mList = arrayListOf<FuckGoods>()

    override fun setData(results: List<FuckGoods>, type: String) {
        when (type) {
            GirlFragment.GIRL -> {
                val imageViewList = arrayListOf<ImageView>()
                for (result in results) {
                    val imageView = ImageView(context)
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
                    imageView.isClickable = true
                    val url = result.url
                    imageView.setOnClickListener { view ->
                        ImageActivity.startActivity(context, imageView, url)
                    }
                    Glide.with(imageView.context)
                            .load(url)
                            .crossFade()
                            .into(imageView)
                    imageViewList.add(imageView)
                }
                viewpager.adapter = MyAdapter(imageViewList)
                indicator.setViewPager(viewpager)
            }
            ANDROID -> {
                mList.addAll(results)
                myRvAdapter.notifyDataSetChanged()
            }
            else -> {
            }
        }
    }
}

class MyAdapter(private var results: ArrayList<ImageView>) : PagerAdapter() {

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount() = results.size

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        container!!.addView(results.get(position))
        return results.get(position)
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        (container as ViewPager).removeView(results.get(position))
    }
}
