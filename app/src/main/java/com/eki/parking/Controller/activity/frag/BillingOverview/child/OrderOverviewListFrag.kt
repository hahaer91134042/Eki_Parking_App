package com.eki.parking.Controller.activity.frag.BillingOverview.child

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.intent.OrderDetailIntent
import com.eki.parking.Controller.frag.ChildFrag
import com.eki.parking.Controller.tools.EkiCalculator
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.ManagerLvPercent
import com.eki.parking.R
import com.eki.parking.View.ViewType
import com.eki.parking.View.recycleview.adapter.ViewTypeAdaptor
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.databinding.FragOrderOverviewListBinding
import com.eki.parking.extension.sqlData
import com.eki.parking.extension.sqlHasData
import com.eki.parking.extension.toCurrency
import com.hill.devlibs.extension.*
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.IRecycleViewModelSet
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateUnit
import com.hill.devlibs.time.ext.monthEqual
import com.hill.devlibs.time.ext.removeDay

/**
 * Created by Hill on 2020/05/18
 */

class OrderOverviewListFrag : ChildFrag(), ISetData<List<EkiOrder>>,IFragViewBinding {

    private lateinit var binding: FragOrderOverviewListBinding
    private var dataList: List<EkiOrder>? = null

    private var itemList = ArrayList<OverViewItem>()
    var clickIncomeDate: ((DateUnit) -> Unit)? = null

    val lvPercent: ManagerLvPercent by lazy {
        when (sqlHasData<ManagerLvPercent>()) {
            true -> sqlData()!!
            else -> ManagerLvPercent()
        }
    }
    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragOrderOverviewListBinding.inflate(inflater,container,false)
        return binding
    }

    override fun initFragView() {

        itemList.clear()
        binding.recycleView.useSimpleDivider()

        dataList.notNull { orderList ->
            val sortMap = LinkedHashMap<DateUnit, ArrayList<EkiOrder>>()

            orderList.forEach { order ->
                when (!sortMap.any { it.key.monthEqual(order.ReservaTime.startDateTime().date) }) {
                    true -> sortMap[order.ReservaTime.startDateTime().date.removeDay()] =
                        arrayListOf(order)
                    else -> sortMap[order.ReservaTime.startDateTime().date.removeDay()]?.add(order)
                }
            }
            sortMap.forEach { set ->
                itemList.add(
                    OverViewItem(
                        ViewType.title, ItemData(
                            TitleData(
                                set.key,
                                set.value.sumByDouble { o ->
                                    try {
                                        //假如訂單沒有checkout的話(舊的系統帳號會出現)
                                        EkiCalculator.calCheckout(
                                            o,
                                            o.Checkout?.Date?.toDateTime()!!
                                        ).finalNormalCost
                                    } catch (e: Exception) {
                                        o.Cost
                                    }
                                },
                                set.value.size
                            ), null
                        )
                    )
                )
                set.value.forEach { order ->
                    itemList.add(OverViewItem(ViewType.item, ItemData(null, order)))
                }
            }
            binding.recycleView.adapter = OrderAdaptor(itemList).also { a ->
                a.setItemListClickListener {
                    itemList[it].data?.order.notNull { order ->
                        startActivitySwitchAnim(OrderDetailIntent(requireContext(), order), false)
                    }
                }
            }
        }
    }

    override fun setData(data: List<EkiOrder>?) {
        data.notNull { dataList = it }
    }

    private inner class OrderAdaptor(var list: ArrayList<OverViewItem>) :
        ViewTypeAdaptor<ItemData>(context) {
        override val modelList: ModelList<ItemData>
            get() = ModelList(list)
        override val viewSets: SetList<ItemData>
            get() = SetList(object : ItemTypeSet<ItemData> {
                override val viewType: Int
                    get() = ViewType.title

                override fun itemBack(parent: ViewGroup): ItemLayout<ItemData> {
                    return TitleItem(getItemView(R.layout.item_order_overview_title, parent)).also {
                        it.init()
                    }
                }
            }, object : ItemTypeSet<ItemData> {
                override val viewType: Int
                    get() = ViewType.item

                override fun itemBack(parent: ViewGroup): ItemLayout<ItemData> {
                    return OrderSelectItem(
                        getItemView(
                            R.layout.item_order_overview_select,
                            parent
                        )
                    ).also { it.init() }
                }
            })

    }

    private inner class TitleItem(itemView: View) : ItemLayout<ItemData>(itemView) {

        var year = itemView.findViewById<TextView>(R.id.yearText)
        var month = itemView.findViewById<TextView>(R.id.monthText)
        var income = itemView.findViewById<TextView>(R.id.incomeDetail)
//        var amt=itemView.findViewById<TextView>(R.id.totalAmountText)
//        var record=itemView.findViewById<TextView>(R.id.recordText)

        override fun init() {
            income.setBackgroundResource(R.drawable.stroke_round_corner_green2)
            income.setTextColor(getColor(R.color.Eki_green_2))
            income.setOnClickListener {
                itemData.titleData?.date.notNull { d ->
                    clickIncomeDate?.invoke(d)
                }
            }
        }

        override fun refresh(data: ItemData?) {
            super.refresh(data)
            data?.titleData.notNull {
                year.text = it.date.year.toString()
                month.text = getString(R.string.Month_formate).messageFormat(it.date.month.mod02d())

            }
        }

    }

    private inner class OrderSelectItem(itemView: View) : ItemLayout<ItemData>(itemView) {

        var parentView = itemView.findViewById<View>(R.id.parentView)!!
        var paidText = itemView.findViewById<TextView>(R.id.paidText)!!
        var timeSpan = itemView.findViewById<TextView>(R.id.timeSpanText)!!
        var cost = itemView.findViewById<TextView>(R.id.costText)!!

        private val format = "{0}/{1} {2}:{3}"

        override fun init() {


            parentView.setOnClickListener { itemClick() }
        }

        override fun refresh(data: ItemData?) {
            super.refresh(data)
            data?.order.notNull { order ->
                try {

                    val result = EkiCalculator.calCheckout(order, order.Checkout?.Date?.toDateTime()!!)
                    val start = order.startDateTime()
                    val end = order.endDateTime()
                    val starStr = format.messageFormat(
                        start.month.mod02d(),
                        start.day.mod02d(),
                        start.hour.mod02d(),
                        start.min.mod02d()
                    )
                    val endStr = format.messageFormat(
                        end.month.mod02d(),
                        end.day.mod02d(),
                        end.hour.mod02d(),
                        end.min.mod02d()
                    )

                    timeSpan.text = "$starStr - $endStr"
                    val finalNormalCost = result.finalNormalCost.toCurrency(order.currencyUnit)
                    cost.text = getString(R.string.Price_format).messageFormat(finalNormalCost)

                    val realPaid =
                        (finalNormalCost.multiply(1.0 -lvPercent.Percent.toDouble()
                            .divide(100.0))).toCurrency(order.currencyUnit)

                    paidText.text = "(實收$${realPaid})"


                } catch (e: Exception) {
//                    order.printValue()
                }

            }
        }

    }

    private data class OverViewItem(override val viewType: Int, override val data: ItemData?) :
        IRecycleViewModelSet<ItemData>

    private data class ItemData(val titleData: TitleData?, val order: EkiOrder?)

    /*
    * amt count 目前暫時不用了 以後再看情況刪除
    * */
    private data class TitleData(
        val date: DateUnit,
        val amt: Double,
        val count: Int
    )
}