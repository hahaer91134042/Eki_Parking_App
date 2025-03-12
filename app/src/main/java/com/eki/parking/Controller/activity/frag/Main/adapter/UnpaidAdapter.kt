package com.eki.parking.Controller.activity.frag.Main.adapter

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eki.parking.AppConfig
import com.eki.parking.Model.EnumClass.WeekDay
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.ExtendedOrder
import com.eki.parking.R
import com.eki.parking.View.widget.ItemCheckoutOrder
import com.eki.parking.databinding.ItemTitleTextviewBinding
import com.eki.parking.databinding.ItemUnpaidRecyclerViewBinding
import com.eki.parking.extension.dimen
import com.eki.parking.extension.hasData
import com.eki.parking.extension.string
import com.eki.parking.extension.toEnum
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.formateShort


class UnpaidAdapter(
    private val context: Context
): ListAdapter<UnpaidDataItem, RecyclerView.ViewHolder>(DiffCallback) {
    private var counterHandler= Handler(Looper.getMainLooper())
    var checkoutEvent: ItemCheckoutOrder.CheckoutEvent?=null

    companion object DiffCallback : DiffUtil.ItemCallback<UnpaidDataItem>() {
        override fun areItemsTheSame(oldItem: UnpaidDataItem, newItem: UnpaidDataItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UnpaidDataItem, newItem: UnpaidDataItem): Boolean {
            return oldItem == newItem
        }

        private val ITEM_UNPAID_TITLE = 0
        private val ITEM_VIEW_UNPAID_LIST = 1
        private val ITEM_UN_CHECKOUT_TITLE = 2
        private val ITEM_VIEW_UN_CHECKOUT_LIST = 3
    }

    inner class UnpaidTitleViewHolder(
        private val binding: ItemTitleTextviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(str: String) {
            binding.textView.text = str

            binding.textView.gravity = Gravity.CENTER_VERTICAL
            binding.textView.setPadding(20, 0, 0, 0)
            binding.textView.setTextColor(context.getColor(R.color.text_color_1))
            binding.textView.setBackgroundColor(context.getColor(R.color.light_gray4))
            binding.textView.textSize = dimen(R.dimen.text_size_8)
        }
    }

    inner class UnpaidViewHolder(
        private val binding: ItemUnpaidRecyclerViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: EkiOrder) {
            val start = order.startDateTime()
            val end = order.Checkout?.Date?.toDateTime() ?: order.ReservaTime.endDateTime()
            binding.itemUnpaidStartDateText.text =
                "${start.month.mod02d()}/${start.day.mod02d()} ${string(start.weekDay.toEnum<WeekDay>().strRes)}"
            binding.itemUnpaidEndDateText.text =
                "${end.month.mod02d()}/${end.day.mod02d()} ${string(end.weekDay.toEnum<WeekDay>().strRes)}"

            binding.itemUnpaidStartTimeText.text = "${start.time.formateShort()}"
            binding.itemUnpaidEndTimeText.text = "${end.time.formateShort()}"

            binding.unpaidMessageText.setTextColor(context.getColor(R.color.gray_8a8a8a))
            binding.unpaidMessageText.setTypeface( null,Typeface.BOLD)
            binding.unpaidMessageText.textSize = 16F
            binding.unpaidMessageText.text = context.getString(R.string.cost_integer,order.Cost.toInt())

            binding.unpaidRightButton.visibility = View.GONE

            binding.unpaidLeftButton.text = context.getString(R.string.Go_pay)
            binding.unpaidLeftButton.setOnClickListener {
                checkoutEvent?.onCheckout(order)
            }
            binding.executePendingBindings()
        }
    }

    inner class UnCheckoutTitleViewHolder(
        private val binding: ItemTitleTextviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(str: String) {
            binding.textView.text = str

            binding.textView.gravity = Gravity.CENTER_VERTICAL
            binding.textView.setPadding(20, 0, 0, 0)
            binding.textView.setTextColor(context.getColor(R.color.text_color_1))
            binding.textView.setBackgroundColor(context.getColor(R.color.light_gray4))
            binding.textView.textSize = dimen(R.dimen.text_size_8)
        }
    }

    inner class UnCheckoutViewHolder(
        private val binding: ItemUnpaidRecyclerViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: EkiOrder) {
            val start = order.startDateTime()
            val end = order.endDateTime()
            binding.itemUnpaidStartDateText.text =
                "${start.month.mod02d()}/${start.day.mod02d()} ${string(start.weekDay.toEnum<WeekDay>().strRes)}"
            binding.itemUnpaidEndDateText.text =
                "${end.month.mod02d()}/${end.day.mod02d()} ${string(end.weekDay.toEnum<WeekDay>().strRes)}"

            binding.itemUnpaidStartTimeText.text = "${start.time.formateShort()}"
            binding.itemUnpaidEndTimeText.text = "${end.time.formateShort()}"

            binding.unpaidMessageText.setTextColor(context.getColor(R.color.Eki_red_1))
            binding.unpaidMessageText.setTypeface( null,Typeface.BOLD)
            binding.unpaidMessageText.textSize = 16F

            binding.unpaidRightButton.text = context.getString(R.string.Go_checkout)
            binding.unpaidLeftButton.text = context.getString(R.string.Go_extension)

            binding.unpaidLeftButton.setOnClickListener {
                checkoutEvent?.onExtenTime(order)
            }
            binding.unpaidRightButton.setOnClickListener {
                checkoutEvent?.onCheckout(order)
            }

            if (order.isBeCheckOut()) {
                counterHandler.post(object : Runnable {
                    override fun run() {
                        val now = DateTime.now()
                        val min = (order.endDateTime() - now).totalMinutes

                        if (AppConfig.openOrderExtenMin > min && min > 0.0) {
                            if (ExtendedOrder.creatBy(order).hasData()) {
                                binding.unpaidLeftButton.visibility = View.GONE
                            } else {
                                binding.unpaidLeftButton.visibility = View.VISIBLE
                            }

                            binding.unpaidMessageText.text =
                                context.getString(R.string.minutes_remaining,min.toInt())
                            counterHandler.postDelayed(this, 1000)
                        } else {
                            binding.unpaidMessageText.text=""
                            binding.unpaidLeftButton.visibility= View.GONE
                            counterHandler.removeCallbacks(this)
                        }
                    }
                })
            }
            binding.executePendingBindings()
        }
    }

    fun submitData(unPaidList: List<EkiOrder>, unCheckoutList: List<EkiOrder>) {
        val unpaidSize = unPaidList.size
        val unCheckoutSize = unCheckoutList.size
        val items: List<UnpaidDataItem> =
            listOf(UnpaidDataItem.UnpaidTitle(context.getString(R.string.Unpaid_orders, unpaidSize))) +
                    unPaidList.map { UnpaidDataItem.UnpaidList(it) } +
                    listOf(UnpaidDataItem.UnCheckoutTitle(
                        context.getString(R.string.Orders_not_return, unCheckoutSize))) +
                    unCheckoutList.map { UnpaidDataItem.UnCheckoutList(it) }
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_UNPAID_TITLE -> {
                UnpaidTitleViewHolder(
                    ItemTitleTextviewBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            ITEM_VIEW_UNPAID_LIST -> {
                UnpaidViewHolder(ItemUnpaidRecyclerViewBinding.inflate(
                    LayoutInflater.from(parent.context),parent,false)
                )
            }
            ITEM_UN_CHECKOUT_TITLE -> {
                UnCheckoutTitleViewHolder(
                    ItemTitleTextviewBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            ITEM_VIEW_UN_CHECKOUT_LIST -> {
                UnCheckoutViewHolder(ItemUnpaidRecyclerViewBinding.inflate(
                    LayoutInflater.from(parent.context),parent,false)
                )
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is UnpaidTitleViewHolder -> {
                holder.bind((getItem(position) as UnpaidDataItem.UnpaidTitle).title)
            }
            is UnpaidViewHolder -> {
                holder.bind((getItem(position) as UnpaidDataItem.UnpaidList).order)
            }
            is UnCheckoutTitleViewHolder -> {
                holder.bind((getItem(position) as UnpaidDataItem.UnCheckoutTitle).title)
            }
            is UnCheckoutViewHolder -> {
                holder.bind((getItem(position) as UnpaidDataItem.UnCheckoutList).order)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UnpaidDataItem.UnpaidTitle -> ITEM_UNPAID_TITLE
            is UnpaidDataItem.UnpaidList -> ITEM_VIEW_UNPAID_LIST
            is UnpaidDataItem.UnCheckoutTitle -> ITEM_UN_CHECKOUT_TITLE
            is UnpaidDataItem.UnCheckoutList -> ITEM_VIEW_UN_CHECKOUT_LIST
        }
    }
}

sealed class UnpaidDataItem {
    data class UnpaidTitle(val title: String) : UnpaidDataItem()
    data class UnpaidList(val order: EkiOrder) : UnpaidDataItem()
    data class UnCheckoutTitle(val title: String) : UnpaidDataItem()
    data class UnCheckoutList(val order: EkiOrder) : UnpaidDataItem()
}