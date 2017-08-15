package com.gkzxhn.gank_kotlin.ui.fragment

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.bean.info.BitmapTemp
import com.gkzxhn.gank_kotlin.databinding.ActivityImageBinding
import com.gkzxhn.gank_kotlin.ui.activity.BaseActivity
import com.gkzxhn.gank_kotlin.ui.adapter.MyPhotoPagerAdapter
import com.gkzxhn.gank_kotlin.ui.wegit.MyDragPhotoView
import com.gkzxhn.gank_kotlin.utils.rxbus.PageChangedEvent
import com.gkzxhn.gank_kotlin.utils.rxbus.RxBus
import kotlinx.android.synthetic.main.activity_image.*




/**
 * Created by 方 on 2017/6/22.
 */
class ImageActivity : BaseActivity<ActivityImageBinding>() {

    val TAG = this.javaClass.simpleName

    private lateinit var urls : ArrayList<String>

    var mOriginLeft: Int = 0
    var mOriginTop: Int = 0
    var mOriginHeight: Int = 0
    var mOriginWidth: Int = 0
    var mOriginCenterX: Int = 0
    var mOriginCenterY: Int = 0
    private var targetX: Int = 0
    private var targetY: Int = 0
    private var mTargetHeight: Float = 0.toFloat()
    private var mTargetWidth: Float = 0.toFloat()
    private var mScaleX: Float = 0.toFloat()
    private var mScaleY: Float = 0.toFloat()
    private var mTranslationX: Float = 0.toFloat()
    private var mTranslationY: Float = 0.toFloat()
    private var scale: Float = 0.toFloat()

    private val mPhotoViews = arrayListOf<ImageView>()

