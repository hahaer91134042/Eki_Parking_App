package com.eki.parking.Controller.activity.frag.Html

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.databinding.FragWebPageBinding
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/10/23
 */

class WebPageFrag : SearchFrag(), ISetData<String>, IFragViewBinding {

    private lateinit var binding: FragWebPageBinding
    private lateinit var url: String

    override fun initFragView() {

        binding.webview.webViewClient = WebViewClient()
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE // 新頁面
        binding.webview.loadUrl(url)

    }

    override fun setData(data: String?) {
        url = data ?: ""
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragWebPageBinding.inflate(inflater, container, false)
        return binding
    }

}