package com.eki.parking.View.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eki.parking.Model.DTO.SocketSelect
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.Model.EnumClass.SiteSize
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.View.abs.FrameCustomView
import com.eki.parking.View.abs.LinearCustomView
import com.eki.parking.extension.color
import com.hill.devlibs.widget.AutoSizeTextView

/**
 * Created by Hill on 2021/12/17
 */
class SiteSocketSelectView(context: Context?, attrs: AttributeSet?) :
    FrameCustomView(context, attrs) {

    interface OnSocketSelectEvent{
        fun onSocketSelect(sockets:List<SocketSelect>)
    }

    var socketSelectEvent:OnSocketSelectEvent?=null

    private var selectCtrl:SocketSelectCtrl?=null

    fun creatFram(loc:ManagerLocation){
        removeAllViews()

        selectCtrl=when(loc.Info?.siteSize){
            SiteSize.Motor->MotorSocketSelectView(context)
            else->CarSocketSelectView(context)
        }

        addView(selectCtrl?.view)

        isEnabled=when(loc.current){
            CurrentEnum.NONE->false
            else->true
        }

        selectCtrl?.setSocketSelect(loc.Socket.map {
            SocketSelect(it.currentEnum,it.chargeSocket)
        })
    }

    fun cleanSelect(){
        selectCtrl?.setSocketSelect(listOf())
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        selectCtrl?.enable(enabled)
    }

    private fun selectBtn(btn: TextView,b:Boolean){
        btn.isSelected=b
        when(b){
            true->btn.setTextColor(color(R.color.Eki_green_2))
            else->btn.setTextColor(color(R.color.text_color_2))
        }
    }

    private fun onSelectSocket(map:Map<TextView,SocketSelect>){
        socketSelectEvent?.onSocketSelect(
            map.filter { it.key.isSelected }
                .map { it.value }
        )
    }

    override fun setInflatView(): Int =0
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null

    private inner class MotorSocketSelectView(context: Context?) :
        LinearCustomView(context),
        SocketSelectCtrl {

        private lateinit var btnMap:HashMap<TextView,SocketSelect>

        private lateinit var eMovingBtn: AutoSizeTextView
        private lateinit var  pbgnBtn: AutoSizeTextView
        private lateinit var  ionexBtn: AutoSizeTextView
        private lateinit var  homeBtn: AutoSizeTextView

        override fun initInFlaterView() {
            eMovingBtn = findViewById(R.id.eMovingBtn)
            pbgnBtn = findViewById(R.id.pbgnBtn)
            ionexBtn = findViewById(R.id.ionexBtn)
            homeBtn = findViewById(R.id.homeBtn)

            btnMap=hashMapOf(
                Pair(eMovingBtn,SocketSelect.eMoving),
                Pair(pbgnBtn,SocketSelect.PBGN),
                Pair(ionexBtn, SocketSelect.ionex),
                Pair(homeBtn,SocketSelect.Home)
            )
            btnMap.forEach {
                it.key.setOnClickListener {v->
                    selectBtn(it.key,!it.key.isSelected)
                    onSelectSocket(btnMap)
                }
            }
        }

        override val view: View
            get() = this

        override fun setSocketSelect(sockets: List<SocketSelect>) {
            btnMap.forEach {selectBtn(it.key,false)}
            btnMap.filter { pair-> sockets.any { s->pair.value==s } }
                .forEach {
                    selectBtn(it.key,true)
                }
        }
        override fun enable(b: Boolean) {
            btnMap.forEach { it.key.isEnabled=b }
        }
        override fun setInflatView(): Int = R.layout.item_socket_select_motor
        override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
    }

    private inner class CarSocketSelectView(context: Context?) :
        LinearCustomView(context),
        SocketSelectCtrl {

        private lateinit var btnMap:HashMap<TextView,SocketSelect>

        private lateinit var tesla_ac: AutoSizeTextView
        private lateinit var j1772_ac: AutoSizeTextView
        private lateinit var tesla_dc: AutoSizeTextView
        private lateinit var ccs1_j1772_dc: AutoSizeTextView
        private lateinit var chademo_dc: AutoSizeTextView

        override fun initInFlaterView() {
            tesla_ac = findViewById(R.id.tesla_ac)
            j1772_ac = findViewById(R.id.j1772_ac)
            tesla_dc = findViewById(R.id.tesla_dc)
            ccs1_j1772_dc = findViewById(R.id.ccs1_j1772_dc)
            chademo_dc = findViewById(R.id.chademo_dc)

            btnMap=hashMapOf(
                Pair(tesla_ac,SocketSelect.Tesla_ac),
                Pair(j1772_ac,SocketSelect.J1772),
                Pair(tesla_dc, SocketSelect.Tesla_dc),
                Pair(ccs1_j1772_dc,SocketSelect.CCS1),
                Pair(chademo_dc, SocketSelect.CHAdeMO)
            )
            btnMap.forEach {
                it.key.setOnClickListener {v->
                    selectBtn(it.key,!it.key.isSelected)
                    onSelectSocket(btnMap)
                }
            }
        }

        override val view: View
            get() = this

        override fun setSocketSelect(sockets: List<SocketSelect>) {
            btnMap.forEach {selectBtn(it.key,false)}
            btnMap.filter { pair-> sockets.any { s->pair.value==s } }
                .forEach {
                    selectBtn(it.key,true)
                }
        }

        override fun enable(b: Boolean) {
            btnMap.forEach { it.key.isEnabled=b }
        }

        override fun setInflatView(): Int =R.layout.item_socket_select_car
        override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
    }

    private interface SocketSelectCtrl{
        val view:View
        fun setSocketSelect(sockets:List<SocketSelect>)
        fun enable(b:Boolean)
    }
}