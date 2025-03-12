package com.eki.parking.Controller.activity.frag.BillingOverview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eki.parking.Controller.activity.frag.BillingOverview.bean.DefaultDetailsBean
import com.eki.parking.R
import com.eki.parking.databinding.ItemDefaultDetailsBinding

class DefaultDetailsAdapter(
    private val context: Context
): ListAdapter<DefaultDetailsBean, RecyclerView.ViewHolder>(DiffCallback) {

    inner class DefaultDetailsViewHolder(
        private val binding: ItemDefaultDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: DefaultDetailsBean) {
            binding.tvDefaultDetailsDatetime.text = list.default_details_datetime
            binding.tvDefaultDetailsPrice.text =
                context.getString(R.string.price,list.default_details_price)
            binding.tvCancellationTime.text =
                context.getString(R.string.Cancellation_time, list.cancellation_time)

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DefaultDetailsBean>() {
        override fun areItemsTheSame(oldItem: DefaultDetailsBean, newItem: DefaultDetailsBean): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DefaultDetailsBean, newItem: DefaultDetailsBean): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DefaultDetailsViewHolder(
            ItemDefaultDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DefaultDetailsViewHolder -> {
                holder.bind((getItem(position) as DefaultDetailsBean))
            }
        }
    }
}