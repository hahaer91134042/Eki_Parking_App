package com.eki.parking.Controller.calendar

import com.eki.parking.AppConfig
import com.eki.parking.Controller.impl.ICalendarMarker
import com.eki.parking.Controller.listener.CalendarMultiSelectListener
import com.eki.parking.Controller.listener.CalendarSelectListener
import com.eki.parking.extension.toJavaCalendar
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.hill.calendarview.multi.CustomMultiMonthView
import com.hill.calendarview.multi.CustomMultiWeekView
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.*
import java.util.*


/**
 * Created by Hill on 03,10,2019
 *
 */
abstract class EkiCalendar<VIEW,DATA> {
    companion object{
        fun loadMulti(calendar: CalendarView):EkiCalendar<CalendarView,Calendar?> =
                HibinCalendarImpl().creat(calendar)

        fun loadSimple(calendar: CalendarView):EkiCalendar<CalendarView,Calendar?> =
                HibinSimpleCalendarImpl().creat(calendar)
    }

    var calendarView:VIEW?=null
    var listeners=ArrayList<CalendarSelectListener<DATA>>()
    var multiListener:CalendarMultiSelectListener<DATA>?=null
    //在multi select 模式下是 now time
    var selectTime:DATA?=null
    var selectDate:(()->ArrayList<DATA>)?=null

    abstract fun creat(calendar: VIEW):EkiCalendar<VIEW,DATA>
//    abstract fun setSelectDate(date:Date)
    abstract fun scrollToPre()
    abstract fun scrollToNext()
    abstract fun scrollToNow()
    abstract fun scrollTo(year:Int,month:Int,day:Int)

    fun addDateSelectListener(l:CalendarSelectListener<DATA>){
        listeners.add(l)
    }
    fun removeDateSelectListener(l:CalendarSelectListener<DATA>){
        listeners.remove(l)
    }

    fun setMultiSelectListener(l:CalendarMultiSelectListener<DATA>){
        multiListener=l
    }

    abstract fun <M:ICalendarMarker> addCalendarMarker(mList:List<M>)

    abstract fun onResume(view:VIEW?)
    abstract fun onPause()

    /**
     * 使用說明
     * https://github.com/huanghaibin-dev/CalendarView/blob/master/QUESTION_ZH.md
     */
    private class HibinSimpleCalendarImpl:EkiCalendar<CalendarView,Calendar?>(),
                                            CalendarView.OnCalendarSelectListener,
                                            CalendarView.OnYearChangeListener{


        override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
//            Log.w("Impl onCalendarSelect ->$calendar Click->$isClick")
            selectTime=calendar
            listeners.forEach {
                it.onCalendarSelect(calendar, isClick)
            }
        }

        override fun onCalendarOutOfRange(calendar: Calendar?) {
            listeners.forEach {
                it.onCalendarOutOfRange(calendar)
            }
        }

        override fun onYearChange(year: Int) {
            listeners.forEach {
                it.onYearChange(year)
            }
        }

        override fun creat(calendar: CalendarView): EkiCalendar<CalendarView, Calendar?> {

//            var now=DateTime()
//            calendar.setSelectStartCalendar(now.date.year,now.date.month,now.date.day)


            selectTime=calendar.selectedCalendar
//            Log.w("HibinSimpleCalendarImpl creat selectTime->$selectTime")

            //這邊有bug week view會沒辦法load 只能設定在xml上面
//            calendar.setWeekBar(SimpleWeekView::class.java)
//            calendar.setMonthView(SimpleMonthView::class.java)


//            calendar.setOnYearChangeListener(this)
//            calendar.setOnCalendarSelectListener(this)
//
//
//            selectTime.notNull {
//                var end= it.toJavaCalendar()
//                end.addDay(AppConfig.maxReservaDay)
//                calendar.setRange(it.year, it.month, it.day,
//                        end.getYear(),end.getMonth(), end.getDay())
//            }
//
//            //multi select 會跑掉所以要規0
//            calendar?.scrollToCurrent(true)

            calendarView=calendar



            return this
        }


        override fun onResume(view:CalendarView?) {
//            Log.i("HibinSimpleCalendarImpl resume->$calendarView")
            calendarView=view
            //這邊不知為何 frag在新生成的view calendar會跑掉
//            selectTime=view?.selectedCalendar
//            Log.w("HibinSimpleCalendarImpl resume selectTime->$selectTime")

            calendarView?.setOnYearChangeListener(this)
            calendarView?.setOnCalendarSelectListener(this)

            val now=DateTime()
            val end=now+AppConfig.maxReservaDay.daySpan

            calendarView?.setRange(now.date.year, now.date.month,now.date.day,
                    end.date.year,end.date.month, end.date.day)
//            selectTime.notNull {
//                var end= it.toJavaCalendar()
//                end.addDay(AppConfig.maxReservaDay)
//                calendarView?.setRange(it.year, it.month, it.day,
//                        end.getYear(),end.getMonth(), end.getDay())
//            }

//            calendarView.sche
            //multi select 會跑掉所以要規0
            calendarView?.scrollToCurrent(true)
        }

        override fun onPause() {
            calendarView=null
        }

        override fun scrollToPre() {
            calendarView?.scrollToPre(true)
        }

        override fun scrollToNext() {
            calendarView?.scrollToNext(true)
        }

        override fun scrollToNow() {
            calendarView?.scrollToCurrent(true)
        }
        override fun scrollTo(year: Int, month: Int, day: Int) {
            calendarView?.scrollToCalendar(year,month,day)
        }

