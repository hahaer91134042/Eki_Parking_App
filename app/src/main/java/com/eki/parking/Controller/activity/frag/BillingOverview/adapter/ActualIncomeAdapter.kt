package com.eki.parking.Controller.activity.frag.BillingOverview.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.frag.BillingOverview.bean.ActualIncomeBean
import com.eki.parking.R
import com.eki.parking.databinding.ItemActualIncomeBinding
import com.squareup.picasso.Picasso

class ActualIncomeAdapter(
    private val context: Context,
    private val onClickListener:OnClickListener
): ListAdapter<ActualIncomeBean, RecyclerView.ViewHolder>(DiffCallback) {

    class OnClickListener(val clickListener: (data: ActualIncomeBean) -> Unit) {
        fun onClick(data: ActualIncomeBean) = clickListener(data)
    }

    inner class ActualIncomeViewHolder(
        private val binding: ItemActualIncomeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: ActualIncomeBean,onClickListener: OnClickListener) {

            binding.tvParkingSpaceSerialNumber.text = list.serial_number
            binding.tvParkingSpaceTitle.text = "(${list.title})"
            binding.tvNumberParkingSpaceIncome.text =
                context.getString(R.string.price,list.number_parking_space_income)
            binding.tvNumberDefaultFee.text =
                context.getString(R.string.negative_price,list.number_default_fee)
            binding.tvNumberActualIncome.text =
                context.getString(R.string.price,list.number_actual_income)

            if("" == list.picture || list.picture.isEmpty()) {
                binding.ivParkingSpacePic.setImageDrawable(context.getDrawable(R.drawable.none_img))
            }else{
                Picasso.with(context).load(list.picture).into(binding.ivParkingSpacePic)
            }

            binding.llDefaultFee.setOnClickListener {
                val intent = Intent(AppFlag.DefaultDetails)
                intent.apply {
                    putExtra("claimant",list.number_default_fee.toString())
                    putExtra("serialNumber",list.serial_number)
                }
                context.sendBroadcast(intent)
            }

            binding.root.setOnClickListener { onClickListener.onClick(list) }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ActualIncomeBean>() {
        override fun areItemsTheSame(oldItem: ActualIncomeBean, newItem: ActualIncomeBean): Boolean {
            return oldItem.number_actual_income == newItem.number_actual_income
        }

        override fun areContentsTheSame(oldItem: ActualIncomeBean, newItem: ActualIncomeBean): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ActualIncomeViewHolder(
            ItemActualIncomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ActualIncomeViewHolder -> {
                holder.bind((getItem(position) as ActualIncomeBean),onClickListener)
            }
        }
    }
}