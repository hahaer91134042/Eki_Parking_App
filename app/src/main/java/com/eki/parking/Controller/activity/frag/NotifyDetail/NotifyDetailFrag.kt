package com.eki.parking.Controller.activity.frag.NotifyDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppConfig
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.EnumClass.NotifyType
import com.eki.parking.databinding.FragNotifyDetailBinding
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/07/27
 */

class NotifyDetailFrag : SearchFrag(), ISetData<ResponseInfo.EkiNotify>, IFragViewBinding {

    private lateinit var binding: FragNotifyDetailBinding
    lateinit var notify: ResponseInfo.EkiNotify

    override fun initFragView() {
        val setting = binding.webview.settings
        setting.javaScriptEnabled = true
        binding.webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE // 新頁面

        when (notify.type) {
            NotifyType.Server -> {
                binding.webview.loadDataWithBaseURL(
                    AppConfig.Url.web,
                    notify.Server?.Html ?: "",
                    "text/html",
                    "UTF-8",
                    ""
                )
            }
            NotifyType.Action -> {
                binding.webview.loadDataWithBaseURL(
                    AppConfig.Url.web,
                    notify.Action?.Page?.Html ?: "",
                    "text/html",
                    "UTF-8",
                    ""
                )
            }
        }

    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragNotifyDetailBinding.inflate(inflater, container, false)
        return binding
    }

    override fun setData(data: ResponseInfo.EkiNotify?) {
        data.notNull { notify = it }
    }

}