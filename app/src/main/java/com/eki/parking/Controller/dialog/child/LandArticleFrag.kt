package com.eki.parking.Controller.dialog.child

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.ISerialDialog
import com.eki.parking.Controller.impl.ISerialEvent
import com.eki.parking.R
import com.eki.parking.databinding.DialogLandArticleBinding
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/04/13
 */
class LandArticleFrag : DialogChildFrag<LandArticleFrag>(), ISerialDialog, IFragViewBinding {

    private lateinit var binding: DialogLandArticleBinding
    private lateinit var serialEvent: ISerialEvent

    override fun initFragView() {
        binding.articleView.apply {
            webViewClient = WebClient()
            settings.cacheMode = WebSettings.LOAD_NO_CACHE // 新頁面
            loadUrl(getString(R.string.landArticleUrl))
        }

        binding.articleCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.determinBtn.isEnabled = isChecked
        }

        binding.determinBtn.setOnClickListener {
            serialEvent.onNext()
        }
    }

    private inner class WebClient : WebViewClient() {

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

    override val frag: DialogChildFrag<*>
        get() = this
    override val title: String
        get() = "地主條款"

    override fun setEvent(event: ISerialEvent) {
        serialEvent = event
    }

    override fun next(): ISerialDialog? = BeManagerFrag()
    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = DialogLandArticleBinding.inflate(inflater, container, false)
        return binding
    }
}