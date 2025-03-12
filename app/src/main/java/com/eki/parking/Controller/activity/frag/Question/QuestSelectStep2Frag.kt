package com.eki.parking.Controller.activity.frag.Question

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.R
import com.eki.parking.databinding.FragQuestionSelectStep2Binding
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/10/12
 */

class QuestSelectStep2Frag:SearchFrag(),ISetData<String>,IFragViewBinding{

    private lateinit var binding:FragQuestionSelectStep2Binding
    private var url=""

    override fun initFragView() {
        binding.webview.webViewClient= WebViewClient()
        binding.webview.settings.javaScriptEnabled=true
        binding.webview.loadUrl(url)
        binding.webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE // 新頁面
    }

    override fun onResumeFragView() {
        toolBarTitle=getString(R.string.Q_and_A)
    }

    override fun setData(data: String?) {
        data.notNull { url=it }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragQuestionSelectStep2Binding.inflate(inflater,container,false)
        return binding
    }

}