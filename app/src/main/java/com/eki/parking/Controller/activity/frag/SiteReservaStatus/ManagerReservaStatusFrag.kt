package com.eki.parking.Controller.activity.frag.SiteReservaStatus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.EkiMsgDialog
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.process.ManagerLocOrderProcess
import com.eki.parking.Controller.tools.CheckRule
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.ManagerOrderCancelResponse
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.View.popup.DateSelectPopup
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.View.widget.AutoLoadImgView
import com.eki.parking.databinding.FragManagerReservaStatusBinding
import com.eki.parking.extension.asWeekStr
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.show
import com.eki.parking.extension.toCurrency
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toArrayList
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.TimeUnit
import com.hill.viewpagerlib.recycleview.CenterSnapHelper
import com.hill.viewpagerlib.recycleview.ScaleLayoutManager
import com.hill.viewpagerlib.recycleview.ViewPagerLayoutManager

/**
 * Created by Hill on 2020/05/08
 */
class ManagerReservaStatusFrag : SearchFrag(), ISetData<List<ManagerLocation>>, IFragViewBinding {

    private lateinit var binding: FragManagerReservaStatusBinding
    private lateinit var locList: List<ManagerLocation>

    private var selectTime = DateTime()
    private var selectLoc: ManagerLocation? = null
    private var locOrder = ArrayList<ResponseInfo.ManagerLocOrder>()

    override fun initFragView() {
        toolBarTitle = getString(R.string.Reservation_status)
        setUpDateSelect()
        startLoad(true)
        refresh()
    }

    private fun refresh() {
        binding.refreshView.setSize(Int.MIN_VALUE)
        binding.refreshView.setProgressViewOffset(
            false,
            0,
            (binding.refreshView.resources.displayMetrics.heightPixels * 0.3).toInt()
        )
        binding.refreshView.setColorSchemeResources(
            R.color.Eki_green_2,
            R.color.Eki_green_2,
            R.color.Eki_green_2
        )
        binding.refreshView.setOnRefreshListener {
            startLoad()
        }
    }

    private fun startLoad(isFirst: Boolean = false) {
        binding.refreshView?.isRefreshing = false
        if (locList.isNotEmpty()) {
            binding.progress.visibility = View.VISIBLE
            LocOrderProcess().apply {
                onSuccess = { result ->
                    locOrder = result
                    binding.progress.visibility = View.GONE
                    if (isFirst) {
                        startDraw()
                    } else {
                        addOrderLabel()
                    }
                }
                onFail = {
                    binding.progress.visibility = View.GONE
                }
            }.run()
        }
    }

