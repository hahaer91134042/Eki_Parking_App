package com.eki.parking.Controller.activity.frag.Login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnMemberFragSwitch
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.Model.sql.MemberLoginInfo
import com.eki.parking.R
import com.eki.parking.databinding.FragLoginBinding
import com.eki.parking.extension.sqlData
import com.eki.parking.extension.sqlHasData
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.listener.OnSuccessListener

/**
 * Created by Hill on 2019/6/18
 */

class LoginFrag : SearchFrag(), IFragViewBinding {
    private lateinit var binding: FragLoginBinding
    var memberSwitch: OnMemberFragSwitch? = null

    override fun initFragView() {
        if (sqlHasData<MemberLoginInfo>())
            binding.loginPanel.setAccount(sqlData<MemberLoginInfo>()?.account ?: "")
    }

    override fun onResumeFragView() {
        toolBarTitle = getString(R.string.login_member)
    }

    override fun onResume() {
        super.onResume()

        binding.loginPanel.listener = object : OnSuccessListener<EkiMember> {
            override fun onSuccess(value: EkiMember) {
                closeKeyBoard()
                memberSwitch?.onFinish()
            }
        }

        binding.registerTex.setOnClickListener {
            memberSwitch?.onSmsCheck()
        }

    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragLoginBinding.inflate(inflater, container, false)
        return binding
    }
}