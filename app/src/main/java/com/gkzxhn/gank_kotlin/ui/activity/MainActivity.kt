package com.gkzxhn.gank_kotlin.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.databinding.ActivityMainBinding
import com.gkzxhn.gank_kotlin.ui.fragment.AndroidFragment
import com.gkzxhn.gank_kotlin.ui.fragment.GirlFragment
import com.gkzxhn.gank_kotlin.ui.fragment.ShipinFragment
import com.gkzxhn.gank_kotlin.utils.PermissionsChecker
import com.iflytek.autoupdate.IFlytekUpdate
import com.iflytek.autoupdate.UpdateConstants
import com.iflytek.autoupdate.UpdateErrorCode
import com.iflytek.autoupdate.UpdateType
import com.wingsofts.gankclient.toast
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : BaseActivity<ActivityMainBinding>() {
    val TAG = javaClass.simpleName

    // 所需的全部权限
    val PERMISSIONS = arrayOf(Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)

    private lateinit var mPermissionsChecker: PermissionsChecker // 权限检测器

    override fun initView() {
        updateVersion()

        mPermissionsChecker = PermissionsChecker(this);
        initFragments()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_home
    }

    lateinit var mFragments: ArrayList<Fragment>

    private fun initFragments() {
        mFragments = ArrayList()
        mFragments.add(AndroidFragment.newInstance())
        mFragments.add(ShipinFragment.newInstance())
        mFragments.add(GirlFragment.newInstance())
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityMainBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val transaction = supportFragmentManager.beginTransaction()
        when (item.itemId) {
            R.id.navigation_home -> {
                if (!mFragments[0].isAdded) {
                    transaction.add(R.id.content, mFragments.get(0))
                }
                transaction.show(mFragments[0])
                        .hide(mFragments[1])
                        .hide(mFragments[2])
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (!mFragments[1].isAdded) {
                    transaction.add(R.id.content, mFragments.get(1))
                }
                transaction.show(mFragments[1])
                        .hide(mFragments[0])
                        .hide(mFragments[2])
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                if (!mFragments[2].isAdded) {
                transaction.add(R.id.content, mFragments.get(2))
                }
                transaction.show(mFragments[2])
                        .hide(mFragments[1])
                        .hide(mFragments[0])
                        .commit()
                if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this,PERMISSIONS,100);
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //执行后续的操作

            }else{
                toast("没有权限无法使用说写日记哟...")
            }
        }
    }

    //初始化自动更新对象
    val updManager = IFlytekUpdate.getInstance(this)

    fun updateVersion(){
        //开启调试模式，默认不开启
//        updManager.setDebugMode(true)
        //开启wifi环境下检测更新，仅对自动更新有效，强制更新则生效
        updManager.setParameter(UpdateConstants.EXTRA_WIFIONLY, "true")
        //设置通知栏使用应用icon，详情请见示例
        updManager.setParameter(UpdateConstants.EXTRA_NOTI_ICON, "true")
        //设置更新提示类型，默认为通知栏提示
        updManager.setParameter(UpdateConstants.EXTRA_STYLE,UpdateConstants.UPDATE_UI_DIALOG)
//        updManager.setParameter(UpdateConstants.EXTRA_STYLE, UpdateConstants.UPDATE_UI_NITIFICATION);
        // 启动自动更新
        updManager.autoUpdate(this, { errorCode, updateInfo ->
            Log.i(TAG, "errorCode $errorCode")
            Log.i(TAG, "updateInfo Url ${updateInfo.downloadUrl}")
            Log.i(TAG, "updateInfo Version ${updateInfo.updateVersionCode}")
            if(errorCode == UpdateErrorCode.OK && updateInfo!= null) {
                if(updateInfo.getUpdateType() == UpdateType.NoNeed) {
                    return@autoUpdate
                }
                updManager.showUpdateInfo(this, updateInfo);
            }
        })
    }


}
