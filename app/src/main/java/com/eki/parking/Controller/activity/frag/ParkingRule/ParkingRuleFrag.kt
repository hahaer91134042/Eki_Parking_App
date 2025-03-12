package com.eki.parking.Controller.activity.frag.ParkingRule

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.R
import com.eki.parking.databinding.FragParkingRuleBinding
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/07/21
 */

class ParkingRuleFrag : SearchFrag(), IFragViewBinding {

    private lateinit var binding: FragParkingRuleBinding

    override fun initFragView() {
        toolBarTitle = getString(R.string.Parking_rules)

        val setting = binding.webview.settings
        setting.javaScriptEnabled = true
        binding.webview.webViewClient = RuleWebViewClient()
        binding.webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE // 新頁面
        binding.webview.loadUrl("https://www.ppyp.app/tc/policy_content.html")
    }

    private class RuleWebViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragParkingRuleBinding.inflate(inflater, container, false)
        return binding
    }
}