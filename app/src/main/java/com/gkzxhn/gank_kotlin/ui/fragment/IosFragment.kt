package com.gkzxhn.gank_kotlin.ui.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.databinding.FragmentIosBinding

/**
 * Created by 方 on 2017/6/21.
 *
 */
class IosFragment : BaseFragment<FragmentIosBinding>(){
    override fun initView() {
//        tv_android.text = "hahaha"
    }

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentIosBinding{
        return DataBindingUtil.inflate<FragmentIosBinding>(inflater, R.layout.fragment_ios, container, false)
    }

    companion object {
        val IOS = "IOS"
        fun newInstance(): IosFragment {
            val fragment = IosFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}