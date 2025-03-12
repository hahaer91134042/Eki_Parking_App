package com.eki.parking.Controller.activity.frag.SiteOpen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.SiteOpen.child.CopyLocOpenFrag
import com.eki.parking.Controller.builder.OpenTimeMarkerBuilder
import com.eki.parking.Controller.calendar.EkiCalendar
import com.eki.parking.Controller.dialog.EkiMsgDialog
import com.eki.parking.Controller.dialog.EkiProgressDialog
import com.eki.parking.Controller.dialog.SimulatePageDialog
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.CalendarSelectListener
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.process.AddOpenSetProcess
import com.eki.parking.Controller.process.CancelOpenSetProcess
import com.eki.parking.Controller.process.EditLocationProcess
import com.eki.parking.Controller.process.ManagerLocOrderProcess
import com.eki.parking.Model.DTO.OpenSet
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.DTO.TimeRange
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.EnumClass.WeekDay
import com.eki.parking.Model.request.body.AddOpenSetBody
import com.eki.parking.Model.request.body.CancelOpenSetBody
import com.eki.parking.Model.request.body.EditLocationBody
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.EditLocationResponse
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.View.popup.OpenTimeSelectPopup
import com.eki.parking.View.widget.AddOpenTimeView
import com.eki.parking.View.widget.OpenSetListView
import com.eki.parking.databinding.FragOpenCalendarBinding
import com.eki.parking.extension.*
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.hill.devlibs.EnumClass.ProgressMode
import com.hill.devlibs.extension.cleanDuplic
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.showToast
import com.hill.devlibs.extension.toArrayList
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.DateUnit
import com.hill.devlibs.time.ext.daySpan
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2020/06/30
 */
