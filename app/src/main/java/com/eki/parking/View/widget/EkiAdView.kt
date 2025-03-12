package com.eki.parking.View.widget

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageView
import com.eki.parking.R
import com.eki.parking.View.abs.ConstrainCustomView
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2021/12/15
 */
class EkiAdView(context: Context?) : ConstrainCustomView(context) {

    private lateinit var cancelBtn: ImageView
    private lateinit var webView: WebView

    interface OnAdLoadEvent{
        fun onStartLoad()
        fun onAdSuccess()
        fun onAdError()
    }

    private var adParent:ViewGroup?=null

    var httpStatus=200

    var adLoadEvent:OnAdLoadEvent?=null

    override fun initInFlaterView() {
        cancelBtn = findViewById(R.id.cancelBtn)
        webView = findViewById(R.id.webView)

        cancelBtn.setOnClickListener {
            adParent?.visibility= GONE
        }

        webView.webViewClient=AdWebClient()

        val setting = webView.settings
        setting.builtInZoomControls = false // 设置支持缩放
        setting.domStorageEnabled = true
        setting.databaseEnabled = true
        setting.javaScriptCanOpenWindowsAutomatically = true // 支持通过Javascript打开新窗口
        setting.javaScriptEnabled = true // 设置jS
        setting.allowFileAccess = true // 设置可以访问文件
        setting.cacheMode = WebSettings.LOAD_NO_CACHE // 新頁面

    }

    fun bindView(parent:ViewGroup):EkiAdView{
        adParent=parent
        adParent?.addView(this)

        return this
    }

    fun loadUrl(url:String):EkiAdView{
        Log.w("Ad view load url->$url")
        webView.loadUrl(url)
        return this
    }

    private inner class AdWebClient: WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            Log.w("onPageStarted url->$url")
            adLoadEvent?.onStartLoad()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            Log.d("onPageFinish url->$url")
            //不論網頁是否正確(404 or 200)最終都會執行這裡
            if(httpStatus==200){
                adLoadEvent?.onAdSuccess()
            }else{
                adLoadEvent?.onAdError()
            }

        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            //先暫存網頁狀態
            httpStatus=errorResponse?.statusCode?:404

            Log.w("onReceivedHttpError code->${httpStatus}")

        }
    }

    override fun setInflatView(): Int = R.layout.item_ad_view
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}