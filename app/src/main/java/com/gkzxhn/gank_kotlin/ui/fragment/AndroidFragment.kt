package com.gkzxhn.gank_kotlin.ui.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
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
import com.wingsofts.gankclient.bean.FuckGoods
import com.wingsofts.gankclient.getMainComponent
import kotlinx.android.synthetic.main.fragment_android.*
import java.util.*
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
                val imageViewList = arrayListOf<ImageView>()
                Collections.shuffle(results)
                for (i in 0..4) {
                    val imageView = ImageView(context)
                    imageView.setScaleType(ImageView.ScaleType.CENTER)
                    imageView.isClickable = true
                    val url = results.get(i).url
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
