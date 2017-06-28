package com.gkzxhn.gank_kotlin.ui.fragment

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.databinding.ActivityImageBinding
import com.gkzxhn.gank_kotlin.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_image.*

/**
 * Created by 方 on 2017/6/22.
 */
class ImageActivity : BaseActivity<ActivityImageBinding>() {
    override fun initView() {
        drag_photo.setOnExitListener { dragPhotoView, fl1, fl2, fl3, fl4 ->
            Log.i("ImageActivity", "dragPhotoView 1 : " + fl1)
            Log.i("ImageActivity", "dragPhotoView 1 : " + fl2)
            Log.i("ImageActivity", "dragPhotoView 1 : " + fl3)
            Log.i("ImageActivity", "dragPhotoView 1 : " + fl4)
            finish()
        }
        drag_photo.setOnTapListener { view ->
            finish()
        }
        mBinding.url = intent.getStringExtra(IMG)
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityImageBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_image)
    }

    companion object{
        val IMG = "IMG"
        fun startActivity(context: Context, imageView: ImageView, url: String) {
            val intent = Intent(context, ImageActivity::class.java)
            intent.putExtra(IMG, url)
            if(Build.VERSION.SDK_INT > 21) {
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity, imageView, "img").toBundle())
            }else{
                context.startActivity(intent)
            }
        }
    }
}