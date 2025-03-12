package com.eki.parking.View.popup

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.eki.parking.AppConfig
import com.eki.parking.Controller.filter.TimeOverlapFilter
import com.eki.parking.Model.DTO.OpenSet
import com.eki.parking.R
import com.eki.parking.View.abs.EkiPopupWindow
import com.eki.parking.View.libs.StateButton
import com.eki.parking.View.recycleview.BaseRecycleView
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.extension.asWeekStr
import com.eki.parking.extension.setList
import com.eki.parking.extension.string
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.TimeUnit
import com.hill.devlibs.time.ext.*
import com.hill.devlibs.util.SimpleAnimationUtils
import com.shawnlin.numberpicker.NumberPicker

/**
 * Created by Hill on 2020/03/26
 */
class OpenTimeSelectPopup(var from:DateTime,private var dayOpen:List<OpenSet>,context: Context?) : EkiPopupWindow(context) {

    private var startTime=DateTime().set(from.date)
    private var startTimePicker: NumberPicker =contentView.findViewById(R.id.startTimePicker)
    private var endTimePicker: NumberPicker =contentView.findViewById(R.id.endTimePicker)
    private var selectBtn:Button=contentView.findViewById(R.id.selectBtn)
    private var openTimeList:BaseRecycleView=contentView.findViewById(R.id.openTimeList)
    private var closeBtn:View=contentView.findViewById(R.id.cancelBtn)
    private var dateText:TextView=contentView.findViewById(R.id.dateText)
    private var errorMsg=contentView.findViewById<TextView>(R.id.errorMsg)
    private var repeatCheck: CheckBox =contentView.findViewById(R.id.repeatTimeCheck)

    var onOpenSelect:(List<OpenSet>)->Unit={}
//    var onSameWeekSelect:(List<OpenSet>)->Unit={}

    private val timeStrList:ArrayList<TimeStringPair> by lazy {
        var list=ArrayList<TimeStringPair>()
        var offSet= AppConfig.openOffsetMin.minSpan
        var start=startTime.time
        var end=TimeUnit(24,0,0)

        do {
//            Log.i("list start time->$start")
            if (start<end)//還是在同一天
                list.add(TimeStringPair(start,start.formateShort()))
            else//跑到底了
                list.add(TimeStringPair(end,end.formateShort()))
            start += offSet
        }while (end>=start)

        list
    }

    var startSelect=startTime.time
    var endSelect=startTime.time


    override fun setUpView() {
        dateText.text=
                "${string(R.string.month_day_format).messageFormat(startTime.month.mod02d(),startTime.day.mod02d())} (${startTime.weekDay.asWeekStr})"

        openTimeList.useHorizonView()
        openTimeList.adapter=OpenTimeAdaptor()
        closeBtn.setOnClickListener {
            dismiss()
        }

        var list=timeStrList.map { it.str }

//        Log.w("Popup dayOpen size->${dayOpen.size}")
//        dayOpen.forEach {
//            it.printValue()
//        }

        startTimePicker.setList(list)
        endTimePicker.setList(list)

        startTimePicker.setOnValueChangedListener { picker, oldVal, newVal ->
//            Log.d("StartTimePicker oldVal->$oldVal newVal->$newVal")
//            startSelect=newVal
            startSelect=timeStrList[newVal].time
            errorMsg.text=""
        }
        endTimePicker.setOnValueChangedListener { picker, oldVal, newVal ->
//            Log.d("EndTimePicker oldVal->$oldVal newVal->$newVal")
//            endSelect=newVal
            endSelect=timeStrList[newVal].time
            errorMsg.text=""
        }


        selectBtn.setOnClickListener {
            if (endSelect>startSelect){
                var totalMin=(endSelect-startSelect).totalMinutes
//                Log.w("select total min->$totalMin")

                if (totalMin<AppConfig.openGapMin){
                    showGapLagerThan30Min()
                    return@setOnClickListener
                }

                var open=OpenSet().also {
                    it.Date=startTime.date.toString()
                    it.StartTime=startSelect.toString()
                    it.EndTime=endSelect.toString()
                }

                var openList=when(repeatCheck.isChecked){
                    true-> open.sameWeekOpenTime()
                    else-> arrayListOf(open)
                }

//                openList.forEach { it.printValue() }

//                Log.d("open start->${open.startDateTime()}")
//                Log.w("same->${open.startDateTime().sameWeekDay}")
//                open.printValue()

//                when(repeatCheck.isChecked){
//                    true->{
//                        var now=DateTime()
//                        onSameWeekSelect(open.startDateTime().sameWeekDay
//                                .filter {it.date >= now.date }
//                                .map {
//                                    OpenSet().apply {
//                                        Date = it.date.toString()
//                                        StartTime = startSelect.toString()
//                                        EndTime = endSelect.toString()
//                                    }
//                                })
//                    }
//                    else->onOpenSelect(arrayListOf(open))
//                }



                if(checkTimeOverlap(openList)){
                    onOpenSelect(openList)
                    dismiss()
                }else
                    showTimeOverlap()
//                    context.showToast(string(R.string.Time_overlap))

            }else
                errorMsg.text=string(R.string.End_time_must_be_greater_than_start_time)
//                context.showToast(context.getString(R.string.End_time_must_be_greater_than_start_time))
        }
    }

