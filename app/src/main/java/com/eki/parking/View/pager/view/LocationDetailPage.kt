package com.eki.parking.View.pager.view

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.eki.parking.Model.sql.EkiLocation
import com.eki.parking.R
import com.eki.parking.View.abs.ConstrainCustomView
import com.eki.parking.View.libs.StateButton
import com.eki.parking.View.widget.AutoLoadImgView
import com.eki.parking.extension.currencyCost
import com.eki.parking.extension.string
import com.hill.devlibs.extension.messageFormat

/**
 * Created by Hill on 29,10,2019
 */
class LocationDetailPage(context: Context?) : ConstrainCustomView(context) {

    private lateinit var loc:EkiLocation
    var onReservaClick:((EkiLocation)->Unit)?=null

    private val imgView: AutoLoadImgView = findViewById(R.id.imgView)
    private val addressText: TextView = findViewById(R.id.addressText)
    private val chargeTypeText: TextView = findViewById(R.id.chargeTypeText)
    private val socketTypeText: TextView = findViewById(R.id.socketTypeText)
    private val serialText: TextView = findViewById(R.id.serialText)
    private val priceTextView: TextView = findViewById(R.id.priceTextView)
    private val remarkText: TextView = findViewById(R.id.remarkText)
    private val reservaBtn: StateButton = findViewById(R.id.reservaBtn)

    init {
        reservaBtn.setOnClickListener {
            onReservaClick?.invoke(loc)
        }
    }

    fun setLoc(loc:EkiLocation){
        this.loc=loc

        imgView.loadUrl(loc.Info?.Img,true)
        addressText.text="${loc.Address?.City}${loc.Address?.Detail}"
        chargeTypeText.text= string(loc.Socket.first().currentEnum?.str!!)
        socketTypeText.text= string(loc.Socket?.first()?.chargeSocket.socketName!!)
        serialText.text=loc.Info?.SerialNumber
        priceTextView.text= string(R.string.billing_method_30min).messageFormat(loc.currencyCost())
        remarkText.text=loc.Info?.Content
    }

    override fun setInflatView(): Int = R.layout.page_location_info

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}