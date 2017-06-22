package com.gkzxhn.gank_kotlin.ui.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.databinding.FragmentGirlBinding

/**
 * Created by 方 on 2017/6/21.
 *
 */
class GirlFragment : BaseFragment<FragmentGirlBinding>(){
    override fun initView() {
//        tv_android.text = "hahaha"
    }

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentGirlBinding{
        return DataBindingUtil.inflate<FragmentGirlBinding>(inflater, R.layout.fragment_girl, container, false)
    }

    companion object {
        val GIRL = "GIRL"
        fun newInstance(): GirlFragment {
            val fragment = GirlFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}