    private fun showGapLagerThan30Min(){
        errorMsg.text= string(R.string.Must_be_open_for_at_least_30_minutes)
    }

    private fun showTimeOverlap(){
        errorMsg.text=string(R.string.This_time_period_overlap_cannot_be_added_please_select_again)
    }

    private fun checkTimeOverlap(openList: List<OpenSet>): Boolean {
        var filter=TimeOverlapFilter(from,dayOpen)
        var result=!openList.any { !filter.filter(it) }
//        Log.i("select add time start->${open.startDateTime(from)} end->${open.endDateTime(from)}")
//        Log.d("is add Time overlap result->$result ")

        return result
    }

    private fun OpenSet.sameWeekOpenTime():List<OpenSet>{
        var open=this
        var startList=open.startDateTime().sameWeekDay
//        var endList=open.endDateTime().sameWeekDay
        return ArrayList<OpenSet>().also { list->
            startList.filter { it.date>=DateTime.now().date }.forEach { start->
//                var end=endList.first { it.date==start.date }
                list.add(OpenSet().apply {
                    Date=start.date.toString()
                    StartTime=open.StartTime
                    EndTime=open.EndTime
                })
            }
        }
    }

    private inner class OpenTimeAdaptor() : BaseAdapter<OpenItem>(context) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenItem =
                OpenItem(getItemView(R.layout.item_open_time2,parent)).also { it.init() }

        override fun onBindViewHolder(item: OpenItem, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(dayOpen[position])
        }

        override fun getItemCount(): Int =dayOpen.size
    }

    private inner class OpenItem(itemView: View) : ItemLayout<OpenSet>(itemView) {

        private var openBtn=itemView.findViewById<StateButton>(R.id.openTimeBtn)

        override fun init() {

        }

        override fun refresh(data: OpenSet?) {
            super.refresh(data)
            data.notNull { set->
                var start=set.startDateTime(from)
                var end=set.endDateTime(from)

                openBtn.text="${start.time.formateShort()} - ${when {
                        start.date<end.date -> "24:00"
                        else -> end.time.formateShort()
                    }}"
            }
        }
    }

    override fun popGravity(): Int =Gravity.BOTTOM
    override fun blurBackground(): Boolean =false
    override fun layoutRes(): Int = R.layout.popup_open_time_picker
    override fun closeAnim(): Animation? = SimpleAnimationUtils.bottomDown()
    override fun showAnim(): Animation? = SimpleAnimationUtils.bottomPopUp()
    private data class TimeStringPair(val time:TimeUnit,val str:String)

}