package com.eki.parking.Controller.activity.frag.ConsumptionHistory.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eki.parking.Model.EnumClass.WeekDay
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.databinding.ItemUnpaidRecyclerViewBinding
import com.eki.parking.extension.string
import com.eki.parking.extension.toEnum
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.time.ext.formateShort

//By Linda
class ConsumptionAdapter(
    private val context: Context,
    private val onClickListener: OnClickListener
) : ListAdapter<EkiOrder, RecyclerView.ViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (data: EkiOrder) -> Unit) {
        fun onClick(data: EkiOrder) = clickListener(data)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<EkiOrder>() {
        override fun areItemsTheSame(oldItem: EkiOrder, newItem: EkiOrder): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: EkiOrder, newItem: EkiOrder): Boolean {
            return oldItem == newItem
        }
    }

    inner class ConsumptionViewHolder(
        private val binding: ItemUnpaidRecyclerViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: EkiOrder,onClickListener: OnClickListener) {
            binding.unpaidLeftButton.visibility = View.GONE
            binding.unpaidRightButton.visibility = View.GONE

            val start = order.startDateTime()
            val end = order.Checkout?.Date?.toDateTime() ?: order.ReservaTime.endDateTime()
            binding.itemUnpaidStartDateText.text =
                "${start.month.mod02d()}/${start.day.mod02d()} ${string(start.weekDay().toEnum<WeekDay>().strRes)}"
            binding.itemUnpaidEndDateText.text =
                "${end.month.mod02d()}/${end.day.mod02d()} ${string(end.weekDay().toEnum<WeekDay>().strRes)}"

            binding.itemUnpaidStartTimeText.text = start.time.formateShort()
            binding.itemUnpaidEndTimeText.text = end.time.toString()
                .removeRange(end.time.toString().length - 3,end.time.toString().length)

            binding.unpaidMessageText.apply {
                typeface = Typeface.DEFAULT_BOLD
                text = context.getString(R.string.cost_integer,order.Cost.toInt())
            }

            binding.root.setOnClickListener {
                onClickListener.onClick(order)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ConsumptionViewHolder(
            ItemUnpaidRecyclerViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ConsumptionViewHolder -> {
                holder.bind((getItem(position) as EkiOrder),onClickListener)
            }
        }
    }

}