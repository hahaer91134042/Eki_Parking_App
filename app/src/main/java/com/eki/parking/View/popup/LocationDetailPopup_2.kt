package com.eki.parking.View.popup

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import com.eki.parking.Controller.tools.FindMarker
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.Model.EnumClass.LocAvailable
import com.eki.parking.Model.sql.EkiLocation
import com.eki.parking.R
import com.eki.parking.View.abs.EkiPopupWindow
import com.eki.parking.View.recycleview.BaseRecycleView
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.View.widget.AutoLoadImgView
import com.eki.parking.extension.color
import com.eki.parking.extension.currencyCost
import com.eki.parking.extension.string
import com.hill.devlibs.EnumClass.SpanHelper
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.util.SimpleAnimationUtils
import com.hill.devlibs.util.StringUtil
import java.lang.StringBuilder

/**
 * Created by Hill on 2021/02/17
 */
class LocationDetailPopup_2(context: Context?) : EkiPopupWindow(context),
                                                 ISetData<ArrayList<EkiLocation>> {

    private var locList=ArrayList<EkiLocation>()
    var onReservaLocation:((EkiLocation)->Unit)?=null
    private var cancelBtn:ImageView=contentView.findViewById(R.id.cancelBtn)
    private var recycleView:BaseRecycleView=contentView.findViewById(R.id.recycleView)
    private var totalNumText:TextView=contentView.findViewById(R.id.totalNumText)

    init {
        cancelBtn.setOnClickListener {
            dismiss()
        }

        recycleView.useHorizonView()
    }

    override fun setUpView() {

        val builder=StringUtil.getSpanBuilder()
        builder.setSpanString("該地點共",SpanHelper.TYPE_NORMAL.setTextColor(color(R.color.text_color_1)))
                .setSpanString("${locList.size}",SpanHelper.TYPE_NORMAL.setTextColor(color(R.color.Eki_red_1)))
                .setSpanString("個車位",SpanHelper.TYPE_NORMAL.setTextColor(color(R.color.text_color_1)))
                .into(totalNumText)

        recycleView.adapter=LocationAdaptor()

    }

    private inner class LocationAdaptor : BaseAdapter<LocInfoItem>(context) {

        init {

            //loc = locList.select(locList[i].Lat, )


            setItemListClickListener { i->
                onReservaLocation?.invoke(locList[i])
                /*locList[i].Socket.forEach {
                    println("取得Adapter " + locList[i].Socket.size + " " + it.Charge + " " + it.Current)
                }*/
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocInfoItem {
            return LocInfoItem(getItemView(R.layout.item_location_info_card,parent))
                    .also { it.init() }
        }

        override fun onBindViewHolder(item: LocInfoItem, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(locList[position])
        }

        override fun getItemCount(): Int = locList.size

//        override fun getItemId(position: Int): Long = position.hashCode().toLong()
//        override fun getItemViewType(position: Int): Int = position
    }


    private class LocInfoItem(itemView: View) : ItemLayout<EkiLocation>(itemView) {

        private var parent=itemView.findViewById<View>(R.id.parentView)
        private var img=itemView.findViewById<AutoLoadImgView>(R.id.imgView)
        private var chargeTypeImg=itemView.findViewById<ImageView>(R.id.chargeTypeImg)
        private var chargeTypeText=itemView.findViewById<TextView>(R.id.chargeTypeText)
        private var reservaImg=itemView.findViewById<ImageView>(R.id.reservaImg)
        private var reservaText=itemView.findViewById<TextView>(R.id.reservaText)
        private var priceText=itemView.findViewById<TextView>(R.id.priceTextView)

        override fun init() {
            parent.setOnClickListener {
                itemClick()
            }
        }

        override fun refresh(data: EkiLocation?) {
            super.refresh(data)
            data.notNull { loc->
                img.loadUrl(loc.imgUrl(),true)

                chargeTypeImg.setImageResource(FindMarker.fromLoc(loc).res)

                chargeTypeText.text = StringBuilder().append(when (loc.current) {
                    CurrentEnum.AC -> string(R.string.Alternating_current)
                    CurrentEnum.DC -> string(R.string.Direct_current)
                    else -> string(R.string.General)
                })

                when(loc.available){
                    LocAvailable.Available->{
                        reservaText.text= string(R.string.Available_for_appointment)
                        reservaImg.setImageResource(R.drawable.shape_circle_green2)
                    }
                }

                priceText.text= string(R.string.billing_method_30min)
                        .messageFormat(loc.currencyCost())
            }
        }
    }

    override fun setData(data: ArrayList<EkiLocation>?) {
        data.notNull { locList.addAll(it) }
    }
    override fun layoutRes(): Int = R.layout.popup_location_detail_2

    override fun popGravity(): Int = Gravity.BOTTOM
    override fun blurBackground(): Boolean =false
    override fun backEnable(): Boolean =true
    override fun outSideDismiss(): Boolean =false
    override fun showAnim(): Animation? = SimpleAnimationUtils.bottomPopUp()
    override fun closeAnim(): Animation? = SimpleAnimationUtils.bottomDown()
}