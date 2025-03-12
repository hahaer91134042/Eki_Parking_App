package com.eki.parking.Controller.activity.frag.AboutMe

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.tools.EkiMail
import com.eki.parking.R
import com.eki.parking.databinding.FragAboutMeBinding
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/10/12
 */
class AboutMeFrag:SearchFrag(),IFragViewBinding{

    private lateinit var binding:FragAboutMeBinding

    override fun initFragView() {
        toolBarTitle=getString(R.string.About_me)

        binding.lineFrame.setOnClickListener {

            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://line.me/R/ti/p/@ppyp"))
            startActivity(i)
        }

        binding.phoneFrame.setOnClickListener {

            var call = Intent(Intent.ACTION_CALL, Uri.parse("tel:0227041758"))
            startActivity(call)
        }

        binding.mailFrame.setOnClickListener {
            app.mailManager.send(EkiMail.support(""))
        }

    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragAboutMeBinding.inflate(inflater,container,false)
        return binding
    }
}