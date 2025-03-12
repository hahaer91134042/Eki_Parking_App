package com.eki.parking.Controller.activity.frag.Reserva.adaptor

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.eki.parking.AppConfig
import com.eki.parking.Controller.tools.ReserveCheck
import com.eki.parking.Model.sql.LocationReserva
import com.eki.parking.R
import com.eki.parking.View.popup.ReservaTimeSelectPopup
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.View.widget.MinOffsetView
import com.eki.parking.extension.mapToReservaDelay
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.DateUnit
import com.hill.devlibs.time.ext.minSpan
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 31,10,2019
 */
class ReserveTimeAdapter(
    context: Context?,
    reserve: LocationReserva,
    from: DateTime
) : BaseAdapter<ReserveTimeAdapter.TimeItem>(context) {

    private var hourTimeList = ArrayList<ItemHourUnit>()
    private var reserveList = reserve.ReservaList
    private var check = ReserveCheck(
        from,
        reserve.OpenList,
        reserve.ReservaList.mapToReservaDelay()
    )

    var onSelectReserveTime: ((start: DateTime, end: DateTime) -> Unit)? = null

    init {
        Log.d("Init now time->$from, which day of week->${from.weekDay()}")

        var now = DateTime.now()

        for (hour in 0..23) {
//            Log.d("hour->$hour")
            var unit = ItemHourUnit(from.date, hour)

            unit.areaTimeList.forEach {
                var isUser = false
                val isOpen = !check.isForbidden(it.start) { reserveTime ->
//                    Log.w("check isForbidden back->$reserveTime")
                    isUser = reserveTime.user
                }

                it.type = if (isOpen) {
                    AreaTimeType.Open
                } else {
                    if (isUser) {
                        AreaTimeType.User
                    } else {
                        if (it.start < now) {
                            AreaTimeType.PastTime
                        } else {
                            AreaTimeType.Reserved
                        }
                    }
                }
            }

            hourTimeList.add(unit)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeItem {
        return TimeItem(getItemView(R.layout.item_select_hour, parent)).also {
            it.init(height * 2 / 10)
        }
    }

    override fun onBindViewHolder(item: TimeItem, position: Int) {
        super.onBindViewHolder(item, position)
        item.refresh(hourTimeList[position])
    }

    override fun getItemCount(): Int = hourTimeList.size

    inner class TimeItem(itemView: View) : ItemLayout<ItemHourUnit>(itemView) {
        var parentView: View = itemView.findViewById(R.id.parentView)
        var hour: TextView = itemView.findViewById(R.id.hourTex)
        var frame: LinearLayout = itemView.findViewById(R.id.minOffsetFrame)

        override fun init(lenght: Int) {
            super.init(lenght)
        }

        override fun refresh(data: ItemHourUnit) {
            super.refresh(data)

            hour.text = "${data.hour.mod02d()}"
            frame.removeAllViews()
            data.areaTimeList.forEach {
                var area = MinOffsetView(context)
                setUpArea(it, area)
                frame.addView(area)
            }
        }

        private fun setUpArea(areaSet: AreaTimeUnit, areaView: MinOffsetView) {
            areaView.color = areaSet.color
            areaView.min = areaSet.start.time.min.mod02d()

            //若為開放時間,允許開啟供時間選擇的 Dialog
            areaView.clickView.setOnClickListener {
                if (areaSet.type == AreaTimeType.Open) {
                    Log.w("${areaSet.end} click")
                    check.isReserve(areaSet.end) {
                        Log.i("select reservaTime->$it")
                        showPickerPopup(it,areaSet.start)
                    }
                } else {
                    showToast(getString(R.string.Cannot_make_an_appointment_at_this_time))
                }
            }
        }

        private fun showPickerPopup(open: ReserveCheck.OpenTime,clickTime:DateTime) {
            ReservaTimeSelectPopup(context).apply {
                openTime = open
                this.clickTime = clickTime
                this.reserveList = this@ReserveTimeAdapter.reserveList
                onReserve = { start, end ->
                    onSelectReserveTime?.invoke(start, end)
                }
            }.showPopupWindow()
        }
    }

    class ItemHourUnit(val date: DateUnit, val hour: Int) {
        var offset = AppConfig.ReservaTimeOffsetMin.minSpan //15 min
        var hourStartTime = DateTime(date.year, date.month, date.day, hour, 0, 0)
        var hourEndTime = DateTime(date.year, date.month, date.day, hour + 1, 0, 0)
        var areaTimeList = ArrayList<AreaTimeUnit>().apply {
            var start = hourStartTime
            do {
                var end = start + offset
                if (hourEndTime >= end) {
                    add(AreaTimeUnit(start, end))
                    start = end
                }
            } while (hourEndTime >= end)
        }
    }

    data class AreaTimeUnit(
        val start: DateTime,
        val end: DateTime
    ) {
        internal var type = AreaTimeType.Open

        val color: Int
            get() = when (type) {
                AreaTimeType.User -> R.color.Eki_orange_1
                AreaTimeType.PastTime -> R.color.black_translucent2
                AreaTimeType.Reserved -> R.color.light_gray5
                else -> R.color.color_white
            }
    }

    internal enum class AreaTimeType {
        Open,
        User,//自己的預約時段
        PastTime,//過掉的時間
        Reserved//已預約
    }
}

