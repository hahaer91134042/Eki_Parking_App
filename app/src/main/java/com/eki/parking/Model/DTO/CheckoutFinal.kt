package com.eki.parking.Model.DTO

import android.net.Uri
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/05/27
 */
class CheckoutFinal(uri: Uri){
    var serial=uri.getQueryParameter("serial")!!
    var amt=uri.getQueryParameter("amt")!!.toDouble()
    var card4=uri.getQueryParameter("card4")!!
    var time= DateTime(uri.getQueryParameter("time")!!.toLong())
}