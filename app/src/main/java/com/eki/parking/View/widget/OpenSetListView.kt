package com.eki.parking.View.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.eki.parking.Model.DTO.OpenSet
import com.eki.parking.Model.EnumClass.WeekDay
import com.eki.parking.R
import com.eki.parking.View.ViewType
import com.eki.parking.View.popup.OpenTimeSelectPopup
import com.eki.parking.View.recycleview.BaseRecycleView
import com.eki.parking.View.recycleview.adapter.ViewTypeAdaptor
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.extension.formatFull
import com.hill.devlibs.extension.*
import com.hill.devlibs.impl.IRecycleViewModelSet
import com.hill.devlibs.impl.ISelectFrom
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.shortTime

/**
 * Created by Hill on 2020/03/24
 */
class OpenSetListView(context: Context?, attrs: AttributeSet?) : BaseRecycleView(context, attrs) {

    private var openList=ArrayList<OpenSort>()
//    private var pastOrder=ArrayList<EkiOrder>()
    private var selectTime= DateTime()

    var onOpenSetCancel:(OpenSet)->Unit={}
    var addNewOpenSet:(List<OpenSet>)->Unit={}
    var onAutoRepeatSelect:(Boolean)->Unit={}

    init {
        useVerticalView()
    }

    private var setAutoRepeat:(Boolean)->Unit={}

    var isSelectAutoRepeat:Boolean=true
    set(value) {
        setAutoRepeat(value)
        field=value
    }

    fun setOpenSort(list:List<OpenSort>):Selector{
//                    past:List<EkiOrder>):Selector{
//        Log.w("OpenSort list size->${list.size}")
        //list.printList()
//        list.forEach {
//            Log.i("Open set size->${it.list.size}")
////            it.list.printList()
//        }
        openList.clear()
        openList.addAll(list)

//        pastOrder.clear()
//        pastOrder.addAll(past)
        return object :Selector(){
            override fun selectFrom(from: DateTime) {
//                Log.w("Select from->$from")
                drawListBy(from)
            }
        }
    }

    private fun drawListBy(from: DateTime) {
        selectTime=from
        removeAllViews()
        var open=openList.first{ it.year==from.year&&it.month==from.month }
//        Log.d("Select open sort->$open")
        adapter=OpenListAdaptor(context,
                                from,
                                open.list.sortedBy { it.startDateTime(from).toStamp() })
                                /**
                                 *以後需要再顯示限制時間 在打開
                                 */
//                                pastOrder.filter{
//                                    var start=it.ReservaTime.startDateTime()
//                                    start.year==from.year&&start.month==from.month
//                                })

    }
    private inner class OpenListAdaptor(context: Context?,
                                        private var now:DateTime,
                                        private var monthSetList:List<OpenSet>) : ViewTypeAdaptor<OpenTime>(context) {
                                        /**
                                         *以後需要再顯示限制時間 在打開
                                         */
//                                        private var monthOrder:List<EkiOrder>) : ViewTypeAdaptor<OpenTime>(context) {

        private var list=ArrayList<OpenModel>()

        val dayOpen:List<OpenSet>
            get() = monthSetList.cleanDuplic()//去掉週期性的(因為會有重複)
                    .filter { it.startDateTime(now).date == now.date }


        var onAddOpen:(List<OpenSet>)->Unit={list->

//            Log.i("monthSetList")
//            monthSetList.forEach { it.printValue() }

            //這要過濾去掉已經有設定的日期
//            var newList=list
//                    .filter {set->
//                        monthSetList.none { it.startDateTime().date == set.startDateTime().date }
//                    }
            //這邊要改成處理掉overlap的時間段落 改道跳窗內部處理好了

//            Log.w("Add filter newList->$newList")

            /**
             *以後需要再顯示限制時間 在打開
             */

//            var oriMonth = monthSetList
//                    .map { it }//新產生另一個list副本
//                    .toArrayList().also {
//                        list.forEach { set->
//
//                            when (set.weekSet == WeekDay.NONE) {
//                                true -> it.add(set)//特定日期開放
//                                else -> {
//                                    for (i in 0 until set.startDateTime().weekNumInMonth)
//                                        it.add(set)
//                                }
//                            }
//                        }
//                    }

            addNewOpenSet(list)

            /**
             *以後需要再顯示限制時間 在打開
             */
//            var remainHour=object :EkiCalculator.ICalRemainHour{
//                override val setTime: DateTime
//                    get() = now
//                override val openSet: List<OpenSet>
//                    get() = oriMonth
//                override val pastOrder: List<EkiOrder>
//                    get() = monthOrder
//            }.remainHour()
//
//            Log.i("this month Remain hour->${remainHour}")
//            when{
//                remainHour>=0->addNewOpenSet(list)
//                else->context.showToast(getString(R.string.Insufficient_hours_left))
//            }
        }
        /**
         *以後需要再顯示限制時間 在打開
         */
//        private var setTitleHour:((Double)->Unit)?=null

        init {

            list.add(OpenModel(ViewType.title,OpenTime(now).also {
//                hourInMonth=monthSetList.remainHour()
                /**
                 *以後需要再顯示限制時間 在打開
                 */
//                it.hourInMonth=object :EkiCalculator.ICalRemainHour{
//                    override val setTime: DateTime
//                        get() = now
//                    override val openSet: List<OpenSet>
//                        get() = monthSetList
//                    override val pastOrder: List<EkiOrder>
//                        get() = monthOrder
//                }.remainHour()
//
//                Log.i("this month Remain hour->${it.hourInMonth}")
            }))
//            setList.cleanDuplic()//去掉週期性的(因為會有重複)
//                    .filter { it.startDateTime(now).date==now.date }
            dayOpen.forEach { list.add(OpenModel(ViewType.item,OpenTime(now,it))) }


            /**
             * 目前新增時段 換成另一種UI
             * */
//            list.add(OpenModel(ViewType.edit, OpenTime(now).also { it.dayOpen=dayOpen }))//add btn
        }

        override val modelList: ModelList<OpenTime>
            get() = ModelList(list)
        override val viewSets: SetList<OpenTime>
            get() = SetList(object :ItemTypeSet<OpenTime>{
                override val viewType: Int
                    get() = ViewType.title

                override fun itemBack(parent: ViewGroup): ItemLayout<OpenTime> =
                        TitleItem(getItemView(R.layout.item_open_set_title,parent)).apply {
                            init()
                            /**
                             *以後需要再顯示限制時間 在打開
                             */
//                            setTitleHour=setRemainHour
                        }
            },object :ItemTypeSet<OpenTime>{
                override val viewType: Int
                    get() = ViewType.item

                override fun itemBack(parent: ViewGroup): ItemLayout<OpenTime> =
                        OpenTimeItem(getItemView(R.layout.item_open_time,parent)).apply {
                            init()
                            itemCancel=onOpenSetCancel
                        }
            },object :ItemTypeSet<OpenTime>{
                override val viewType: Int
                    get() = ViewType.edit

                override fun itemBack(parent: ViewGroup): ItemLayout<OpenTime> =
                        AddItem(getItemView(R.layout.item_open_time_add,parent)).apply {
                            init()
                            itemAdd=onAddOpen
                        }
            })

    }