class OpenCalendarFrag : SearchFrag(), ISetData<ManagerLocation>,
    CalendarSelectListener<Calendar?>, IFragViewBinding {

    private lateinit var binding: FragOpenCalendarBinding
    private lateinit var location: ManagerLocation
    private lateinit var ekiCalendar: EkiCalendar<CalendarView, Calendar?>
    private var selectTime = DateTime()
    private var now = DateTime()
    private lateinit var selector: OpenSetListView.Selector

    override fun initFragView() {
        ekiCalendar = EkiCalendar.loadSimple(binding.calendarView)

        drawCalendar()
        /**
         *以後需要再顯示限制時間 在打開
         */
//        loadLocOrder()

        binding.openSetView.onOpenSetCancel = {
            checkAndDeleteOpenTime(it)
        }
        binding.openSetView.onAutoRepeatSelect = { checked ->
//            Log.w("$TAG check->${checked}")
            if (location.Config?.beRepeat != checked) {
                EditLocProcess(location.toData().apply { config.beRepeat = checked }).apply {
                    onSuccess = {
                        location.Config?.beRepeat = checked
                        location.sqlSaveOrUpdate()
                        showToast(string(R.string.Successfully_modified))
                    }
                    onFail = {
                        binding.openSetView.isSelectAutoRepeat = location.Config?.beRepeat ?: true
                    }
                }.start()
            }
        }

        binding.refreshView.isEnabled = false

        ekiCalendar.onResume(binding.calendarView)
        ekiCalendar.addDateSelectListener(this)
        binding.calendarMonthBar.setCalendar(ekiCalendar)

        binding.addOpenTimeView.btnEvent = addBtnEvent
    }

    private var addBtnEvent = object : AddOpenTimeView.OnBtnEvent {
        override fun onAddSingle() {
            OpenTimeSelectPopup(
                selectTime,
                DayOpen(selectTime, location.Config?.OpenSet ?: listOf()).list,
                context
            ).also { p ->
                p.onOpenSelect = { selectOpen ->
                    Log.i("select open ->$selectOpen")
                    addNewOpenSet(selectOpen)
                }
            }.showPopupWindow()
        }

        override fun onAddRepeat() {
            var nextWeekOpen =
                FindNextWeekOpen(selectTime.date, location.Config?.OpenSet ?: listOf())

            if (nextWeekOpen.result.isEmpty()) {
                showToast(string(R.string.No_time_to_join))
            } else {
                addNewOpenSet(nextWeekOpen.result)
            }

        }

        override fun onAddCopy() {
            var copyOpen = location.Config?.OpenSet?.filter {
                it.weekSet == WeekDay.NONE
                //true
            } ?: listOf()

            SimulatePageDialog
                .initWithFrag(
                    CopyLocOpenFrag()
                        .also {
                            it.setData(copyOpen)
                            it.onCopyAddFinish = {
                                binding.addOpenTimeView.setOptionOpen(false)
                            }
                        }, string(R.string.Choose_the_parking_space_posted)
                )
                .show(childFragmentManager, "CopyLocOpenFrag")
        }
    }


    /**
     *以後需要再顯示限制時間 在打開
     */
//    private fun loadLocOrder(){
//        refreshView?.isRefreshing=true
//        LoadOrderProcess().apply {
//            onFail={
//                refreshView?.isRefreshing=false
//            }
//            onSuccess={orderList->
//                refreshView?.isRefreshing=false
//                locOrder=orderList.first { it.Id==location.Id }
////                locOrder.printValue()
//
//                drawCalendar()
//            }
//        }.run()
//    }

    private fun addNewOpenSet(list: List<OpenSet>) {
        var progress = context?.showProgress(ProgressMode.PROCESSING_MODE)
        AddOpenProcess(AddOpenSetBody().apply {
            id = location?.Id
            openSet.addAll(list.map { set -> set.toData() })
            time = now.toString()
        }) {
            progress?.dismiss()
            location?.Config?.OpenSet?.apply {
                addAll(list)
            }
            location.sqlSaveOrUpdate()
            drawCalendar()
            binding.addOpenTimeView.setOptionOpen(false)
            context.showToast(getString(R.string.Opening_hours_have_been_added))
        }.apply { onFail = { progress?.dismiss() } }.run()
    }

    private fun checkAndDeleteOpenTime(set: OpenSet) {
        EkiMsgDialog().also { dialog ->
            dialog.msg = getString(R.string.Deleting_the_open_period_may_result_in_fines)
            dialog.determinClick = {
                var progress = context?.showProgress(ProgressMode.PROCESSING_MODE)
                CancelOpenProcess(CancelOpenSetBody().apply {
                    id = location.Id
                    openSet.add(set.toData())
                    time = now.toString()
                }) { loc, mulcts ->
                    progress?.dismiss()
                    location.copyFrom(loc)
                    location.sqlSaveOrUpdate()
                    drawCalendar()
                    context.showToast(getString(R.string.Opening_hours_cancelled))
                    if (mulcts.isNotEmpty()) {
                        var amt = 0.0
                        mulcts.forEach {
                            it.toSql().also { mulct ->
                                mulct.sqlSaveOrUpdate()
                                amt += mulct.currencyCost()
                            }
                        }
                        showMulctAmt(amt)
                    }
                }.apply { onFail = { progress?.dismiss() } }.run()
            }
        }.show(childFragmentManager)
    }

    private fun showMulctAmt(cost: Double) {
        EkiMsgDialog().also {
            it.msg = "違約罰金為 $cost NTD"
            it.btnSet = EkiMsgDialog.BtnSet.Single
        }.show(childFragmentManager)
    }

    private fun drawCalendar() {
        var builder = OpenTimeBuilder(now, location?.Config?.OpenSet ?: ArrayList())
        ekiCalendar.addCalendarMarker(builder.calendarMarker)

        var sort = builder.openSort

        binding.openSetView.notNull { view ->
            view.isSelectAutoRepeat = location.Config?.beRepeat ?: true

            selector = view.setOpenSort(sort)
            selector.selectFrom(selectTime)
        }
    }

    override fun onResumeFragView() {
        toolBarTitle = location?.Info?.Content ?: ""
    }

    override fun setData(data: ManagerLocation?) {
        data.notNull { location = ManagerLocation().apply { copyFrom(it) } }
    }

    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {

        selectTime = calendar?.toDateTime() ?: DateTime()
//        Log.w("OnCalendarSelect calendar->$calendar  selectTime->$selectTime")
        selector.selectFrom(selectTime)
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {

    }

    override fun onYearChange(year: Int) {

    }

    /**
     *以後需要再顯示限制時間 在打開
     */
    private inner class LoadOrderProcess : ManagerLocOrderProcess(context) {
        private var nowTime = DateTime.now()
        override val body: SendIdBody
            get() = SendIdBody(EkiApi.ManagerLocOrder).also { b ->
                b.id.add(location.Id)
                b.timeSpan = RequestBody.TimeSpan().apply {
                    start = DateTime(nowTime.year, nowTime.month, 1, 0, 0, 0).toString()
                    end = nowTime.toString()
                }
            }

    }

    private inner class AddOpenProcess(val set: AddOpenSetBody, back: () -> Unit) :
        AddOpenSetProcess(context) {
        override val body: AddOpenSetBody
            get() = set

        init {
            onEditSuccess = back
        }
    }

    private inner class CancelOpenProcess(
        val set: CancelOpenSetBody,
        back: (ManagerLocation, List<ResponseInfo.Mulct>) -> Unit
    ) : CancelOpenSetProcess(context) {
        override val body: CancelOpenSetBody
            get() = set

        init {
            onEditSuccess = back
        }
    }

    private inner class EditLocProcess(private var data: EditLocationBody) :
        EditLocationProcess(context) {
        var onSuccess = {}
        var onFail = {}

        fun start() {
            EkiMsgDialog().also {
                it.msg = "確定是否修改自動重複功能?"
                it.determinClick = {
                    Log.d("start repeat")

                    run()
                }
                it.cancelClick = {
                    onFail()
                }
            }.show(childFragmentManager)
        }

        override val body: EditLocationBody
            get() = data

        override val onResponse: OnResponseListener<EditLocationResponse>?
            get() = object : OnResponseListener<EditLocationResponse> {
                override fun onReTry() {
                    progress?.dismiss()
                }

                override fun onFail(errorMsg: String, code: String) {
                    progress?.dismiss()
                    onFail()
                }

                override fun onTaskPostExecute(result: EditLocationResponse) {
                    progress?.dismiss()
                    onSuccess()
                }
            }

        var progress: EkiProgressDialog? = null
        override fun run() {
            progress = from?.showProgress(ProgressMode.PROCESSING_MODE)
            super.run()
        }
    }

    private class FindNextWeekOpen(from: DateUnit, list: List<OpenSet>) {

        //要包含所選擇的日期 所以往回6天
        private var lastWeekRange = TimeRange(from - 6.daySpan, from)
        private var nextWeekRange = TimeRange(from + 1.daySpan, from + 7.daySpan)

        var result = ArrayList<OpenSet>().also {
            var now = DateTime.now()
            var lastOpen = list.filter { set ->
                //先去掉過去的時間
                set.startDateTime().date >= now.date
            }.filter { set ->
                when {
                    set.weekSet != WeekDay.NONE -> false
                    else -> lastWeekRange.isBetween(set.startDateTime())
                }
            }
            var nextOpen = list.filter { set ->
                //先去掉過去的時間
                set.startDateTime().date >= now.date
            }.filter { set ->
                when {
                    set.weekSet != WeekDay.NONE -> false
                    else -> nextWeekRange.isBetween(set.startDateTime())
                }
            }

            it.addAll(lastOpen.map { o ->
                OpenSet().apply {
                    Date = (o.startDateTime() + 7.daySpan).date.toString()
                    Week = o.Week
                    StartTime = o.StartTime
                    EndTime = o.EndTime
                }
            }.filter { o ->
                //這邊要去掉重疊的
                !nextOpen.any { n -> n.isOverLap(o) }
            })
        }
    }

    private class DayOpen(from: DateTime, locOpen: List<OpenSet>) {
        var list: List<OpenSet> = locOpen.filter {
            it.startDateTime(from).date == from.date
        }.cleanDuplic()

    }

    private class OpenTimeBuilder(start: DateTime, setList: List<OpenSet>) :
        OpenTimeMarkerBuilder(start, setList) {

        val openSort: List<OpenSetListView.OpenSort>
            get() = ArrayList<OpenSetListView.OpenSort>().also { list ->
                timeSort.forEach { entry ->
                    var now = entry.key

                    var count = list.count { it.year == now.year && it.month == now.month }
                    when {
                        count > 0 -> list.first { it.year == now.year && it.month == now.month }.list.addAll(
                            entry.value
                        )
                        else -> list.add(
                            OpenSetListView.OpenSort(
                                now.year,
                                now.month,
                                entry.value.toArrayList()
                            )
                        )
                    }
                }
            }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragOpenCalendarBinding.inflate(inflater, container, false)
        return binding
    }
}