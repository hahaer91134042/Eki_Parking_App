package com.eki.parking.View.popup

import android.content.Context
import android.view.Gravity
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.eki.parking.Model.EnumClass.WeekDay
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.View.abs.EkiPopupWindow
import com.eki.parking.View.widget.TimeRangeSelectView
import com.eki.parking.extension.string
import com.eki.parking.extension.toEnum
import com.hill.devlibs.extension.*
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.formateShort
import com.hill.devlibs.tools.Log
import com.hill.devlibs.util.SimpleAnimationUtils

/**
 * Created by Hill on 2021/11/18
 */
class OrderExtenTimePopup(context: Context?) : EkiPopupWindow(context),ISetData<EkiOrder> {

    private var timeSelectView=contentView.findViewById<TimeRangeSelectView>(R.id.timeSelectView)
    private var cancelBtn=contentView.findViewById<ImageView>(R.id.cancelBtn)
    private var extendBtn=contentView.findViewById<Button>(R.id.extenBtn)
    private var startTimeText=contentView.findViewById<TextView>(R.id.startTimeText)
    private var endTimeText=contentView.findViewById<TextView>(R.id.endTimeText)

    private var monthText=contentView.findViewById<TextView>(R.id.monthText)
    private var dayText=contentView.findViewById<TextView>(R.id.dayText)
    private var weekText=contentView.findViewById<TextView>(R.id.weekText)

    private var selectEndTime:DateTime?=null

    private lateinit var order:EkiOrder
    private lateinit var timeStart:DateTime
    private lateinit var timeEnd:DateTime

    var onSelectExtendEnd:((DateTime)->Unit)={ end->

    }

    override fun setUpView() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
        extendBtn.setOnClickListener {

            selectEndTime.isNull {
                context.showToast(string(R.string.Please_select_a_delayed_end_time))
            }.onNotNull {end->

                onSelectExtendEnd.invoke(end)
            }
        }

        monthText.text="${timeStart.month.mod02d()}月"
        dayText.text="${timeStart.day.mod02d()}"
        weekText.text= string(timeStart.weekDay.toEnum<WeekDay>().strRes)

        timeSelectView.selectTimeRange(timeStart,timeEnd)
        timeSelectView.setSelectStart(timeStart)

        timeSelectView.onSelectTimeEvent=object:TimeRangeSelectView.OnSelectTimeEvent{
            //這邊不用開始時間
            override fun onSelectStart(start: DateTime) {
                Log.w("on select start->${start}")
            }

            override fun onSelectEnd(end: DateTime) {
                Log.w("on select end->${end}")

                selectEndTime=end
                endTimeText.text=when{
                    end.date>timeStart.date->"24:00"
                    else->end.time.formateShort()
                }
            }
        }
    }

    fun setTimeRange(s:DateTime,e:DateTime){
        timeStart=s
        timeEnd=e
        initTimeRangeText()
    }

    fun refreshRange(s:DateTime,e:DateTime){
        timeStart=s
        timeEnd=e
        timeSelectView.selectTimeRange(timeStart,timeEnd)
        timeSelectView.setSelectStart(timeStart)
    }

    private fun initTimeRangeText(){
        startTimeText.text=timeStart.time.formateShort()
        endTimeText.text=""
    }

    override fun setData(data: EkiOrder?) {
        data.notNull { order=it }
    }

    override fun layoutRes(): Int = R.layout.popup_extenion_order_time
    override fun popGravity(): Int = Gravity.BOTTOM
    override fun blurBackground(): Boolean =false
    override fun backEnable(): Boolean =true
    override fun outSideDismiss(): Boolean =false
    override fun showAnim(): Animation? = SimpleAnimationUtils.bottomPopUp()
    override fun closeAnim(): Animation? = SimpleAnimationUtils.bottomDown()

}