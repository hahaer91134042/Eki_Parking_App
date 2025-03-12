package com.eki.parking.Controller.activity.frag.DiscountSelect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.R
import com.eki.parking.View.ViewType
import com.eki.parking.View.recycleview.adapter.ViewTypeAdaptor
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.databinding.ItemRecycleviewBinding
import com.eki.parking.extension.color
import com.hill.devlibs.extension.halfUp
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.IRecycleViewModelSet
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.format

/**
 * Created by Hill on 2020/08/19
 */
class DiscountSelectListFrag : SearchFrag(), ISetData<ArrayList<ResponseInfo.Coupon>>,
    IFragViewBinding {

    private lateinit var binding: ItemRecycleviewBinding
    private var discountList: ArrayList<ResponseInfo.Coupon>? = null
    var onScanClick: (() -> Unit)? = null
    var onDiscountSelect: ((ResponseInfo.Coupon) -> Unit)? = null


    override fun initFragView() {
        toolBarTitle = getString(R.string.coupon)
        binding.recycleView.useVerticalView()
        binding.recycleView.setBackgroundColor(color(R.color.light_gray4))

        discountList.notNull { startList(it) }
    }

    private fun startList(info: ArrayList<ResponseInfo.Coupon>) {
        if (info.size > 0) {
            var list = ArrayList<SelectModel>()
            info.forEach { dis ->
                list.add(SelectModel(ViewType.item, dis))
            }
            list.add(SelectModel(ViewType.edit, null))

            binding.recycleView.adapter = DiscountAdaptor(list).apply {
                setItemListClickListener {
                    list[it].data.notNull { dis ->
                        if (dis.IsRange) {
                            if (dis.End.toDateTime().date >= DateTime.now().date)
                                onDiscountSelect?.invoke(dis)
                            else
                                showToast("該折價券時法使用")
                        } else {
                            onDiscountSelect?.invoke(dis)
                        }
                    }
                }
            }
        } else {
            showToast(getString(R.string.You_have_no_coupons_available))
        }
    }

    private inner class DiscountAdaptor(private var list: List<SelectModel>) :
        ViewTypeAdaptor<ResponseInfo.Coupon>(context) {
        override val modelList: ModelList<ResponseInfo.Coupon>
            get() = ModelList(list)
        override val viewSets: SetList<ResponseInfo.Coupon>
            get() = SetList(
                object : ItemTypeSet<ResponseInfo.Coupon> {
                    override val viewType: Int
                        get() = ViewType.item

                    override fun itemBack(parent: ViewGroup): ItemLayout<ResponseInfo.Coupon> =
                        DiscountItem(
                            getItemView(
                                R.layout.item_discount2,
                                parent
                            )
                        ).also { it.init() }
                }, object : ItemTypeSet<ResponseInfo.Coupon> {
                    override val viewType: Int
                        get() = ViewType.edit

                    override fun itemBack(parent: ViewGroup): ItemLayout<ResponseInfo.Coupon> =
                        SelectItem(
                            getItemView(
                                R.layout.item_discount_select_btn,
                                parent
                            )
                        ).also { it.init() }
                })
    }

    private data class SelectModel(
        override val viewType: Int,
        override val data: ResponseInfo.Coupon?
    ) : IRecycleViewModelSet<ResponseInfo.Coupon>

    private class DiscountItem(itemView: View) : ItemLayout<ResponseInfo.Coupon>(itemView) {

        var discountCode = itemView.findViewById<TextView>(R.id.discountCode)
        var endDate = itemView.findViewById<TextView>(R.id.dateText)
        var amt = itemView.findViewById<TextView>(R.id.priceTextView)
        var parent = itemView.findViewById<View>(R.id.parentView)

        override fun init() {
            parent.setOnClickListener {
                itemClick()
            }
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

    private inner class SelectItem(itemView: View) : ItemLayout<ResponseInfo.Coupon>(itemView) {

        private var determinBtn = itemView.findViewById<Button>(R.id.determinBtn)
        override fun init() {
            determinBtn.setOnClickListener {
                onScanClick?.invoke()
            }
        }

        override fun refresh(data: ResponseInfo.Coupon?) {
            super.refresh(data)


        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = ItemRecycleviewBinding.inflate(inflater, container, false)
        return binding
    }

    override fun setData(data: ArrayList<ResponseInfo.Coupon>?) {
        discountList = data
    }
}