package com.eki.parking.Controller.activity.frag.ForgetPwd

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.databinding.FragForgetPwdStep2Binding
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/10/07
 */
class ForgetPwdStep2Frag : SearchFrag(), ISetData<String>, IFragViewBinding {

    private lateinit var binding: FragForgetPwdStep2Binding
    private var smsCode = ""
    var onNext: (() -> Unit)? = null

    override fun initFragView() {
        binding.codeTex.whenInputChange {
            checkCodeValid(it)
        }

        binding.toNextBtn.setOnClickListener {
            onNext?.invoke()
        }
    }

    private fun checkCodeValid(code: String) {
        when (code.cleanTex == smsCode) {
            true -> binding.toNextBtn.isEnabled = true
            else -> binding.toNextBtn.isEnabled = false
        }
    }

    override fun setData(data: String?) {
        smsCode = data!!
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragForgetPwdStep2Binding.inflate(inflater, container, false)
        return binding
    }
}