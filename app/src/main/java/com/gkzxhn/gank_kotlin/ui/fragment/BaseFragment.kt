package com.gkzxhn.gank_kotlin.ui.fragment

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *
 * Created by 方 on 2017/6/21.
 */

abstract class BaseFragment<B: ViewDataBinding> : Fragment() {

    lateinit var mBinding: B
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = createDataBinding(inflater, container, savedInstanceState)
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        initView()
    }

    /**
     * 初始化控件
     */
    abstract fun initView()

    /**
     * 进行资源文件数据绑定
     */
    abstract fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): B
}
