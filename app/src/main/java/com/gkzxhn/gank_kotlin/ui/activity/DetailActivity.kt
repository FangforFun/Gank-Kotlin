package com.gkzxhn.gank_kotlin.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
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
    override fun initView() {
        setupToolbar(toolbar)
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            url = URLDecoder.decode(intent.getStringExtra(GankClientUri.DETAIL_PARAM_URL))
        }
        tv_title.setText("干货集中营")
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
}