package com.eki.parking.Controller.activity.frag.Main.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eki.parking.Model.EnumClass.WeekDay
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.databinding.ItemTitleTextviewBinding
import com.eki.parking.databinding.ItemUnpaidRecyclerViewBinding
import com.eki.parking.extension.dimen
import com.eki.parking.extension.string
import com.eki.parking.extension.toEnum
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.time.ext.formateShort


class ReserveCalendarAdapter(
    private val context: Context
) : ListAdapter<ReserveCalendarDataItem, RecyclerView.ViewHolder>(DiffCallback) {
    var seeMoreCallback: ((EkiOrder) -> Unit)? = null

    companion object DiffCallback : DiffUtil.ItemCallback<ReserveCalendarDataItem>() {
        override fun areItemsTheSame(
            oldItem: ReserveCalendarDataItem,
            newItem: ReserveCalendarDataItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ReserveCalendarDataItem,
            newItem: ReserveCalendarDataItem
        ): Boolean {
            return oldItem == newItem
        }

        private val ITEM_TITLE = 0
        private val ITEM_VIEW_LIST = 1
    }

    inner class TitleViewHolder(
        private val binding: ItemTitleTextviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(str: String) {
            binding.textView.text = str

            binding.textView.gravity = Gravity.CENTER_VERTICAL
            binding.textView.setPadding(20, 0, 0, 0)
            binding.textView.setTextColor(context.getColor(R.color.text_color_1))
            binding.textView.textSize = dimen(R.dimen.text_size_8)
        }
    }

    inner class ReserveCalendarViewHolder(
        private val binding: ItemUnpaidRecyclerViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: EkiOrder) {
            val start = order.startDateTime()
            val end = order.endDateTime()
            binding.itemUnpaidStartDateText.text =
                "${start.month.mod02d()}/${start.day.mod02d()} ${string(start.weekDay().toEnum<WeekDay>().strRes)}"
            binding.itemUnpaidEndDateText.text =
                "${end.month.mod02d()}/${end.day.mod02d()} ${string(end.weekDay().toEnum<WeekDay>().strRes)}"

            binding.itemUnpaidStartTimeText.text = "${start.time.formateShort()}"
            binding.itemUnpaidEndTimeText.text = "${end.time.formateShort()}"

            binding.unpaidMessageText.setTextColor(context.getColor(R.color.dark_gray1))
            binding.unpaidMessageText.textSize = 16F
            binding.unpaidMessageText.text = order.Address.Detail

            binding.unpaidRightButton.visibility = View.GONE

            binding.unpaidLeftButton.text = context.getString(R.string.See_more)
            binding.unpaidLeftButton.setOnClickListener {
                seeMoreCallback?.invoke(order)
            }
            binding.executePendingBindings()
        }
    }

    fun submitData(reserveList: List<EkiOrder>) {
        val reserveSize = reserveList.size
        val items: List<ReserveCalendarDataItem> =
            listOf(
                ReserveCalendarDataItem.ReserveCalendarTitle(
                    context.getString(R.string.appointment_number, reserveSize))) +
                    reserveList.map { ReserveCalendarDataItem.ReserveCalendarList(it) }
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TITLE -> {
                TitleViewHolder(
                    ItemTitleTextviewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            ITEM_VIEW_LIST -> {
                ReserveCalendarViewHolder(
                    ItemUnpaidRecyclerViewBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TitleViewHolder -> {
                holder.bind((getItem(position) as ReserveCalendarDataItem.ReserveCalendarTitle).title)
            }
            is ReserveCalendarViewHolder -> {
                holder.bind((getItem(position) as ReserveCalendarDataItem.ReserveCalendarList).list)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ReserveCalendarDataItem.ReserveCalendarTitle -> ITEM_TITLE
            is ReserveCalendarDataItem.ReserveCalendarList -> ITEM_VIEW_LIST
        }
    }
}

sealed class ReserveCalendarDataItem {
    data class ReserveCalendarTitle(val title: String) : ReserveCalendarDataItem()
    data class ReserveCalendarList(val list: EkiOrder) : ReserveCalendarDataItem()
}