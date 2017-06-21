package com.gkzxhn.gank_kotlin.ui.activity

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.gkzxhn.gank_kotlin.R

abstract class BaseActivity<B: ViewDataBinding> : AppCompatActivity() {

    lateinit var mBinding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = createDataBinding(savedInstanceState)

        initView()
    }

    /**
     * 初始化控件
     */
    abstract fun initView()

    /**
     * 进行资源文件数据绑定
     */
    abstract fun createDataBinding(savedInstanceState: Bundle?): B

    fun setupToolbar(toolbar: Toolbar){
        toolbar.title = ""
        toolbar.setNavigationIcon(R.drawable.icon_back)
        setSupportActionBar(toolbar)

    }
}