        override fun <M : ICalendarMarker> addCalendarMarker(mList: List<M>) {
            calendarView?.clearSchemeDate()
            val map: MutableMap<String, Calendar?> = HashMap()
            mList.forEach { marker->
                val time=marker.time
                val calendar = Calendar()
                calendar.year = time.year
                calendar.month = time.month
                calendar.day = time.day
                calendar.schemeColor = marker.color
                calendar.scheme = marker.text
                calendar.addScheme(Calendar.Scheme())
                map[calendar.toString()]=calendar
            }
            calendarView?.setSchemeDate(map)
        }
    }


    private class HibinCalendarImpl:EkiCalendar<CalendarView,Calendar?>(),
                                    CalendarView.OnCalendarSelectListener,
                                    CalendarView.OnYearChangeListener,
                                    CalendarView.OnCalendarMultiSelectListener{
        override fun onResume(view:CalendarView?) {
            //目前MultiSelect不會遇到切換Fragment的問題
            //假如有需要再實作這邊
        }

        override fun onPause() {

        }

//        private var schemeMap=LinkedHashMap<String,Calendar?>()

        override fun onMultiSelectOutOfSize(calendar: Calendar?, maxSize: Int) {
//            Log.d("onMultiSelectOutOfSize ->$calendar")
            multiListener?.onMultiSelectOutOfSize(calendar,maxSize)
        }

        override fun onCalendarMultiSelectOutOfRange(calendar: Calendar?) {
//            Log.w("onCalendarMultiSelectOutOfRange->$calendar")
            multiListener?.onCalendarMultiSelectOutOfRange(calendar)
        }

        override fun onCalendarMultiSelect(calendar: Calendar?, curSize: Int, maxSize: Int) {
//            Log.i("onCalendarMultiSelect ->$calendar  size->$curSize max->$maxSize")
            multiListener?.onCalendarMultiSelect(calendar,curSize,maxSize)
        }


        override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
//            Log.d("onCalendarSelect ->$calendar Click->$isClick")
            selectTime=calendar
            listeners.forEach {
                it.onCalendarSelect(calendar, isClick)
            }
        }

        override fun onCalendarOutOfRange(calendar: Calendar?) {
            listeners.forEach {
                it.onCalendarOutOfRange(calendar)
            }
        }

        override fun onYearChange(year: Int) {
            listeners.forEach {
                it.onYearChange(year)
            }
        }

//        private var allowDate:AllowDate?=null
        override fun creat(calendar: CalendarView): EkiCalendar<CalendarView,Calendar?> {
            calendarView=calendar
            selectTime=calendarView?.selectedCalendar
            calendarView?.setMonthView(CustomMultiMonthView::class.java)
            calendarView?.setWeekBar(CustomMultiWeekView::class.java)


            calendarView?.setOnYearChangeListener(this)
            calendarView?.setOnCalendarSelectListener(this)
            calendarView?.setOnCalendarMultiSelectListener(this)

//            calendarView?.setOnCalendarInterceptListener(object :CalendarView.OnCalendarInterceptListener{
//                override fun onCalendarInterceptClick(calendar: Calendar?, isClick: Boolean) {
//
//                }
//
//                override fun onCalendarIntercept(calendar: Calendar?): Boolean {
//                    return false
////                    return allowDate?.isAllow(calendar?.year,calendar?.month,calendar?.day)?:true
//                }
//            })


//            Log.d("EkiCalendar allowDate-> ${allowDate}")
            val end= selectTime?.toJavaCalendar()
            end?.addDay(AppConfig.maxReservaDay)
//            end?.addMonth(AppConfig.maxReservaDay)

//            Log.w("end time year->${end?.getYear()} month->${end?.getMonth()} day->${end?.getDay()}")
            calendarView?.setRange(selectTime?.year!!, selectTime?.month!!, selectTime?.day!!,
                    end?.getYear()!!,end.getMonth(), end.getDay())



            //multi select 會跑掉所以要規0
            calendarView?.scrollToCurrent(true)


            return this
        }

        override fun <M : ICalendarMarker> addCalendarMarker(mList: List<M>) {
            val map: MutableMap<String, Calendar?> = HashMap()
            mList.forEach { marker->
                val time=marker.time
                val calendar = Calendar()
                calendar.year = time.year
                calendar.month = time.month
                calendar.day = time.day
                calendar.schemeColor = marker.color //如果单独标记颜色、则会使用这个颜色
                calendar.scheme = marker.text
                calendar.addScheme(Calendar.Scheme())
                map[calendar.toString()]=calendar
            }
            calendarView?.setSchemeDate(map)
        }

//        override fun setSelectDate(date: Date) {
//            var resC=date.toCalendar()
//            var c=Calendar()
//            c.year=resC.getYear()
//            c.month=resC.getMonth()
//            c.day=resC.getDay()
//            c.scheme="test"
//            //c.addScheme()
//            schemeMap[c.toString()] = c
//            Log.i("setSelectDate->$c")
//
////            calendarView?.addSchemeDate(schemeMap)
//
//        }

        override fun scrollToPre() {
            calendarView?.scrollToPre(true)
        }

        override fun scrollToNext() {
            calendarView?.scrollToNext(true)
        }

        override fun scrollToNow() {
            calendarView?.scrollToCurrent(true)
        }
        override fun scrollTo(year: Int, month: Int, day: Int) {
            calendarView?.scrollToCalendar(year,month,day)
        }
    }

    private data class AllowDate(val start: Date, var end:Date){
        fun isAllow(year:Int?,month:Int?,day:Int?):Boolean{
            val now=Date(year?:0,month?:0,day?:0).time
            return start.time<now && now<end.time
        }
    }

//    private data class Date(val year:Int,val month:Int,val day:Int)
}