    private lateinit var mBus: RxBus

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initView() {
//        mBinding.url = intent.getStringExtra(IMG)
        urls = intent.getStringArrayListExtra(IMG)

        mBus = RxBus.instance

        val index = intent.getIntExtra(INDEX, 0)
        for (i in urls.indices) {
            val imageView = MyDragPhotoView(this)
            imageView.transitionName = "img"
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER)
            imageView.isClickable = true
            imageView.setOnTapListener { finishWithAnimation() }
            imageView.setOnExitListener { var1, var2, var3, var4, var5, var6 ->
                performExitAnimation(var1, var2, var3, var4, var5, var6)
            }
            if (index == -1 && i == 0) {
                if (null!= BitmapTemp.bitmaps[index]) {
                    imageView.setImageBitmap(BitmapTemp.bitmaps[index])
                }else {
                    Glide.with(imageView.context)
                            .load(urls[i])
                            .asBitmap()//强制Glide返回一个Bitmap对象
                            .error(R.drawable.error_photo)
                            .into(object : SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL){
                                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                    val width = resource!!.getWidth()
                                    val height = resource.getHeight()
                                    Log.d(TAG, "width2 " + width) //400px
                                    Log.d(TAG, "height2 " + height) //400px
                                    Log.d(TAG, "index " + index) //400px
                                    imageView.setImageBitmap(resource)
                                    BitmapTemp.bitmaps.put(index, resource)
                                    getScaled(resource)
                                }
                            })
                }
            }else if (null!= BitmapTemp.bitmaps[i]) {
                imageView.setImageBitmap(BitmapTemp.bitmaps[i])
            }else {
                Glide.with(imageView.context)
                        .load(urls[i])
                        .asBitmap()//强制Glide返回一个Bitmap对象
                        .error(R.drawable.error_photo)
                        .into(object : SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL){
                            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                val width = resource!!.getWidth()
                                val height = resource.getHeight()
                                Log.d(TAG, "width2 " + width) //400px
                                Log.d(TAG, "height2 " + height) //400px
                                Log.d(TAG, "index " + index) //400px
                                imageView.setImageBitmap(resource)
                                BitmapTemp.bitmaps.put(index, resource)
                                if (i == viewpager_photo.currentItem) {
                                    getScaled(resource)
                                }
                            }
                        })
            }
            mPhotoViews.add(imageView)
        }
        viewpager_photo.adapter = MyPhotoPagerAdapter(mPhotoViews)

        viewpager_photo.getViewTreeObserver()
                .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        viewpager_photo.getViewTreeObserver().removeOnGlobalLayoutListener(this)

                        mOriginLeft = intent.getIntExtra("left", 0)
                        mOriginTop = intent.getIntExtra("top", 0)
                        mOriginHeight = intent.getIntExtra("height", 0)
                        mOriginWidth = intent.getIntExtra("width", 0)

                        mOriginCenterX = mOriginLeft + mOriginWidth / 2
                        mOriginCenterY = mOriginTop + mOriginHeight / 2

                        val location = IntArray(2)

                        val photoView = this@ImageActivity.mPhotoViews[viewpager_photo.currentItem]
                        photoView.getLocationOnScreen(location)

                        mTargetHeight = photoView.getHeight().toFloat()
                        mTargetWidth = photoView.getWidth().toFloat()
                        mScaleX = mOriginWidth.toFloat() / mTargetWidth
                        mScaleY = mOriginHeight.toFloat() / mTargetHeight

                        targetX = location[0]
                        targetY = location[1]
                        val targetCenterX = targetX + mTargetWidth / 2
                        val targetCenterY = targetY + mTargetHeight / 2

                        mTranslationX = mOriginCenterX - targetCenterX
                        mTranslationY = mOriginCenterY - targetCenterY
                        photoView.setTranslationX(mTranslationX)
                        photoView.setTranslationY(mTranslationY)

                        Log.i(TAG, "getViewTreeObserver : $mTranslationX ")

                        var bitmap = BitmapTemp.bitmaps[viewpager_photo.currentItem]
                        if (index == -1) {
                            bitmap = BitmapTemp.bitmaps[-1]
                        }
                        if (bitmap != null) {
                            getScaled(bitmap)
                        }

                        performEnterAnimation()

                        /*for (i in 0..this@ImageActivity.mPhotoViews.size - 1) {
                            (this@ImageActivity.mPhotoViews[i] as MyDragPhotoView).setMinScale(mScaleX)
                        }*/
                    }
                })
        viewpager_photo.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                var bitmap = BitmapTemp.bitmaps[viewpager_photo.currentItem]
                if (index == -1) {
                    bitmap = BitmapTemp.bitmaps[-1]
                }
                if (bitmap != null) {
                    mBus.send(PageChangedEvent(position))
                    getScaled(bitmap)
                }
            }
        })
        if (index == -1){
            viewpager_photo.currentItem = 0
        }else {
            viewpager_photo.currentItem = index
        }
    }

    /**
     * 得到缩放比例
     */
    private fun getScaled(bitmap: Bitmap?) {
        val realW2h = bitmap!!.width / bitmap.height.toFloat()
        val originW2h = mOriginWidth / mOriginHeight.toFloat()
        val targetW2h = mTargetWidth / mTargetHeight
        //centercrop如果控件的宽高比大于图片的宽高比,那么控件的宽度就等于图片的宽度
        if (originW2h >= realW2h) {
            if (targetW2h <= realW2h) {
                //fitCenter如果控件的宽高比小于图片的宽高比,那么控件的宽度就等于图片的宽度
                scale = mOriginWidth / mTargetWidth
            } else {
                scale = mOriginWidth / mTargetHeight / realW2h
            }
        } else {
            if (targetW2h <= realW2h) {
                scale = mOriginHeight / mTargetWidth * realW2h
            } else {
                scale = mOriginHeight / mTargetHeight
            }
        }
        Log.i(TAG, "scale:  $scale")
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

            val location = intArrayOf(0, 1)
            imageView.getLocationOnScreen(location)
            intent.putExtra("left", location[0])
            intent.putExtra("top", location[1])
            intent.putExtra("height", imageView.getHeight())
            intent.putExtra("width", imageView.getWidth())
            if(Build.VERSION.SDK_INT > 21) {
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity, imageView, "img").toBundle())
            }else{
                context.startActivity(intent)
            }
            (context as Activity).overridePendingTransition(0, 0)
        }
    }

    private val DURATION = 200L //动画时长

    /**
     * 拖拽关闭界面的动画效果
     */
    private fun performExitAnimation(view: MyDragPhotoView, x: Float, y: Float, w: Float, h: Float, alpha: Int) {
        Log.i(TAG, "traslateX : " + x)
        Log.i(TAG, "traslateY : " + y)
        Log.i(TAG, "width : " + w)
        Log.i(TAG, "height : " + h)
        Log.i(TAG, "height : " + h)
        Log.i(TAG, "alpha : " + alpha)

//        view.finishAnimationCallBack()

        val translateXAnimator = ValueAnimator.ofFloat(x, mTranslationX)
        translateXAnimator.addUpdateListener { valueAnimator ->
            view.translateX = valueAnimator.animatedValue as Float }
        translateXAnimator.duration = DURATION
        translateXAnimator.start()
        val translateYAnimator = ValueAnimator.ofFloat(y, mTranslationY)
        translateYAnimator.addUpdateListener { valueAnimator ->
            view.translateY = valueAnimator.animatedValue as Float }
        translateYAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                animator.removeAllListeners()
                finish()
                overridePendingTransition(0, 0)
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
        translateYAnimator.duration = DURATION
        translateYAnimator.start()

        val alphaAnimator = ValueAnimator.ofInt(alpha, 0)
        alphaAnimator.addUpdateListener { valueAnimator ->
            view.setMAlpha(valueAnimator.animatedValue as Int)
        }
        alphaAnimator.duration = DURATION
        alphaAnimator.start()

        val scaleAnimator = ValueAnimator.ofFloat(view.getmScale(), scale)
        scaleAnimator.addUpdateListener { valueAnimator ->
            view.setmScale(valueAnimator.animatedValue as Float)
        }
        scaleAnimator.duration = DURATION
        scaleAnimator.start()
    }

    private fun finishWithAnimation() {

        val photoView = mPhotoViews[viewpager_photo.currentItem] as MyDragPhotoView
        val translateXAnimator = ValueAnimator.ofFloat(0f, mTranslationX)
        translateXAnimator.addUpdateListener { valueAnimator -> photoView.translateX=valueAnimator.animatedValue as Float }
        translateXAnimator.duration = DURATION
        translateXAnimator.start()

        val translateYAnimator = ValueAnimator.ofFloat(0f, mTranslationY)
        translateYAnimator.addUpdateListener { valueAnimator -> photoView.translateY = valueAnimator.animatedValue as Float }
        translateYAnimator.duration = DURATION
        translateYAnimator.start()

        val scaleAnimator = ValueAnimator.ofFloat(1f, scale)
        scaleAnimator.addUpdateListener { valueAnimator ->
            photoView.setmScale(valueAnimator.animatedValue as Float)
        }
        scaleAnimator.duration = DURATION
        scaleAnimator.start()

        val alphaAnimator = ValueAnimator.ofInt(255, 0)
        alphaAnimator.addUpdateListener { valueAnimator ->
            photoView.setMAlpha(valueAnimator.animatedValue as Int)
        }
        alphaAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                animator.removeAllListeners()
                finish()
                overridePendingTransition(0, 0)
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
        alphaAnimator.duration = DURATION
        alphaAnimator.start()
    }

    private fun performEnterAnimation() {
        val photoView = mPhotoViews[viewpager_photo.currentItem]
        val translateXAnimator = ValueAnimator.ofFloat(photoView.getX(), 0f)
        translateXAnimator.addUpdateListener { valueAnimator -> photoView.setX(valueAnimator.animatedValue as Float) }
        translateXAnimator.duration = DURATION
        translateXAnimator.start()

        val translateYAnimator = ValueAnimator.ofFloat(photoView.getY(), 0f)
        translateYAnimator.addUpdateListener { valueAnimator -> photoView.setY(valueAnimator.animatedValue as Float) }
        translateYAnimator.duration = DURATION
        translateYAnimator.start()

        val scaleYAnimator = ValueAnimator.ofFloat(mScaleY, 1f)
        scaleYAnimator.addUpdateListener { valueAnimator -> photoView.setScaleY(valueAnimator.animatedValue as Float) }
        scaleYAnimator.duration = DURATION
        scaleYAnimator.start()

        val scaleXAnimator = ValueAnimator.ofFloat(mScaleX, 1f)
        scaleXAnimator.addUpdateListener { valueAnimator -> photoView.setScaleX(valueAnimator.animatedValue as Float) }
        scaleXAnimator.duration = DURATION
        scaleXAnimator.start()
    }

    override fun onBackPressed() {
        finishWithAnimation()
    }
}