package com.gkzxhn.gank_kotlin.ui.activity

import android.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.WindowManager
import com.gkzxhn.gank_kotlin.R



abstract class BaseActivity<B: ViewDataBinding> : AppCompatActivity() {

    lateinit var mBinding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = createDataBinding(savedInstanceState)
        initState()
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

    /**
     * 沉浸式状态栏
     */
    private fun initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }
}
