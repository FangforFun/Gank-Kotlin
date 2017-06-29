package com.gkzxhn.gank_kotlin.ui.fragment

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.view.ViewTreeObserver
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

    var mOriginLeft: Int = 0
    var mOriginTop: Int = 0
    var mOriginHeight: Int = 0
    var mOriginWidth: Int = 0
    var mOriginCenterX: Int = 0
    var mOriginCenterY: Int = 0
    private var mTargetHeight: Float = 0.toFloat()
    private var mTargetWidth: Float = 0.toFloat()
    private var mScaleX: Float = 0.toFloat()
    private var mScaleY: Float = 0.toFloat()
    private var mTranslationX: Float = 0.toFloat()
    private var mTranslationY: Float = 0.toFloat()

    private val mPhotoViews = arrayListOf<ImageView>()

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
        val index = intent.getIntExtra(INDEX, 0)
        for (url in urls) {
            val imageView = MyDragPhotoView(this)
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER)
            imageView.isClickable = true
            imageView.setOnTapListener { finishWithAnimation() }
            imageView.setOnExitListener { var1, var2, var3, var4, var5 ->
                performExitAnimation(var1, var2, var3, var4, var5)
            }
            Glide.with(imageView.context)
                    .load(url)
                    .crossFade()
                    .into(imageView)
            mPhotoViews.add(imageView)
        }
        viewpager_photo.adapter = MyPhotoPagerAdapter(mPhotoViews)
        viewpager_photo.currentItem = index

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

                        val targetCenterX = location[0] + mTargetWidth / 2
                        val targetCenterY = location[1] + mTargetHeight / 2

                        mTranslationX = mOriginCenterX - targetCenterX
                        mTranslationY = mOriginCenterY - targetCenterY
                        photoView.setTranslationX(mTranslationX)
                        photoView.setTranslationY(mTranslationY)

                        photoView.setScaleX(mScaleX)
                        photoView.setScaleY(mScaleY)

                        performEnterAnimation()

                        for (i in 0..this@ImageActivity.mPhotoViews.size - 1) {
                            (this@ImageActivity.mPhotoViews[i] as MyDragPhotoView).setMinScale(mScaleX)
                        }
                    }
                })
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
            imageView.getLocationOnScreen(location);
            intent.putExtra("left", location[0]);
            intent.putExtra("top", location[1]);
            intent.putExtra("height", imageView.getHeight());
            intent.putExtra("width", imageView.getWidth());
            if(Build.VERSION.SDK_INT > 21) {
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity, imageView, "img").toBundle())
            }else{
                context.startActivity(intent)
            }
            (context as Activity).overridePendingTransition(0, 0)
        }
    }

    /**
     * ===================================================================================
     * 底下是低版本"共享元素"实现   不需要过分关心  如有需要 可作为参考.
     */
    private fun performExitAnimation(view: MyDragPhotoView, x: Float, y: Float, w: Float, h: Float) {
        view.finishAnimationCallBack()
        val viewX = mTargetWidth / 2 + x - mTargetWidth * mScaleX / 2
        val viewY = mTargetHeight / 2 + y - mTargetHeight * mScaleY / 2
        view.x = viewX
        view.y = viewY

        val centerX = view.x + mOriginWidth / 2
        val centerY = view.y + mOriginHeight / 2

        val translateX = mOriginCenterX - centerX
        val translateY = mOriginCenterY - centerY


        val translateXAnimator = ValueAnimator.ofFloat(view.x, view.x + translateX)
        translateXAnimator.addUpdateListener { valueAnimator -> view.x = valueAnimator.animatedValue as Float }
        translateXAnimator.duration = 300
        translateXAnimator.start()
        val translateYAnimator = ValueAnimator.ofFloat(view.y, view.y + translateY)
        translateYAnimator.addUpdateListener { valueAnimator -> view.y = valueAnimator.animatedValue as Float }
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
        translateYAnimator.duration = 300
        translateYAnimator.start()
    }

    private fun finishWithAnimation() {

        val photoView = mPhotoViews[viewpager_photo.currentItem]
        val translateXAnimator = ValueAnimator.ofFloat(0f, mTranslationX)
        translateXAnimator.addUpdateListener { valueAnimator -> photoView.setX(valueAnimator.animatedValue as Float) }
        translateXAnimator.duration = 300
        translateXAnimator.start()

        val translateYAnimator = ValueAnimator.ofFloat(0f, mTranslationY)
        translateYAnimator.addUpdateListener { valueAnimator -> photoView.setY(valueAnimator.animatedValue as Float) }
        translateYAnimator.duration = 300
        translateYAnimator.start()

        val scaleYAnimator = ValueAnimator.ofFloat(1f, mScaleY)
        scaleYAnimator.addUpdateListener { valueAnimator -> photoView.setScaleY(valueAnimator.animatedValue as Float) }
        scaleYAnimator.duration = 300
        scaleYAnimator.start()

        val scaleXAnimator = ValueAnimator.ofFloat(1f, mScaleX)
        scaleXAnimator.addUpdateListener { valueAnimator -> photoView.setScaleX(valueAnimator.animatedValue as Float) }

        scaleXAnimator.addListener(object : Animator.AnimatorListener {
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
        scaleXAnimator.duration = 300
        scaleXAnimator.start()
    }

    private fun performEnterAnimation() {
        val photoView = mPhotoViews[viewpager_photo.currentItem]
        val translateXAnimator = ValueAnimator.ofFloat(photoView.getX(), 0f)
        translateXAnimator.addUpdateListener { valueAnimator -> photoView.setX(valueAnimator.animatedValue as Float) }
        translateXAnimator.duration = 300
        translateXAnimator.start()

        val translateYAnimator = ValueAnimator.ofFloat(photoView.getY(), 0f)
        translateYAnimator.addUpdateListener { valueAnimator -> photoView.setY(valueAnimator.animatedValue as Float) }
        translateYAnimator.duration = 300
        translateYAnimator.start()

        val scaleYAnimator = ValueAnimator.ofFloat(mScaleY, 1f)
        scaleYAnimator.addUpdateListener { valueAnimator -> photoView.setScaleY(valueAnimator.animatedValue as Float) }
        scaleYAnimator.duration = 300
        scaleYAnimator.start()

        val scaleXAnimator = ValueAnimator.ofFloat(mScaleX, 1f)
        scaleXAnimator.addUpdateListener { valueAnimator -> photoView.setScaleX(valueAnimator.animatedValue as Float) }
        scaleXAnimator.duration = 300
        scaleXAnimator.start()
    }

    override fun onBackPressed() {
        finishWithAnimation()
    }
}