package com.gkzxhn.gank_kotlin.ui.adapter

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import java.util.*

/**
 * Created by 方 on 2017/6/29.
 */

class MyPhotoPagerAdapter(private var results: ArrayList<ImageView>) : PagerAdapter() {

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