    private fun startDraw() {
        context.notNull { c ->
            val adapter = LocSelectAdaptor(c, locList).apply {
                onSelectLoc = {
                    selectLoc = it
                    addOrderLabel()
                }
            }

            binding.recycleView.layoutManager = ScaleLayoutManager(c, locList.size).also {
                it.minScale = 0.8f
                it.minAlpha = 0.5f
                it.itemSpace = 0
                it.moveSpeed = 0.5f

                it.setOnPageChangeListener(object : ViewPagerLayoutManager.OnPageChangeListener {
                    override fun onPageSelected(position: Int) {
//                    Log.w("page select->$position")
                        adapter.clickItem(position)

                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })
            }
            CenterSnapHelper().attachToRecyclerView(binding.recycleView)

            binding.recycleView.adapter = adapter
        }
    }

    private fun setUpDateSelect() {
        binding.timeSelect.setOnClickListener {

            val opens = selectLoc?.Config?.OpenSet ?: ArrayList()

            DateSelectPopup(context, opens).also {
                it.onSelectTime = { t ->
                    selectTime = t
                    setTimeStr()
                    addOrderLabel()
                }
            }.showPopupWindow()
        }
        setTimeStr()
    }

    private fun setTimeStr() {
        binding.time.text =
            StringBuilder("${selectTime.year}/${selectTime.month.mod02d()}/${selectTime.day.mod02d()} ${selectTime.weekDay().asWeekStr}")
    }

    private fun addOrderLabel() {
        selectLoc.notNull { loc ->
            val orders = locOrder.firstOrNull { it.Id == loc.Id }
                ?.Order?.map { it.toSql() }
                ?.filter { it.ReservaTime.startDateTime().date == selectTime.date || it.ReservaTime.endDateTime().date == selectTime.date } ?: ArrayList()

            val opens = loc.Config?.OpenSet?.filter {
                it.startDateTime(selectTime).date == selectTime.date
            } ?: ArrayList()

            binding.hourScaleView.setOrder(orders,selectTime.date)
            binding.hourScaleView.setOpenBar(opens)
            binding.hourScaleView.onLabelClick = { order ->
                EkiMsgDialog().also {
                    it.btnSet = EkiMsgDialog.BtnSet.Single
                    it.msg =
                        "車主:${order.Member?.NickName}\n車牌:${order.CarNum}\n手機:${order.Member?.Phone}"
                }.show(childFragmentManager)
            }
            binding.hourScaleView.onOrderCancel = { order ->

                val mulct = calCancelMulct(order).toCurrency(order.Unit)

                when (mulct >= 0.0) {
                    true -> {
                        EkiMsgDialog().also {
                            it.msg = "刪除預約可能會產生罰金$${mulct} \n確定要刪除該預約嗎?"
                            it.determinClick = {
                                startCancelOrder(order)
                            }
                        }.show(childFragmentManager)
                    }
                    else -> {
                        showToast("該訂單無法取消")
                    }
                }
            }
        }
    }

    private fun calCancelMulct(order: EkiOrder): Double {
        val start = order.ReservaTime.startDateTime()
        val now = DateTime.now()
        val rule = CheckRule.managerMulctRules.firstOrNull { it.isInRule(start - now) }

        return when (rule) {
            null -> -1.0
            else -> order.Cost * rule.mulctRatio()
        }
    }

    private fun startCancelOrder(order: EkiOrder) {
        EkiRequest<SendIdBody>().apply {
            body = SendIdBody(EkiApi.ManagerOrderCancel).also { b ->
                b.serNum.add(order.SerialNumber)
                b.time = DateTime.now().toString()
            }
        }.sendRequest(
            context,
            showProgress = false,
            listener = object : OnResponseListener<ManagerOrderCancelResponse> {
                override fun onReTry() {
                    binding.progress.visibility = View.VISIBLE
                }

                override fun onFail(errorMsg: String, code: String) {
                    binding.progress.visibility = View.GONE
                }

                override fun onTaskPostExecute(result: ManagerOrderCancelResponse) {
                    binding.progress.visibility = View.GONE
                    removeLocOrder(order)

                }
            })

    }

    private fun removeLocOrder(order: EkiOrder) {
        selectLoc.notNull { loc ->
            val locOrder = locOrder.first { it.Id == loc.Id }
            locOrder.Order.remove(locOrder.Order.first { it.SerialNumber == order.SerialNumber })
            addOrderLabel()
        }
    }

    private inner class LocOrderProcess : ManagerLocOrderProcess(context) {
        override val body: SendIdBody
            get() = SendIdBody(EkiApi.ManagerLocOrder).also {
                it.time = DateTime().set(DateTime().date, TimeUnit(0, 0, 0)).toString()
                it.id = locList.map { loc -> loc.Id }.toArrayList()
            }
    }

    private class LocSelectAdaptor(context: Context, locList: List<ManagerLocation>) :
        BaseAdapter<LocItem>(context) {
        private var selectList = locList.map { loc -> LocSelectDate(false, loc) }
            .apply { firstOrNull()?.select = true }
            .toArrayList()
        private val clickList = ArrayList<ItemClickEvent>()

        var onSelectLoc: (ManagerLocation) -> Unit = {}

        init {
            setItemListClickListener { position ->
                onSelectLoc(selectList[position].loc)
            }
        }

        fun clickItem(position: Int) {
            if (clickList.isNotEmpty()) {
                clickList.filter { c -> c.position() != position }
                    .forEach { i -> i.unClick() }

                clickList.firstOrNull { c -> c.position() == position }.also {
                }?.click()

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocItem =
            LocItem(getItemView(R.layout.item_loc_select_card, parent))
                .also {
                    it.init()
                }

        override fun onBindViewHolder(item: LocItem, position: Int) {
            super.onBindViewHolder(item, position)
            clickList.add(position, item)
            val data = selectList[position]
            item.refresh(data)

        }

        override fun getItemCount(): Int = selectList.size
    }

    private class LocItem(itemView: View) : ItemLayout<LocSelectDate>(itemView),
        ItemClickEvent {

        var locName = itemView.findViewById<TextView>(R.id.textView)
        var cardView = itemView.findViewById<CardView>(R.id.card_view)
        var img = itemView.findViewById<AutoLoadImgView>(R.id.imgView)

        override fun init() {
        }

        override fun refresh(data: LocSelectDate?) {
            super.refresh(data)
            data.notNull {
                img.loadUrl(it.loc.imgUrl(), true)
                if (it.select)
                    click()
                else
                    unClick()
            }
        }

        override fun click() {
            itemClick()
            locName.text = itemData.loc.Info?.Content
            itemData.select = true
        }

        override fun unClick() {
            locName.text = ""
            itemData.select = false
        }

        override fun position(): Int = layoutPosition
    }

    private data class LocSelectDate(var select: Boolean, val loc: ManagerLocation) {
    }

    private interface ItemClickEvent {
        fun click()
        fun unClick()
        fun position(): Int
    }

    override fun setData(data: List<ManagerLocation>?) {
        data.notNull {
            locList = it.sortedBy { loc -> loc.Id }
            selectLoc = locList.firstOrNull()
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragManagerReservaStatusBinding.inflate(inflater, container, false)
        return binding
    }
}