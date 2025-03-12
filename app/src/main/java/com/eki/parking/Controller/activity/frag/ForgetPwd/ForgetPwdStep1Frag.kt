package com.eki.parking.Controller.activity.frag.ForgetPwd

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.R
import com.eki.parking.databinding.FragForgetPwdStep1Binding
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.tools.ValidCheck


/**
 * Created by Hill on 2020/10/07
 */
class ForgetPwdStep1Frag : SearchFrag(), IFragViewBinding {

    private lateinit var binding: FragForgetPwdStep1Binding
    var onNext: ((String) -> Unit)? = null

    override fun initFragView() {
        toolBarTitle = getString(R.string.Reset_pwd)

        binding.phoneText.whenInputChange {
            setNextBtnEnable(it)
        }

        binding.toNextBtn.setOnClickListener {
            onNext?.invoke(binding.phoneText.input.cleanTex)
        }
    }

    private fun setNextBtnEnable(phone: String) {
        var result = ValidCheck.phone(phone)
        when (result.valid) {
            true -> binding.toNextBtn.isEnabled = true
            else -> binding.toNextBtn.isEnabled = false
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragForgetPwdStep1Binding.inflate(inflater, container, false)
        return binding
    }
}