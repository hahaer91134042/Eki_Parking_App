package com.eki.parking.View.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.eki.parking.Model.DTO.OpenSet
import com.eki.parking.Model.EnumClass.OrderStatus
import com.eki.parking.Model.impl.ITimeConvert
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView
import com.eki.parking.View.abs.RelativeCustomView
import com.eki.parking.extension.color
import com.eki.parking.extension.screenWidth
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.DateUnit
import com.hill.devlibs.time.TimeUnit
import com.hill.devlibs.time.ext.formateShort

/**
 * Created by Hill on 2020/05/11
 */
class DayHour24ScaleListView(context: Context, attrs: AttributeSet?) : NestedScrollView(context, attrs) {

    private var parentView = HourScaleList(context).also { it.id=R.id.parentView }

    init {
        addView(parentView)
    }

    private var barList=ArrayList<OpenTimeScaleBar>()
    private var labelList=ArrayList<ReservaLabel>()

    var onLabelClick:((EkiOrder)->Unit)?=null
    var onOrderCancel:((EkiOrder)->Unit)?=null

    private lateinit var selectTime:DateUnit

    fun setOrder(orders: List<EkiOrder>, selectTime: DateUnit) {
        this.selectTime = selectTime
        labelList.forEach { parentView.removeView(it) }
        labelList.clear()
        orders.filter { it.orderStatus != OrderStatus.Cancel && it.orderStatus != OrderStatus.CancelByManager }
            .forEach { order ->

                var label =
                    ReservaLabel(context, ReservaLabelSet(order, getAnchorTime(order.ReservaTime)))
                label.setOnClickListener {
                    onLabelClick?.invoke(order)
                }
                label.cancelBtn.setOnClickListener {
                    onOrderCancel?.invoke(order)
                }

                labelList.add(label)
                parentView.addView(label)

            }
    }

    fun setOpenBar(opens: List<OpenSet>) {
        barList.forEach {parentView.removeView(it)}
        barList.clear()
//        var hourScale = parentView.hourScale
//        Log.w("AddOpenBar start child count->${parentView.childCount}")
        opens.forEach { open ->

            var bar=OpenTimeScaleBar(context, ScaleBarSet(getAnchorTime(open)))
            barList.add(bar)
            parentView.addView(bar)
        }
    }

    private fun getAnchorTime(t: ITimeConvert): List<AnchorTime> {
        var hourScale = parentView.hourScale
        var start = if (selectTime == t.startDateTime().date) {
            t.startDateTime().time
        } else {
            TimeUnit(0,0,0)
        }
        var end = t.let {
            if (it.endDateTime().date > it.startDateTime().date) {
                if (selectTime != it.startDateTime().date) {
                    return@let it.endDateTime().time
                }
                return@let TimeUnit(24, 0, 0)
            }
            return@let it.endDateTime().time
        }

        return hourScale.filter { start.hour <= it.hour && it.hour <= end.hour }
                .map { h ->

                    var hour = h.hour
                    var l=h.minScales.filter {
                        var s = TimeUnit(hour, it.startMin, 0)
                        var e = TimeUnit(hour, it.endMin, 0).refresh()
                        start <= s && e <= end
                    }
//                    Log.w("map list size->${l.size}")

                    AnchorTime(hour,l)
                }.filter { it.mins.isNotEmpty() }
    }

    private class HourScaleList(context: Context?) : RelativeCustomView(context) {

        var hourScale:ArrayList<HourScaleView.HourScale> = itemView.findViewById<HourScaleView>(R.id.hourScaleView).hourScale

        init {

        }

        override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
        override fun setInflatView(): Int =R.layout.item_24_hour_list

    }

    private class ReservaLabel(context: Context?,var set:ReservaLabelSet) : LinearCustomView(context) {
        var timeSpan=itemView.findViewById<TextView>(R.id.timeSpanText)
        var license=itemView.findViewById<TextView>(R.id.licenseNum)
        var cancelBtn=itemView.findViewById<ImageButton>(R.id.cancelBtn)

        init {
            var lp=RelativeLayout.LayoutParams(set.labelW.toInt(),set.labelH.toInt())
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            lp.topMargin=set.marginTop
            lp.rightMargin=set.marginEnd
            layoutParams=lp

            timeSpan.text="${set.startTime.formateShort()}-${set.endTime.formateShort()} (${getString(R.string.Total_mins_formate).messageFormat((set.endTime-set.startTime).totalMinutes)})"
            license.text=set.order.CarNum

            var start=set.order.ReservaTime.startDateTime()
            var now=DateTime.now()
            if (now>=start)
                cancelBtn.visibility=View.INVISIBLE


        }

        override fun setInflatView(): Int =R.layout.item_reserva_label
        override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
        companion object{
            const val marginEnd=0
        }
    }

    private data class ReservaLabelSet(val order:EkiOrder,val anchors: List<AnchorTime>){
        var startTime:TimeUnit
        var endTime:TimeUnit
        var labelW= 0f
        var labelH=0f
        var marginTop=0
        var marginEnd=ReservaLabel.marginEnd
        init {
            var sortAnchors=anchors.sortedBy { it.hour }

            var startMin= sortAnchors.first().mins.minByOrNull { it.startMin }!!
            var endMin= sortAnchors.last().mins.maxByOrNull { it.startMin }!!
            startTime=TimeUnit(sortAnchors.first().hour,startMin.startMin,0)

            endTime=TimeUnit(sortAnchors.last().hour,endMin.endMin,0).refresh()


            marginTop= startMin.startY.toInt()

            var pX=startMin.startX+OpenTimeScaleBar.barOffsetX+OpenTimeScaleBar.barWidth
            labelW=startMin.endX-pX
            sortAnchors.forEach {a->
                var minScale=a.mins.sortedBy { it.startMin }
                labelH += (minScale.last().endY - minScale.first().startY)
            }
        }
    }


    private class OpenTimeScaleBar(context: Context?,var set:ScaleBarSet) : View(context) {
        init {
            id=set.hashCode()
            setBackgroundColor(set.color)
            var lp=RelativeLayout.LayoutParams(set.barW.toInt(),set.barH.toInt())
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            lp.topMargin=set.marginTop
            lp.leftMargin=set.marginStart

            layoutParams=lp
        }

        companion object{
            val barOffsetX= screenWidth()/16f
            val barWidth= screenWidth()/15f
        }
    }
    private data class ScaleBarSet(var anchors: List<AnchorTime>){
        var color= color(R.color.Eki_green_3)
        var barW= OpenTimeScaleBar.barWidth
        var barH=0f
        var marginTop=0
        var marginStart=0

        init {
            var sortAnchors=anchors.sortedBy { it.hour }

            var startMin= sortAnchors.first().mins.minByOrNull { it.startMin }!!


            marginStart=(startMin.startX+OpenTimeScaleBar.barOffsetX).toInt()
            marginTop= startMin.startY.toInt()

            sortAnchors.forEach {a->
                var minScale=a.mins.sortedBy { it.startMin }
                barH += (minScale.last().endY - minScale.first().startY)
            }
        }
    }

    private data class AnchorTime(val hour:Int,val mins:List<HourScaleView.MinScale>)
}