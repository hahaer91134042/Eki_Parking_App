package com.eki.parking.View.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.eki.parking.Model.EnumClass.ChargeSocket
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.Model.sql.EkiLocation
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView
import com.eki.parking.extension.string
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2020/10/05
 */
class LocationIntroducePanel(context: Context?, attrs: AttributeSet?) :
    LinearCustomView(context, attrs), EkiAdView.OnAdLoadEvent {

    lateinit var storeInfoBtn: LottieAnimationView

    private val addressText: TextView = findViewById(R.id.addressText)
    private val serialText: TextView = findViewById(R.id.serialText)
    private val nameText: TextView = findViewById(R.id.nameText)
    private val chargeTypeText: TextView = findViewById(R.id.chargeTypeText)
    private val socketTypeText: TextView = findViewById(R.id.socketTypeText)
    private val precationText: TextView = findViewById(R.id.precationText)
    private val positionText: TextView = findViewById(R.id.positionText)
    private val typeText: TextView = findViewById(R.id.typeText)
    private val sizeText: TextView = findViewById(R.id.sizeText)
    private val heightText: TextView = findViewById(R.id.heightText)
    private val weightText: TextView = findViewById(R.id.weightText)

    override fun onStartLoad() {

    }

    override fun onAdSuccess() {
        Log.w("$TAG AD success")
        showStoreInfo()
    }

    override fun onAdError() {
        Log.i("$TAG AD error")
        closeStoreInfo()
    }

    fun showStoreInfo() {
        storeInfoBtn.visibility = View.VISIBLE
    }

    fun closeStoreInfo() {
        storeInfoBtn.visibility = View.INVISIBLE
    }

    fun setLoc(loc: EkiLocation) {

        addressText.text = "${loc.Address?.City}${loc.Address?.Detail}"
        serialText.text = loc.Info?.SerialNumber
        nameText.text = loc.Info?.Content

        chargeTypeText.text = when (loc.current) {
            CurrentEnum.NONE -> string(loc.current.str)
            else -> loc.Socket.groupBy { it.currentEnum }.let {
                var builder = StringBuilder()
                it.forEach { pair ->
                    builder.append("${string(pair.key.str)}/")
                }
                builder.substring(0, builder.length - 1)
            }
        }

        socketTypeText.text = when {
            loc.Socket.size < 1 -> string(ChargeSocket.NONE.socketName)
            else -> loc.Socket.sortedBy { it.Current }.let {
                var builder = StringBuilder()
                it.forEach { s ->
                    if (s.currentEnum == CurrentEnum.NONE) {
                        builder.append("${string(s.chargeSocket.socketName)}、")
                    } else {
                        builder.append("(${s.currentEnum})${string(s.chargeSocket.socketName)}、")
                    }
                }
                builder.substring(0, builder.length - 1)
            }
        }

        precationText.text = loc.Config?.Text

        positionText.text = string(loc.Info?.sitePosition?.strRes!!)
        typeText.text = string(loc.Info?.siteType?.strRes!!)
        sizeText.text = string(loc.Info?.siteSize?.strRes!!)
        heightText.text = when {
            loc.Info?.Height!! > 0.0 -> "限高${loc.Info?.Height}m"
            else -> string(R.string.Unlimited)
        }
        weightText.text = when {
            loc.Info?.Weight!! > 0.0 -> "限重${loc.Info?.Weight}kg"
            else -> string(R.string.Unlimited)
        }
    }

    override fun initInFlaterView() {
        //防止空直
        storeInfoBtn = itemView.findViewById(R.id.storeInfoView)
    }

    override fun setInflatView(): Int = R.layout.item_location_introduce
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null

}