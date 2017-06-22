package com.gkzxhn.gank_kotlin.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.databinding.ActivityMainBinding
import com.gkzxhn.gank_kotlin.ui.fragment.AndroidFragment
import com.gkzxhn.gank_kotlin.ui.fragment.GirlFragment
import com.gkzxhn.gank_kotlin.ui.fragment.IosFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun initView() {
        initFragments()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    lateinit var mFragments: ArrayList<Fragment>

    private fun initFragments() {
        mFragments = ArrayList()
        mFragments.add(AndroidFragment.newInstance())
        mFragments.add(IosFragment.newInstance())
        mFragments.add(GirlFragment.newInstance())
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityMainBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val transaction = supportFragmentManager.beginTransaction()
        when (item.itemId) {
            R.id.navigation_home -> {
                transaction.replace(R.id.content, mFragments.get(0))
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                transaction.replace(R.id.content, mFragments.get(1))
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                transaction.replace(R.id.content, mFragments.get(2))
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}
