package com.eki.parking.Controller.activity.frag.CheckOutProcess

import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Model.DTO.CheckoutFinal
import com.eki.parking.Model.EnumClass.EkiUri
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.databinding.FragPayPageBinding
import com.eki.parking.extension.isCheckOut
import com.eki.parking.extension.sqlSaveOrUpdate
import com.hill.devlibs.EnumClass.ProgressMode
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2019/11/28
 */
class PayPageFrag : SearchFrag(), ISetData<EkiOrder>, IFragViewBinding {

    private lateinit var order: EkiOrder
    var onFinal: (CheckoutFinal) -> Unit = {}

    private lateinit var binding:FragPayPageBinding

    override fun setData(data: EkiOrder?) {
        data.notNull { order = it }
    }

    override fun initFragView() {
        toolBarTitle = getString(R.string.Payment_page)

        if (order.CheckOutUrl.isNotEmpty()) {
            binding.payPageLoader.settings.javaScriptEnabled = true
            binding.payPageLoader.webViewClient = PayPageClient()

            binding.payPageLoader.loadUrl(order.CheckOutUrl)
            showProgress(ProgressMode.PROCESSING_MODE)
        } else {
            showToast("該訂單無法付款!")
            toMainActivity()
        }
    }

    private inner class PayPageClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            closeProgress()
            Log.i("$TAG onPageStarted url->${url}")
            url.notNull {
                val rawUri = Uri.parse(it)
                val uri = EkiUri.parse(rawUri)
//                Log.w("onPageStarted uri->$u query->${u.query} status->${u.getQueryParameter("Status")}")
                when (uri.host) {
                    EkiUri.Host.NewebPay,
                    EkiUri.Host.NewebPayTest -> {
                        if (uri.status == EkiUri.Status.Success) {
                            order.beCheckout()
                            order.sqlSaveOrUpdate()
                        }
                    }
                    //oppo 不會跑 shouldOverrideUrlLoading
                    else -> {
                        if (uri.isCheckOut()) {
                            if (uri.action == EkiUri.Action.Finish) {
                                onFinal(CheckoutFinal(rawUri))
                            }
                        }
                    }
                }
            }
        }

        //超連結再用
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            Log.d("$TAG OverrideUrlLoading url->${request?.url}")
            Log.w("$TAG host->${request?.url?.host}")
            Log.i("$TAG path->${request?.url?.path}")
            return super.shouldOverrideUrlLoading(view, request)

        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragPayPageBinding.inflate(inflater,container,false)
        return binding
    }
}