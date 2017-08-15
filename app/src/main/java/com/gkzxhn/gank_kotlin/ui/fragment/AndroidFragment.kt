package com.gkzxhn.gank_kotlin.ui.fragment

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.bean.info.BitmapTemp
import com.gkzxhn.gank_kotlin.databinding.FragmentAndroidBinding
import com.gkzxhn.gank_kotlin.di.component.GankModule
import com.gkzxhn.gank_kotlin.mvp.contract.GankContract
import com.gkzxhn.gank_kotlin.mvp.presenter.GankPresenter
import com.gkzxhn.gank_kotlin.ui.adapter.MyPhotoPagerAdapter
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
    val TAG = javaClass.simpleName

    @Inject lateinit var mPresenter: GankPresenter
    private val INTERPOLATOR = FastOutSlowInInterpolator()
    private var isEnd = true

    var page: Int = 1
    private lateinit var myRvAdapter : MyRvAdapter

    override fun initView() {
        mPresenter.getData(5, Random().nextInt(102), GirlFragment.GIRL)
        mPresenter.getData(10, page, AndroidFragment.ANDROID)
        srl_android.setOnRefreshListener {
            page = 1
            mPresenter.getData(10, page, AndroidFragment.ANDROID)
        }
        setSearchView()
        fab.setOnClickListener {
            view ->
            BitmapTemp.bitmaps.clear()
            mPresenter.getData(5, Random().nextInt(102), GirlFragment.GIRL)
            context.toast("看看手气~")
        }
        rv_android.layoutManager = LinearLayoutManager(context)
        myRvAdapter = MyRvAdapter(context, mList)
        rv_android.adapter = myRvAdapter
        rv_android.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView?.layoutManager is LinearLayoutManager) {
                    val position = recyclerView.layoutManager.getPosition(recyclerView.getChildAt(0))
                    if (position == 0) {
                        //回到顶部时调用的方法
                        if (fab2top.visibility == View.VISIBLE && !isEnd) {
                            ViewCompat.animate(fab2top).scaleX(0.0f).scaleY(0.0f).alpha(0.0f)
                                    .setInterpolator(INTERPOLATOR)
                                    .withLayer()
                                    .start()
                        }else {
                            isEnd = false
                        }
                    }
                }
                if (!recyclerView?.canScrollVertically(1)!!) {
                    mPresenter.getData(10, ++page, ANDROID)
                }
            }
        }
        )
        fab2top.setOnClickListener {
            view ->
            //回到顶部
            rv_android.smoothScrollToPosition(0)
        }
        appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                //完全展开状态
                search_view.visibility = View.INVISIBLE
            } else if (Math.abs(verticalOffset) == appBarLayout.totalScrollRange) {
                //appBarLayout.getTotalScrollRange() == 100
                //完全折叠
                search_view.visibility = View.VISIBLE
            }
        }
        srl_android.isRefreshing = true
    }

    private fun setSearchView() {
        if (search_view == null) {
            return
        } else {
            //获取到TextView的控件
            val textView = search_view.findViewById(R.id.search_src_text) as TextView
            //设置字体大小为14sp
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)//14sp
            //设置字体颜色
            textView.setTextColor(activity.resources.getColor(R.color.search_txt_color))
            //设置提示文字颜色
            textView.setHintTextColor(activity.resources.getColor(R.color.search_hint_color))
        }
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
                val urls = arrayListOf<String>()
                for (result in results) {
                    urls.add(result.url)
                }
                for (index in results.indices) {
                    val imageView = ImageView(context)
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
                    imageView.isClickable = true
                    val url = results.get(index).url
                    imageView.setOnClickListener { view ->
                        ImageActivity.startActivity(context, imageView, urls, index)
                    }
                    Glide.with(imageView.context)
                            .load(url)
                            .asBitmap()//强制Glide返回一个Bitmap对象
                            /*.listener(object: RequestListener<String, Bitmap> {
                                override fun onResourceReady(resource: Bitmap?, model: String?, target: Target<Bitmap>?,
                                                             isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                    val width = resource!!.getWidth()
                                    val height = resource.getHeight()
                                    Log.d(TAG, "width2 " + width) //400px
                                    Log.d(TAG, "height2 " + height) //400px
                                    Log.d(TAG, "index " + index) //400px
                                    BitmapTemp.bitmaps.put(index, resource)
                                    return false
                                }

                                override fun onException(e: Exception?, model: String?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                    Log.d(TAG, "onException " + e.toString())
                                    return false
                                }
                            })*/
                            .into(object : SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL){
                                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                    val width = resource!!.getWidth()
                                    val height = resource.getHeight()
                                    Log.d(TAG, "width2 " + width) //400px
                                    Log.d(TAG, "height2 " + height) //400px
                                    Log.d(TAG, "index " + index) //400px
                                    imageView.setImageBitmap(resource)
                                    BitmapTemp.bitmaps.put(index, resource)
                                }
                            })
                    imageViewList.add(imageView)
                }
                viewpager.adapter = MyPhotoPagerAdapter(imageViewList)
                indicator.setViewPager(viewpager)
            }
            ANDROID -> {
                if (srl_android.isRefreshing) {
                    mList.clear()
                    srl_android.isRefreshing = false
                }
                mList.addAll(results)
                myRvAdapter.notifyDataSetChanged()
            }
            else -> {
            }
        }
    }
}
