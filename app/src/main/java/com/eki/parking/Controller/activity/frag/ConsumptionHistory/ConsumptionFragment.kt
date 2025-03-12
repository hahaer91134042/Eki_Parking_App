package com.eki.parking.Controller.activity.frag.ConsumptionHistory

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppResultCode
import com.eki.parking.Controller.activity.frag.ConsumptionHistory.adapter.ConsumptionAdapter
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IFilterChangeBack
import com.eki.parking.Controller.impl.IFilterSet
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.databinding.FragmentConsumptionBinding
import com.eki.parking.extension.sqlDataListAsync
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.getMonth
import com.hill.devlibs.time.ext.getYear
import java.util.*

//By Linda
class ConsumptionFragment : SearchFrag(), ISetData<ArrayList<EkiOrder>>,
    IFilterChangeBack<EkiOrder>, IFragViewBinding {

    private lateinit var binding: FragmentConsumptionBinding
    private lateinit var adapter: ConsumptionAdapter

    private var orderList = listOf<EkiOrder>()

    var toDetail: ((EkiOrder) -> Unit)? = null
    private val now = DateTime()
    private val calendar = Calendar.getInstance()
    private val halfYearAgoCalendar = Calendar.getInstance()

    override fun initFragView() {
        halfYearAgoCalendar.add(Calendar.MONTH,-6)
    }

    override fun onResumeFragView() {
        toolBarTitle = getString(R.string.Consumption_history)

        binding.leftArrowButton.setOnClickListener {
            calendar.add(Calendar.MONTH,-1)
            if (calendar.time < halfYearAgoCalendar.time) {
                calendar.add(Calendar.MONTH,+1)
                return@setOnClickListener
            }
            binding.monthText.text = getString(R.string.year_month_format,
                calendar.getYear(),calendar.getMonth())
            setUI(orderList.filter { it.ReservaTime.startDateTime().month == calendar.getMonth() })
        }
        binding.rightArrowButton.setOnClickListener {
            calendar.add(Calendar.MONTH,1)
            if (calendar.getMonth() <= now.month) {
                binding.monthText.text = getString(R.string.year_month_format,
                        calendar.getYear(),calendar.getMonth())
                setUI(orderList.filter { it.ReservaTime.startDateTime().month == calendar.getMonth() })
            } else {
                calendar.add(Calendar.MONTH,-1)
            }
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragmentConsumptionBinding.inflate(inflater,container,false)
        adapter = ConsumptionAdapter(requireContext(),ConsumptionAdapter.OnClickListener{
            toDetail?.invoke(it)
        })
        binding.consumptionRecyclerView.adapter = adapter
        return binding
    }

    override fun onFilterChange(filters: IFilterSet.FilterList<EkiOrder>) {

    }

    override fun onSortChange(sortby: IFilterSet.SortBySet<EkiOrder>) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            AppResultCode.OrderStateChange -> {
                showProgress(true)
                sqlDataListAsync<EkiOrder> { orders ->
                    binding.progress.relativeLayout.visibility = View.GONE
                    orderList = orders

                    setUI(orderList.filter { it.ReservaTime.startDateTime().month == now.month })
                }
            }
        }
    }

    override fun setData(data: ArrayList<EkiOrder>?) {
        if (data != null) {
            showProgress(true)

            orderList = data

            binding.monthText.text = getString(R.string.year_month_format,
                now.year,now.date.month)
            setUI(orderList.filter { it.ReservaTime.startDateTime().month == now.month })
            setListSorting(true)
            showProgress(false)
        }
    }

    private fun showProgress(isShow:Boolean) {
        if (isShow) {
            binding.progress.relativeLayout.visibility = View.VISIBLE
        } else {
            binding.progress.relativeLayout.visibility = View.GONE
        }
    }

    private fun setUI(list: List<EkiOrder>) {
        binding.consumptionText.text = getString(R.string.record_size,list.size)

        var cost = 0.0
        list.forEach {
            cost += it.Cost
        }
        binding.AmountOfConsumptionText.text = "$${cost.toInt()}"

        adapter.submitList(list) {
            binding.consumptionRecyclerView.scrollToPosition(0)
        }
    }

    fun setListSorting(isDescend:Boolean) {
        val list = if (isDescend) {
            orderList.sortedByDescending { it.ReservaTime.StartTime }
        } else {
            orderList.sortedBy { it.ReservaTime.StartTime }
        }
        setUI(list.filter { it.ReservaTime.startDateTime().month == calendar.getMonth() })
    }
}