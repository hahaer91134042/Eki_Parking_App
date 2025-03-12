package com.eki.parking.Controller.activity.frag.OrderDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.OrderDetail.child.RatingFrag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.process.RatingProcess
import com.eki.parking.Controller.tools.EkiCalculator
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.body.RatingBody
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.databinding.FragOrderDetailPanelBinding
import com.eki.parking.extension.asWeekStr
import com.eki.parking.extension.show
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.ext.formateShort

/**
 * Created by Hill on 2020/05/19
 */
class OrderDetailPanelFrag : SearchFrag(), ISetData<EkiOrder>, IFragViewBinding {

    private lateinit var binding: FragOrderDetailPanelBinding
    private var order: EkiOrder? = null

    var onArgue: (EkiOrder) -> Unit = {}
    var onRatingFinish = {}

    override fun initFragView() {

        order.notNull { o ->
            val start = o.startDateTime()
            val end = o.endDateTime()
            binding.dateText.text = "${start.year}/${start.month.mod02d()}/${start.day.mod02d()} " +
                    "${start.weekDay().asWeekStr} ${start.time.formateShort()}-${end.time.formateShort()}"
            setUpBtn(o)

            o.Checkout.notNull { checkout ->

                val result = EkiCalculator.calCheckout(o, checkout.Date.toDateTime())
                result.discountAmt = when (checkout.CostFix) {
                    0.0 -> 0.0
                    else -> -checkout.CostFix
                }

                binding.imgView.loadUrl(checkout.Img, true)
                val priceTemp = getString(R.string.Price_format)
                val minusPriceTemp = getString(R.string.Minus_Price_format)

                binding.parkingFeeRow.text = priceTemp.messageFormat(result.normalCost)
                binding.otherFeeRow.text = priceTemp.messageFormat(o.HandlingFee)
                binding.defaultFeeRow.text = priceTemp.messageFormat(result.normalDamageCost)
                binding.sumFeeRow.text = priceTemp.messageFormat(result.totalNormalCost)

                binding.discountRow.text = minusPriceTemp.messageFormat(result.discountAmt)

                binding. amountText.text = priceTemp.messageFormat(result.finalNormalCost)

            }
        }
    }

    override fun onResumeFragView() {
        toolBarTitle = order?.SerialNumber ?: getString(R.string.Order_Details)
    }

    private fun setUpBtn(order: EkiOrder) {
        setBtnEnable(order)

        binding.toRatingBtn.setOnClickListener {
            RatingFrag.creat().also {
                it.onRatingStartBack = { star ->
                    order.notNull { o ->
                        RatingUser(o, star).run()
                    }
                }
            }.show(childFragmentManager)
        }

        binding.toAppealBtn.setOnClickListener {
            order.notNull(onArgue)


        }
    }

    private fun setBtnEnable(o: EkiOrder) {
        binding.toRatingBtn.isEnabled = o.Rating
        binding.toAppealBtn.isEnabled = o.Argue
    }


    private inner class RatingUser(private var o: EkiOrder, var star: Double) :
        RatingProcess(context) {
        init {
            onFail = {

            }
            onSuccess = {
                o.Rating = false
                setBtnEnable(o)
                onRatingFinish()
            }
        }

        override val body: RatingBody
            get() = RatingBody(EkiApi.RatingUser).also {
                it.serial = o.SerialNumber
                it.star = star
            }
    }

    override fun setData(data: EkiOrder?) {
        order = data
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragOrderDetailPanelBinding.inflate(inflater, container, false)
        return binding
    }
}