package com.gkzxhn.gank_kotlin.ui.activity

import android.content.pm.ActivityInfo
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.airbnb.deeplinkdispatch.DeepLink
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.databinding.ActivityDetailBinding
import com.wingsofts.gankclient.router.GankClientUri
import kotlinx.android.synthetic.main.activity_detail.*
import java.net.URLDecoder


/**
 * Created by 方 on 2017/6/23.
 *
 */

@DeepLink("${GankClientUri.DETAIL}{${GankClientUri.DETAIL_PARAM_URL}}")
class DetailActivity : BaseActivity<ActivityDetailBinding>(){

    var url = ""
    private lateinit var webSettings : WebSettings

    override fun initView() {
        setupToolbar(toolbar)
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            url = URLDecoder.decode(intent.getStringExtra(GankClientUri.DETAIL_PARAM_URL))
        }
        tv_title.setText("干货集中营")
        webSettings = webView.settings
        webSettings.setJavaScriptEnabled(true)

        webView.setWebChromeClient(WebChromeClient())
        webSettings.pluginState = (WebSettings.PluginState.ON);
        webSettings.useWideViewPort = true
        webSettings.setSupportZoom(true)
        webView.loadUrl(url)
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        )
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityDetailBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_detail)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();//返回上一页面
        }else {
            super.onBackPressed()
        }
    }

    inner class MyWebChromeClient : WebChromeClient() {
        private var xprogressvideo: View? = null
        private var xCustomView: View? = null
        private var xCustomViewCallback: WebChromeClient.CustomViewCallback? = null

        // 播放网络视频时全屏会被调用的方法
        override fun onShowCustomView(view: View, callback: WebChromeClient.CustomViewCallback) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            webView.visibility = View.INVISIBLE
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden()
                return
            }
            video_fullView.addView(view)
            xCustomView = view
            xCustomViewCallback = callback
            video_fullView.setVisibility(View.VISIBLE)
        }

        // 视频播放退出全屏会被调用的
        override fun onHideCustomView() {
            if (xCustomView == null)
            // 不是全屏播放状态
                return

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            xCustomView!!.setVisibility(View.GONE)
            video_fullView.removeView(xCustomView)
            xCustomView = null
            video_fullView.setVisibility(View.GONE)
            xCustomViewCallback!!.onCustomViewHidden()
            webView.visibility = View.VISIBLE
        }

        // 视频加载时进程loading
        override fun getVideoLoadingProgressView(): View {
            if (xprogressvideo == null) {
                val inflater = LayoutInflater
                        .from(this@DetailActivity)
                xprogressvideo = inflater.inflate(
                        R.layout.video_loading_progress, null)
            }
            return xprogressvideo!!
        }
    }
}