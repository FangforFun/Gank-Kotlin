package com.gkzxhn.gank_kotlin.ui.fragment

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.databinding.ActivityImageBinding
import com.gkzxhn.gank_kotlin.ui.activity.BaseActivity
import com.gkzxhn.gank_kotlin.ui.adapter.MyPhotoPagerAdapter
import com.gkzxhn.gank_kotlin.ui.wegit.MyDragPhotoView
import kotlinx.android.synthetic.main.activity_image.*

/**
 * Created by 方 on 2017/6/22.
 */
class ImageActivity : BaseActivity<ActivityImageBinding>() {

    private lateinit var urls : ArrayList<String>

    override fun initView() {
        /*drag_photo.setOnExitListener { dragPhotoView, fl1, fl2, fl3, fl4 ->
            Log.i("ImageActivity", "dragPhotoView 1 : " + fl1)
            Log.i("ImageActivity", "dragPhotoView 1 : " + fl2)
            Log.i("ImageActivity", "dragPhotoView 1 : " + fl3)
            Log.i("ImageActivity", "dragPhotoView 1 : " + fl4)
            finish()
        }
        drag_photo.setOnTapListener { view ->
            finish()
        }*/
//        mBinding.url = intent.getStringExtra(IMG)
        urls = intent.getStringArrayListExtra(IMG)
        var index = intent.getIntExtra(INDEX, 0)
        val imageViewList = arrayListOf<ImageView>()
        for (url in urls) {
            val imageView = MyDragPhotoView(this)
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER)
            imageView.isClickable = true
            imageView.setOnTapListener { finish() }
            imageView.setOnExitListener { var1, var2, var3, var4, var5 ->
                finish()
            }
            Glide.with(imageView.context)
                    .load(url)
                    .crossFade()
                    .into(imageView)
            imageViewList.add(imageView)
        }
        viewpager_photo.adapter = MyPhotoPagerAdapter(imageViewList)
        viewpager_photo.currentItem = index
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityImageBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_image)
    }

    companion object{
        val IMG = "IMG"
        val INDEX = "INDEX"
        fun startActivity(context: Context, imageView: ImageView, urls: java.util.ArrayList<String>, index: Int) {
            val intent = Intent(context, ImageActivity::class.java)
            intent.putStringArrayListExtra(IMG, urls)
            intent.putExtra(INDEX, index)
            if(Build.VERSION.SDK_INT > 21) {
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity, imageView, "img").toBundle())
            }else{
                context.startActivity(intent)
            }
        }
    }
}