    private inner class TitleItem(itemView: View) : ItemLayout<OpenTime>(itemView) {

        private val openText:TextView=itemView.findViewById(R.id.openHour)
        private val autoCB=itemView.findViewById<CheckBox>(R.id.autoCB)


        /**
         *以後需要再顯示限制時間 在打開
         */
//        var setRemainHour:(Double)->Unit={
//            itemData?.hourInMonth=it
//            openText.text="${getString(R.string.Open_appointments)}${getString(R.string.Open_time_remain_hour)
//                    .messageFormat(it,AppConfig.maxOpenHourPerMonth)}"
//        }

        override fun init() {
            autoCB.isChecked=isSelectAutoRepeat

            autoCB.setOnCheckedChangeListener { buttonView, isChecked ->

                onAutoRepeatSelect(isChecked)
            }
            setAutoRepeat={
                autoCB.isChecked=it
            }
        }

        override fun refresh(data: OpenTime?) {
            super.refresh(data)
            data.notNull {

                /**
                 *以後需要再顯示限制時間 在打開
                 */
//                openText.text="${getString(R.string.Open_appointments)}${getString(R.string.Open_time_remain_hour)
//                        .messageFormat(it.hourInMonth,AppConfig.maxOpenHourPerMonth)}"
            }

        }
    }
    private class OpenTimeItem(itemView: View) : ItemLayout<OpenTime>(itemView) {
        private val date:TextView =itemView.findViewById(R.id.titleText)
        private val time:TextView=itemView.findViewById(R.id.mainText)
        private val cancelBtn:ImageView=itemView.findViewById(R.id.cancelBtn)

        var itemCancel:(OpenSet)->Unit={}

        override fun init() {
            cancelBtn.setOnClickListener {
                itemData?.set.notNull { itemCancel(it) }
            }
        }

        override fun refresh(data: OpenTime?) {
            super.refresh(data)

            data?.set.notNull { set->
                date.text=when(set.weekSet==WeekDay.NONE){
                    true->set.startDateTime().formatFull()
                    false->getString(set.weekSet.strRes)
                }
                var start=set.startDateTime()
                var end=set.endDateTime()
                time.text="{0} - {1}".messageFormat(start.shortTime(),when((end.date-start.date).totalDays>0){
                    true->"24:00"
                    else->end.shortTime()
                })
            }
        }
    }

    @Deprecated("這目前暫時沒用到了以後再刪除")
    private class AddItem(itemView: View) : ItemLayout<OpenTime>(itemView) {
        private val addBtn=itemView.findViewById<View>(R.id.addBtn)
        var itemAdd:((List<OpenSet>)->Unit)?=null
        override fun init() {
            addBtn.setOnClickListener {
//                itemAdd()

                itemData.notNull {
                    OpenTimeSelectPopup(it.now,
                                itemData.dayOpen?:ArrayList(),
                                        context).also { p->
                        p.onOpenSelect={selectOpen->
                            itemAdd?.invoke(selectOpen)
                        }
//                        p.onSameWeekSelect={
////                            it.forEach {open-> open.printValue() }
//                            itemAdd?.invoke(it)
//                        }
                    }.showPopupWindow()
                }

            }
        }

        override fun refresh(data: OpenTime?) {
            super.refresh(data)
        }
    }
    private class OpenModel(override val viewType: Int, override val data: OpenTime?) :IRecycleViewModelSet<OpenTime>{
    }

    private class OpenTime(val now:DateTime,val set:OpenSet?=null){
//        var hourInMonth=0.0
        var dayOpen:List<OpenSet>?=null
    }

    abstract class Selector:ISelectFrom<DateTime>
    data class OpenSort(val year:Int,val month:Int,var list:ArrayList<OpenSet>)
    private interface OpenAdd{fun onOpenAdd(set:OpenSet)}
}