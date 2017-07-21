package com.gkzxhn.gank_kotlin.ui.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.databinding.FragmentShipinBinding
import kotlinx.android.synthetic.main.fragment_shipin.*

/**
 * Created by 方 on 2017/6/21.
 *
 */
class ShipinFragment : BaseFragment<FragmentShipinBinding>(){

    var mTabs = listOf<String>("开眼视频", "干货视频")
    private lateinit var mFragments: ArrayList<Fragment>

    override fun initView() {
        mFragments = arrayListOf()
        mFragments.add(KaiyanVideoFragment.newInstance())
        mFragments.add(IosFragment.newInstance())
        vp_content.adapter = object : FragmentPagerAdapter(fragmentManager){
            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getCount(): Int {
                return mFragments.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return mTabs[position]
            }
        }
        tabs.setupWithViewPager(vp_content)
    }

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentShipinBinding{
        return DataBindingUtil.inflate<FragmentShipinBinding>(inflater, R.layout.fragment_shipin, container, false)
    }

    companion object {
        fun newInstance(): ShipinFragment {
            val fragment = ShipinFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}