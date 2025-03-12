package com.eki.parking.Controller.tools

import com.eki.parking.Controller.impl.IMail
import com.eki.parking.R
import com.eki.parking.extension.string
import com.hill.devlibs.list.DataList

/**
 * Created by Hill on 2020/10/12
 */
abstract class EkiMail:IMail {
    companion object{
        fun support(subject:String="問題反映"):EkiMail=object :Support(){
            override fun mailSubject(): String =subject
        }
    }

    private abstract class Support:EkiMail(){
        override fun sendMailTo(): ArrayList<String> = DataList(string(R.string.support_mail))
        override fun contentText(): String =""
    }

}