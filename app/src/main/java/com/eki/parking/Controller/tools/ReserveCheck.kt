package com.eki.parking.Controller.tools

import com.eki.parking.Model.DTO.OpenSet
import com.eki.parking.Model.DTO.ReservaSet
import com.eki.parking.extension.standarByTimeOffset
import com.hill.devlibs.extension.toDate
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 01,11,2019
 */
class ReserveCheck(var from: DateTime, oList: ArrayList<OpenSet>, val rList: ArrayList<ReservaSet>) {

    private var openList = OpenList()
    private var reservaList = ReserveList()
    private var now = DateTime()
    private val aWeekLater =
        DateTime(from.year, from.month, from.day + 6, from.hour, from.min,from.sec)
    private val dateFormat = "yyyy-MM-dd"

    init {
        oList.sortedBy { it.StartTime }
        oList.forEach {
            val start = it.startDateTime(from)
            val end = it.endDateTime(from)
            if (from.date == start.date) {
                openList.add(OpenTime(start, end))
            }
            //檢查有無連續開放時間
            if (oList.any { it.endDateTime().date == start.date } && it.Date.toDate(dateFormat) < aWeekLater.date) {
                if (!openList.contains(OpenTime(start, end))) {
                    openList.add(OpenTime(start, end))
                }
            }
        }
        rList.sortedBy { it.StartTime }
        rList.forEach {
            val start = it.startDateTime()
            val end = it.endDateTime()
            if (from.date == start.date) {
                reservaList.add(ReserveTime(start, end, it.IsUser))
            }
            //符合跨日條件,檢查跨日開放時間內的所有預定
            if (from.date <= end.date && !reservaList.contains(ReserveTime(start, end, it.IsUser))) {
                reservaList.add(ReserveTime(start, end, it.IsUser))
            }
        }

    }

    fun isForbidden(time: DateTime, back: ((ReserveTime) -> Unit)? = null): Boolean {
        if (time < now)
            return true

        if (!openList.isOpen(time))
            return true
        if (reservaList.isForbidden(time, back))
            return true

        return false
    }

    fun isReserve(clickTime: DateTime, back: ((OpenTime) -> Unit)? = null): Boolean {
        val now = DateTime().standarByTimeOffset()
        val oList = openList.sortedBy { it.start.date.day }

        var start = DateTime()
        var end = DateTime()

        oList.forEach { open ->
            val pReserveTime = runCatching {
                reservaList.last { it.end <= clickTime }
            }.getOrNull()

            val lReserveTime = runCatching {
                reservaList.first { it.start >= clickTime }
            }.getOrNull()

            if (open.isBetween(clickTime)) {
                start = when (reservaList.isEmpty()) {
                    true -> open.start
                    else -> when (pReserveTime != null) {
                        true -> pReserveTime.end
                        else -> open.start
                    }
                }
            }

            end = when (reservaList.isEmpty()) {
                true -> if (openList.find { it.start == open.end } != null) {
                    openList.find { it.start == open.end }?.end ?: open.end
                } else {
                    open.end
                }
                else -> when (lReserveTime != null) {
                    true -> lReserveTime.start
                    else -> open.end
                }
            }

            //判斷現在時間 避免已經過掉的時間點出現
            if (open == oList.last()) {
                back?.invoke(
                    OpenTime(
                        when {
                            start < now -> now
                            else -> start
                        }, end
                    )
                )
                return true
            }
        }
        return false
    }

    data class OpenTime(val start: DateTime, val end: DateTime) {
        fun isBetween(time: DateTime): Boolean {
            //time<=end 會導致後面的間格被加進去
            return start <= time && time < end
        }

    }

    data class ReserveTime(val start: DateTime, val end: DateTime, var user: Boolean = false) {
        fun isBetween(time: DateTime): Boolean {
            //time<=end 會導致後面的間格被加進去
            return start <= time && time < end
        }
    }

    private class OpenList : ArrayList<OpenTime>() {
        fun isOpen(time: DateTime): Boolean {
            forEach {
                if (it.isBetween(time))
                    return true
            }
            return false
        }
    }

    private class ReserveList : ArrayList<ReserveTime>() {
        fun isForbidden(time: DateTime, back: ((ReserveTime) -> Unit)? = null): Boolean {
            forEach {
                if (it.isBetween(time)) {
                    back?.invoke(it)
                    return true
                }
            }
            return false
        }
    }
}