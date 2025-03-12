package com.eki.parking.Controller.activity.frag.Main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.LoginActivity
import com.eki.parking.Controller.frag.ChildFrag
import com.eki.parking.databinding.FragUserNoLoginBinding
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/05/26
 */
class UserNoLoginFrag: ChildFrag(),IFragViewBinding{

    private lateinit var binding: FragUserNoLoginBinding
    override fun initFragView() {
        binding.loginBtn.setOnClickListener {
            startActivitySwitchAnim(LoginActivity::class.java)
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragUserNoLoginBinding.inflate(inflater,container,false)
        return binding
    }

}