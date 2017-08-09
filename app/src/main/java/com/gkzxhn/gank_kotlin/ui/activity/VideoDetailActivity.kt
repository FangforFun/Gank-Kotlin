package com.gkzxhn.gank_kotlin.ui.activity

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.bean.info.VideoBean
import com.gkzxhn.gank_kotlin.databinding.ActivityVideoDetailBinding
import com.gkzxhn.gank_kotlin.utils.StringUtils
import com.gkzxhn.gank_kotlin.utils.VideoListener
import com.shuyu.gsyvideoplayer.GSYVideoPlayer
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import kotlinx.android.synthetic.main.activity_video_detail.*
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.Exception
import java.util.concurrent.ExecutionException

/**
 * Created by 方 on 2017/7/26.
 */
class VideoDetailActivity : BaseActivity<ActivityVideoDetailBinding>() {
    val TAG = this::class.java.simpleName

    private lateinit var videoBean : VideoBean
    var mContext: Context = this


    override fun initView() {
        videoBean = intent.getParcelableExtra<VideoBean>("data")
        mBinding.videobean = videoBean
        tv_video_time.setText(StringUtils.getDetailText(videoBean.duration!!))
        prepareVideo()
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityVideoDetailBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_video_detail)
    }

    companion object {
        var MSG_IMAGE_LOADED = 101
    }

    lateinit var imageView: ImageView
    var isPlay: Boolean = false
    var isPause: Boolean = false
    lateinit var orientationUtils: OrientationUtils
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                MSG_IMAGE_LOADED -> {
                    Log.e("video", "setImage")
                    gsy_player.setThumbImageView(imageView)
                }
            }
        }
    }

    private fun prepareVideo() {
        gsy_player.setUp(videoBean.playUrl, false, null, null, null)
        //增加封面
        imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
//        ImageViewAsyncTask(mHandler, this, imageView).execute(videoBean.feed)

        Glide.with(imageView.context).load(videoBean.feed)
                /*.asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)*/
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .into(object : GlideDrawableImageViewTarget(imageView){
                    override fun onResourceReady(resource: GlideDrawable?, animation: GlideAnimation<in GlideDrawable>?) {
                        super.onResourceReady(resource, animation)
                        gsy_player.setThumbImageView(imageView)
                    }

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        Log.e(TAG, e.toString())
                        imageView.setImageDrawable(errorDrawable)
                        gsy_player.setThumbImageView(imageView)
                    }
                })


        gsy_player.titleTextView.visibility = View.GONE
        gsy_player.backButton.visibility = View.VISIBLE
        orientationUtils = OrientationUtils(this, gsy_player)
        gsy_player.setIsTouchWiget(true);
        //关闭自动旋转
        gsy_player.isRotateViewAuto = false;
        gsy_player.isLockLand = false;
        gsy_player.isShowFullAnimation = false;
        gsy_player.isNeedLockFull = true;
        gsy_player.fullscreenButton.setOnClickListener {
            //直接横屏
            orientationUtils.resolveByClick();
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            gsy_player.startWindowFullscreen(mContext, true, true);
        }
        gsy_player.setStandardVideoAllCallBack(object : VideoListener() {
            override fun onPrepared(url: String?, vararg objects: Any?) {
                super.onPrepared(url, *objects)
                //开始播放了才能旋转和全屏
                orientationUtils.isEnable = true
                isPlay = true;
            }

            override fun onAutoComplete(url: String?, vararg objects: Any?) {
                super.onAutoComplete(url, *objects)

            }

            override fun onClickStartError(url: String?, vararg objects: Any?) {
                super.onClickStartError(url, *objects)
            }

            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                super.onQuitFullscreen(url, *objects)
                orientationUtils?.let { orientationUtils.backToProtVideo() }
            }
        })
        gsy_player.setLockClickListener { view, lock ->
            //配合下方的onConfigurationChanged
            orientationUtils.isEnable = !lock
        }
        gsy_player.backButton.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

    }

    private class ImageViewAsyncTask(handler: Handler, activity: VideoDetailActivity, private val mImageView: ImageView) : AsyncTask<String, Void, String>() {
        private var handler = handler
        private var mPath: String? = null
        private var mIs: FileInputStream? = null
        private var mActivity: VideoDetailActivity = activity
        override fun doInBackground(vararg params: String): String? {
            try {
            val future = Glide.with(mActivity)
                    .load(params[0])
                    .downloadOnly(100, 100)
                val cacheFile = future.get()
                mPath = cacheFile.absolutePath
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
            return mPath
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            try {
                mIs = FileInputStream(s)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            val bitmap = BitmapFactory.decodeStream(mIs)
            mImageView.setImageBitmap(bitmap)
            var message = handler.obtainMessage()
            message.what = MSG_IMAGE_LOADED
            handler.sendMessage(message)
        }
    }

    override fun onBackPressed() {
        orientationUtils?.let {
            orientationUtils.backToProtVideo()
        }
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoPlayer.releaseAllVideos()
        orientationUtils?.let {
            orientationUtils.releaseListener()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (isPlay && !isPause) {
            if (newConfig?.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                if (!gsy_player.isIfCurrentIsFullscreen) {
                    gsy_player.startWindowFullscreen(mContext, true, true)
                }
            } else {
                //新版本isIfCurrentIsFullscreen的标志位内部提前设置了，所以不会和手动点击冲突
                if (gsy_player.isIfCurrentIsFullscreen) {
                    StandardGSYVideoPlayer.backFromWindowFull(this);
                }
                orientationUtils?.let { orientationUtils.isEnable = true }
            }
        }
    }

}