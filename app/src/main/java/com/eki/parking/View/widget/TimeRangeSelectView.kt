package com.eki.parking.View.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eki.parking.AppConfig
import com.eki.parking.Model.DTO.TimeRange
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView
import com.eki.parking.View.recycleview.BaseRecycleView
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.extension.color
import com.eki.parking.extension.isBetween
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.TimeUnit
import com.hill.devlibs.time.ext.minSpan
import com.hill.devlibs.tools.Log
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Hill on 2021/11/18
 */
class TimeRangeSelectView(context: Context?, attrs: AttributeSet?) :
    BaseRecycleView(context, attrs) {

    init {
        useSimpleDivider()
    }

    private var offsetMin = AppConfig.ReservaTimeOffsetMin.minSpan

    private var dataList = ArrayList<ViewData>()

    private lateinit var rangeStart: DateTime
    private lateinit var rangeEnd: DateTime

    private var selectStart: DateTime? = null

    var onSelectTimeEvent: OnSelectTimeEvent? = null

    //記錄使用者已經選擇的時間
    private var userSelectRange:TimeRange?=null

    fun selectTimeRange(start: DateTime, end: DateTime) {

        //要注意會有最大道24:00:00 要處理
//        Log.d("view start->$start end->$end")
//        if(start.date!=end.date&&end>start)
//            throw IllegalArgumentException("Time must in the same date and end>start!")//必須同一天才能 目前不支援跨日

//        var end=end
//
//        if(end.date>start.date)
//            end= DateTime(start.date,TimeUnit(24,0,0))

        dataList.clear()

        rangeStart = start
        rangeEnd = end
//        Log.w("rangeEnd->${rangeEnd}")

        var datas = ArrayList<TimeUnit>()
        var sTime = start.time
        var eTime = when {
            end.date > start.date -> TimeUnit(24,0,0)
            else -> end.time
        }

//        Log.i("start hour->${sTime.hour} end hour->${eTime.hour}")

        for (hour in sTime.hour..eTime.hour) {
            var hourTime = TimeUnit(hour, 0, 0)
            do {
//                Log.i("hour time->${hourTime}")
                if (hourTime >= sTime && hourTime < eTime) {

//                    Log.i("hour time->${hourTime}")
                    //dataList.add(ViewData())
                    datas.add(hourTime)
                }

                hourTime += offsetMin

            } while (hourTime < TimeUnit(hour + 1,0,0) && hourTime<eTime)

        }

        for (pair in datas.groupBy { it.hour }) {
            dataList.add(ViewData(pair.key, pair.value))
        }

//        Log.d("dataList->${dataList.toJsonStr()}")
        this.adapter = HourTimeAdaptor(context)

    }

    fun setSelectStart(start: DateTime) {
        if (start.date != rangeStart.date)
            throw IllegalArgumentException("Select Date must eqal range start Date !!")
        selectStart = start
    }

    private var onStartClick: (DateTime) -> Unit = { start ->
//        var s = DateTime(rangeStart.date, start)
        setSelectStart(start)
        onSelectTimeEvent?.onSelectStart(start)
    }

    //接收所有的EndClick
    private var onEndClick: (DateTime) -> Unit = { end ->
        //Log.w("end click time->${end}")
        userSelectRange= TimeRange(selectStart!!,end)
        dataList.forEach {
            //把事件傳遞回各個view來畫標記
            it.hourView?.onSelectEnd(end)
        }
        onSelectTimeEvent?.onSelectEnd(end)
    }

    private inner class HourTimeAdaptor(context: Context?) :
        BaseAdapter<HourTimeSpanLayout>(context) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
                HourTimeSpanLayout = HourTimeSpanLayout(HourTimeView(context))
                .also { it.init() }

        override fun onBindViewHolder(item: HourTimeSpanLayout, position: Int) {
            super.onBindViewHolder(item, position)

            item.refresh(dataList[position])
        }

        override fun getItemCount(): Int = dataList.size

    }

    private inner class HourTimeSpanLayout(itemView: HourTimeView) : ItemLayout<ViewData>(itemView) {

        private var hourView = itemView

        override fun init() {
            super.init()
        }

        override fun refresh(data: ViewData?) {
            super.refresh(data)
            data.notNull {
                hourView.showData(it)
            }
        }

    }

    private inner class HourTimeView(context: Context?) : LinearCustomView(context) {

        lateinit var hourText: TextView
        lateinit var min_0_15: View
        lateinit var min_15_30: View
        lateinit var min_30_45: View
        lateinit var min_45_60: View

        private lateinit var data: ViewData
        var viewBindList = ArrayList<TimeViewBind>()
        private lateinit var viewList:List<View>

        override fun initInFlaterView() {
            hourText = itemView.findViewById<TextView>(R.id.hourTex)
            min_0_15 = itemView.findViewById<View>(R.id.min_15)
            min_15_30 = itemView.findViewById<View>(R.id.min_30)
            min_30_45 = itemView.findViewById<View>(R.id.min_45)
            min_45_60 = itemView.findViewById<View>(R.id.min_60)
            viewList= listOf(min_0_15,min_15_30,min_30_45,min_45_60)
        }

        //可能有改進空間
        //之後改成動態加入
        fun showData(data: ViewData) {
            this.data = data
            viewBindList.clear()
            hourText.text = data.hour.toString()
            //把事件串接起來
            data.hourView = this
            //這邊是把傳入的時間當作區間的開始時間
            data.times.forEach { time ->
                var start=DateTime(rangeStart.date,time)
//                Log.d("view time start->$start")
                var end = start + offsetMin
                when (time.min) {
                    0 -> {
                        min_0_15.setOnClickListener {
                            Log.w("click time->${time}")
                            //data.selectTimeEvent?.onSelectEnd(end)
                            if (selectStart == null) {
                                onStartClick(start)
                            } else {
                                onEndClick(end)
                            }

                        }
                        viewBindList.add(TimeViewBind(min_0_15,start))
                    }
                    15 -> {
                        min_15_30.setOnClickListener {
                            Log.w("click time->${time}")
                            //data.selectTimeEvent?.onSelectEnd(end)
                            if (selectStart == null) {
                                onStartClick(start)
                            } else {
                                onEndClick(end)
                            }
                        }
                        viewBindList.add(TimeViewBind(min_15_30,start))

                    }
                    30 -> {
                        min_30_45.setOnClickListener {
                            Log.w("click time->${time}")
                            //data.selectTimeEvent?.onSelectEnd(end)
                            if (selectStart == null) {
                                onStartClick(start)
                            } else {
                                onEndClick(end)
                            }
                        }
                        viewBindList.add(TimeViewBind(min_30_45,start))
                    }
                    45 -> {
                        min_45_60.setOnClickListener {
                            Log.w("click time->${time}")
                            //data.selectTimeEvent?.onSelectEnd(end)
                            if (selectStart == null) {
                                onStartClick(start)
                            } else {
                                onEndClick(end)
                            }
                        }
                        viewBindList.add(TimeViewBind(min_45_60,start))
                    }
                }
            }

            //可能是因為recycleview有cache?所以這邊要先指定成預設的顏色
            viewList.forEach {
                it.setBackgroundColor(color(R.color.light_gray7))
            }
            viewBindList.forEach {
                initColor(it.view,it.time)
            }

        }

        private fun initColor(view:View,time:DateTime){
//            view.setBackgroundColor(color(R.color.color_white))
            if(userSelectRange!=null){
                if(userSelectRange?.isBetween(time) == true){
                    view.setBackgroundColor(color(R.color.Eki_orange_1))
                }else{
                    view.setBackgroundColor(color(R.color.color_white))
                }
            }else{
                view.setBackgroundColor(color(R.color.color_white))
            }
        }


        override fun setInflatView(): Int = R.layout.item_hour_time
        override fun initNewLayoutParams(): ViewGroup.LayoutParams? = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        fun onSelectEnd(end: DateTime) {
            //Log.w("on select end->$end view->$this")
            selectStart.notNull { start ->
                viewBindList.filter { it.time < start }
                    .forEach { it.view.setBackgroundColor(color(R.color.color_white)) }

                viewBindList.filter { it.time >= start && it.time < end }
                    .forEach {
                        it.view.setBackgroundColor(color(R.color.Eki_orange_1))
                        //Log.i("valid view time->${it.time}")
                    }
                viewBindList.filter { it.time >= end }
                    .forEach { it.view.setBackgroundColor(color(R.color.color_white)) }
            }
        }


    }

    private data class TimeViewBind(val view: View, val time: DateTime)

    private data class ViewData(val hour: Int, val times: List<TimeUnit>) {
        var hourView: HourTimeView? = null
    }

    //
    interface OnSelectTimeEvent {
        fun onSelectStart(start: DateTime)
        fun onSelectEnd(end: DateTime)
    }
}