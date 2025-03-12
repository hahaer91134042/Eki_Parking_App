package com.eki.parking.View.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2021/11/30
 */
class PpypBottomBar(context: Context?, attrs: AttributeSet?) : LinearCustomView(context, attrs) {

    private lateinit var mapBtn:View
    private lateinit var checkoutBtn:View
    private lateinit var notifyBtn:View
    private lateinit var settingBtn:View

    abstract class SelectEvent{
        open fun selectable():Boolean=true
        abstract fun onClick()
    }

    var eventMap=HashMap<View,SelectEvent>()

    override fun initInFlaterView() {
        mapBtn=itemView.findViewById(R.id.mapOption)
        checkoutBtn=itemView.findViewById(R.id.checkoutOption)
        notifyBtn=itemView.findViewById(R.id.notifyOption)
        settingBtn=itemView.findViewById(R.id.settingOption)

        var imgCtrl=ImgController()

        mapBtn.setOnClickListener {
            Log.i("view map click")
            if(eventMap[mapBtn]?.selectable()==true)
                imgCtrl.selectMap()
            eventMap[mapBtn]?.onClick()
        }
        //check out 比較例外
        checkoutBtn.setOnClickListener {
            Log.i("view checkout click")
            if(eventMap[checkoutBtn]?.selectable()==true)
                imgCtrl.selectCheckout()
            eventMap[checkoutBtn]?.onClick()
//            event?.onCheckout()
        }
        notifyBtn.setOnClickListener {
            Log.i("view notify click")
            if(eventMap[notifyBtn]?.selectable()==true)
                imgCtrl.selectNotify()
            eventMap[notifyBtn]?.onClick()
        }
        settingBtn.setOnClickListener {
            Log.i("view setting click")
            if(eventMap[settingBtn]?.selectable()==true)
                imgCtrl.selectSetting()
            eventMap[settingBtn]?.onClick()
        }
    }

    fun addMapEvent(e:SelectEvent){
        eventMap[mapBtn]=e
    }
    fun addCheckoutEvent(e:SelectEvent){
        eventMap[checkoutBtn]=e
    }
    fun addNotifyEvent(e:SelectEvent){
        eventMap[notifyBtn]=e
    }
    fun addSettingEvent(e:SelectEvent){
        eventMap[settingBtn]=e
    }

    fun selectMap(){
        mapBtn.performClick()
    }
    fun selectCheckout(){
        checkoutBtn.performClick()
    }
    fun selectNotify(){
        notifyBtn.performClick()
    }
    fun selectSetting(){
        settingBtn.performClick()
    }

    override fun setInflatView(): Int= R.layout.item_bottombar
    override fun initNewLayoutParams(): ViewGroup.LayoutParams?=null

    private inner class ImgController{

        var imgMap= mapOf(
            Pair(mapBtn,itemView.findViewById<ImageView>(R.id.map_img)),
            Pair(checkoutBtn,itemView.findViewById<ImageView>(R.id.checkout_img)),
            Pair(notifyBtn,itemView.findViewById<ImageView>(R.id.notify_img)),
            Pair(settingBtn,itemView.findViewById<ImageView>(R.id.setting_img))
        )

        fun selectMap(){
            imgMap[mapBtn]?.isSelected=true
            mapBtn.isClickable=false
            imgMap.filter { it.key!=mapBtn }
                .forEach {
                    it.value.isSelected=false
                    it.key.isClickable=true
                }
        }

        fun selectCheckout(){
            imgMap[checkoutBtn]?.isSelected=true
            checkoutBtn.isClickable=false
            imgMap.filter { it.key!=checkoutBtn }
                .forEach {
                    it.value.isSelected=false
                    it.key.isClickable=true
                }
        }

        fun selectNotify(){
            imgMap[notifyBtn]?.isSelected=true
            notifyBtn.isClickable=false
            imgMap.filter { it.key!=notifyBtn }
                .forEach {
                    it.value.isSelected=false
                    it.key.isClickable=true
                }
        }

        fun selectSetting(){
            imgMap[settingBtn]?.isSelected=true
            settingBtn.isClickable=false
            imgMap.filter { it.key!=settingBtn }
                .forEach {
                    it.value.isSelected=false
                    it.key.isClickable=true
                }
        }
    }
}