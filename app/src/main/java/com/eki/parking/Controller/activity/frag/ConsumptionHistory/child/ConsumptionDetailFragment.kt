package com.eki.parking.Controller.activity.frag.ConsumptionHistory.child

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppFlag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.databinding.FragmentConsumptionDetailBinding
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.model.ValueObjContainer
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/05/26
 */
class ConsumptionDetailFragment : SearchFrag(), ISetData<EkiOrder>, IFragViewBinding {

    private lateinit var binding: FragmentConsumptionDetailBinding

    private lateinit var orderData: EkiOrder

    var toMain: (() -> Unit)? = null

    override fun initFragView() {
        toolBarTitle = getString(R.string.consumption_detail)
        binding.consumptionDetailAppealButton.setOnClickListener {
            context?.sendBroadcast(Intent(AppFlag.OnAppealOrder).apply {
                putExtra(AppFlag.DATA_FLAG, ValueObjContainer<EkiOrder>().apply {
                    setValueObjData(orderData)
                })
            })
        }
        setUI(orderData)
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragmentConsumptionDetailBinding.inflate(inflater, container, false)
        return binding
    }

    override fun setData(data: EkiOrder?) {
        data?.let {
            orderData = data
            if (this::binding.isInitialized) {
                setUI(orderData)
            }
        }
    }

    private fun setUI(data: EkiOrder) {
        binding.detailAddressText.text = data.Address.Detail

        val startDate = data.ReservaTime.startDateTime().date
        val startWeek = getStringArr(
            R.array.chinese_week_string_array
        )[data.ReservaTime.startDateTime().weekDay()]
        val startTime = data.ReservaTime.startDateTime().time.toString()

        val endDate = data.ReservaTime.endDateTime().date
        val endWeek = getStringArr(
            R.array.chinese_week_string_array
        )[data.ReservaTime.endDateTime().weekDay()]
        val endTime = data.Checkout?.Date?.toDateTime()?.time.toString() ?: data.ReservaTime.endDateTime().toString()

        binding.detailStartDateText.text = "${startDate} $startWeek ${startTime.removeRange(startTime.length-3,startTime.length)}"
        binding.detailEndDateText.text = "${endDate} $endWeek ${endTime.removeRange(endTime.length-3,endTime.length)}"

        binding.carPlaceText.text = data.LocSerial
        binding.orderNumberText.text = data.SerialNumber
        binding.receiptText.text = data.Invoice?.Number ?: ""
        binding.creditCardText.text = data.Invoice?.Card4No ?: ""

        val discount = if (data.Checkout?.CostFix == null) {
            0.0
        } else {
            if ((data.Checkout?.CostFix ?: 0.0) < 0.0) {
                data.Checkout?.CostFix ?: 0.0
            } else {
                0.0
            }
        }

        val parkingFee = data.Cost - (data.Checkout?.Claimant ?: 0.0) + discount
        binding.orderCostText.text =
            "(${getString(R.string.billing_method_half_hour,data.LocPrice.toInt())}) ${getString(R.string.price,parkingFee.toInt())}"

        binding.orderAdditionCostText.text = getString(R.string.price,data.Checkout?.Claimant?.toInt() ?:0)
        binding.orderDiscountText.text = getString(R.string.price,discount.toInt())
        binding.orderTotalCostText.text = getString(R.string.price,data.Cost.toInt())

        val dateInterval = DateTime.now().date - orderData.endDateTime().date
        if (dateInterval.day > 10) {
            binding.consumptionDetailAppealButton.visibility = View.INVISIBLE
        }
    }
}