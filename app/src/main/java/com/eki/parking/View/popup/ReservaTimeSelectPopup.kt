package com.eki.parking.View.popup

import android.content.Context
import android.view.Gravity
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.Toast
import com.eki.parking.AppConfig
import com.eki.parking.Controller.tools.ReserveCheck
import com.eki.parking.Model.DTO.ReservaSet
import com.eki.parking.R
import com.eki.parking.View.abs.EkiPopupWindow
import com.eki.parking.View.libs.StateButton
import com.eki.parking.extension.setList
import com.eki.parking.extension.string
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.showToast
import com.hill.devlibs.extension.toDate
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.format
import com.hill.devlibs.time.ext.minSpan
import com.hill.devlibs.util.SimpleAnimationUtils
import com.shawnlin.numberpicker.NumberPicker

/**
 * Created by Hill on 05,11,2019
 */
class ReservaTimeSelectPopup(context: Context) : EkiPopupWindow(context) {

    private var cancelBtn: ImageView = contentView.findViewById(R.id.cancelBtn)
    private var startDatePicker: NumberPicker = contentView.findViewById(R.id.startDatePicker)
    private var startTimePicker: NumberPicker = contentView.findViewById(R.id.startTimePicker)
    private var endDatePicker: NumberPicker = contentView.findViewById(R.id.endDatePicker)
    private var endTimePicker: NumberPicker = contentView.findViewById(R.id.endTimePicker)
    private var reserveBtn: StateButton = contentView.findViewById(R.id.reserveBtn)

    lateinit var reserveList:ArrayList<ReservaSet>

    companion object {
        const val format = "HH:mm"
        const val dateFormat = "yyyy/MM/dd"
    }

    private var openDateList = listOf<String>()
    private val timeStrList: ArrayList<TimeStringPair> by lazy {

        val list = ArrayList<TimeStringPair>()
        val dateList = mutableListOf<String>()
        val offSet = AppConfig.ReservaTimeOffsetMin.minSpan

        var start = openTime.start
        val end = openTime.end

        do {
            list.add(TimeStringPair(start, start.format(format)))
            start += offSet

            if (!dateList.contains(start.date.format(dateFormat))) {
                dateList.add(start.date.format(dateFormat))
            }
            if (!dateList.contains(end.date.format(dateFormat))) {
                dateList.add(end.date.format(dateFormat))
            }
        } while (end > start)

        //補上最後的時間
        if ((end.date - openTime.start.date).day > 0) {
            //表示到24:00
            val zeroTime =
                DateTime(start.year, start.month, start.day, start.hour, start.min, start.sec)
            list.add(TimeStringPair(zeroTime, zeroTime.format(format)))
        } else {
            list.add(TimeStringPair(end, end.format(format)))
        }
        openDateList = dateList.sortedBy { it }

        list
    }
    lateinit var openTime: ReserveCheck.OpenTime
    lateinit var clickTime: DateTime

    private var startSelect = 0
    private var startDateSelect = 0
    private var endSelect = 0
    private var endDateSelect = 0

    //各起訖日期的開放時間清單
    var startTimeList = listOf<String>()
    var endTimeList = listOf<String>()

    var onReserve: ((start: DateTime, end: DateTime) -> Unit)? = null

    override fun setUpView() {
        cancelBtn.setOnClickListener {
            dismiss()
        }

        //---NumberPicker值的初始化設定---
        //當下日期的開放時間清單
        val list = timeStrList
            .filter { it.time.date.format(dateFormat) == openDateList[0] }.map { it.str }

        //點擊時間的格式檢查
        val checkMin = if (clickTime.min < 10) {
            "0${clickTime.min}"
        } else {
            clickTime.min.toString()
        }
        //點擊時間於開放時間清單下的index
        val initValue = list.indexOf("${clickTime.hour}:$checkMin")
        //避免若沒有滑動時間,時間更新錯誤
        startSelect = initValue
        endSelect = initValue

        startDatePicker.setList(openDateList)
        startTimePicker.setList(list)
        endDatePicker.setList(openDateList)
        endTimePicker.setList(list)

        startTimeList = list
        endTimeList = list
        startTimePicker.value = initValue
        endTimePicker.value = initValue
        //---NumberPicker值的初始化設定---

        setDatePicker()
    }

    private fun setDatePicker() {
        startDatePicker.setOnValueChangedListener{ picker, _, newIndex ->
            val filterList = timeStrList
                .filter { it.time.date.format(dateFormat) == openDateList[newIndex] }

            //若不先 reset,
            // 當 前 input list size < 後 input list size 時會閃退(ArrayIndexOutOfBoundsException)
            startTimePicker.displayedValues = null
            startTimeList = filterList.map { it.str }
            startTimePicker.setList(startTimeList)
            startDateSelect = newIndex
        }
        startTimePicker.setOnValueChangedListener { _, _, newVal ->
            startSelect = newVal
        }
        endDatePicker.setOnValueChangedListener { _, _, newIndex ->
            val filterList = timeStrList
                .filter { it.time.date.format(dateFormat) == openDateList[newIndex] }

            endTimePicker.displayedValues = null
            endTimeList = filterList.map { it.str }
            endTimePicker.setList(endTimeList)

            endDateSelect = newIndex
        }
        endTimePicker.setOnValueChangedListener { picker, _, newVal ->
            endSelect = newVal
        }

        reserveBtn.setOnClickListener {
            try {
                val startSelectTime =
                    DateTime(openDateList[startDateSelect].toDate(dateFormat)
                        ,startTimeList[startSelect].toDateTime(format).time)
                val endSelectTime = DateTime(openDateList[endDateSelect].toDate(dateFormat)
                    ,endTimeList[endSelect].toDateTime(format).time)

                if (startSelectTime >= endSelectTime) {
                    context.showToast(string(R.string.End_time_must_be_greater_than_start_time))
                } else {

                    if ((endSelectTime - startSelectTime) < AppConfig.ReservaGapMin.minSpan) {
                        context.showToast(
                            string(R.string.Reservation_space_must_be_greater_than_min)
                                .messageFormat(AppConfig.ReservaGapMin)
                        )
                    } else {
                        dismiss()
                        if (checkReserved(startSelectTime,endSelectTime)) {
                            onReserve?.invoke(startSelectTime, endSelectTime)
                        }
                    }
                }
            } catch (e:Exception) {
                Toast.makeText(context,
                    "Time Error, please choose other time or parking space",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    //再次確認選取時間段有無和已預訂的時間重合
    private fun checkReserved(start: DateTime, end: DateTime):Boolean {
        val reserveFormat = "yyyy-MM-dd HH:mm:ss"
        reserveList.forEach {
            //預定起點 在 已預定時間段內
            if ( start > it.StartTime.toDateTime(reserveFormat) &&
                start < it.EndTime.toDateTime(reserveFormat)) {
                return false
            }
            //預定終點 在 已預定時間段內
            if ( end > it.StartTime.toDateTime(reserveFormat) &&
                end < it.EndTime.toDateTime(reserveFormat)) {
                return false
            }
        }
        return true
    }

    override fun outSideDismiss(): Boolean = false
    override fun blurBackground(): Boolean = false
    override fun popGravity(): Int = Gravity.BOTTOM
    override fun closeAnim(): Animation? = SimpleAnimationUtils.bottomDown()
    override fun showAnim(): Animation? = SimpleAnimationUtils.bottomPopUp()
    override fun layoutRes(): Int = R.layout.popup_reserve_time_picker

    private data class TimeStringPair(val time: DateTime, val str: String)
}