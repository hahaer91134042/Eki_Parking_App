package com.eki.parking.Controller.activity.frag.MemberDiscount

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.DiscountBody
import com.eki.parking.Model.response.DiscountResponse
import com.eki.parking.R
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.databinding.ItemRecycleviewBinding
import com.eki.parking.extension.color
import com.eki.parking.extension.sendRequest
import com.hill.devlibs.extension.halfUp
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.time.ext.format

/**
 * Created by Hill on 2020/06/20
 */
class DiscountListFrag : SearchFrag(), IFragViewBinding {

    private lateinit var binding: ItemRecycleviewBinding

    override fun initFragView() {
        toolBarTitle = getString(R.string.coupon)
        binding.recycleView.useSimpleDivider()
        binding.recycleView.setBackgroundColor(color(R.color.light_gray4))

        EkiRequest<DiscountBody>().also {
            it.body = DiscountBody()
        }.sendRequest(
            context,
            showProgress = true,
            listener = object : OnResponseListener<DiscountResponse> {
                override fun onReTry() {

                }

                override fun onFail(errorMsg: String, code: String) {

                }

                override fun onTaskPostExecute(result: DiscountResponse) {
                    startList(result.info)
                }
            })

    }

    private fun startList(info: ArrayList<ResponseInfo.Coupon>) {
        if (info.size > 0) {
            binding.recycleView.adapter = DiscountAdaptor(context, info)
        } else {
            showToast(getString(R.string.You_have_no_coupons_available))
        }
    }

    private class DiscountAdaptor(context: Context?, var list: List<ResponseInfo.Coupon>) :
        BaseAdapter<DiscountItem>(context) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountItem =
            DiscountItem(getItemView(R.layout.item_discount, parent)).also { it.init() }

        override fun onBindViewHolder(item: DiscountItem, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(list[position])
        }

        override fun getItemCount(): Int = list.size
    }

    private class DiscountItem(itemView: View) : ItemLayout<ResponseInfo.Coupon>(itemView) {

        var discountCode = itemView.findViewById<TextView>(R.id.discountCode)
        var endDate = itemView.findViewById<TextView>(R.id.dateText)
        var amt = itemView.findViewById<TextView>(R.id.priceTextView)

        override fun init() {
        }

        override fun refresh(data: ResponseInfo.Coupon?) {
            super.refresh(data)
            data.notNull { discount ->
                discountCode.text = "${getString(R.string.Code)} ${discount.Code}"
                when (discount.IsRange) {
                    true -> endDate.text =
                        "有效日期至 ${discount.End.toDateTime().date.format("yyyy/MM/dd")}"
                    else -> endDate.text = ""
                }
                amt.text = getString(R.string.Price_format).messageFormat(discount.Amt.halfUp())
            }
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = ItemRecycleviewBinding.inflate(inflater, container, false)
        return binding
    }
}