package com.eki.parking.Controller.activity.frag.BillingOverview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.response.ManagerLocIncomeResponse
import com.eki.parking.R
import com.eki.parking.databinding.FragLocincomeBinding
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateUnit


/**
 * Created by Hill on 2021/01/20
 *
 * 和DefaultDetails同版型
 */
class LocIncomeFrag:SearchFrag(), ISetData<ManagerLocIncomeResponse.IncomeResult>,IFragViewBinding {

    private var incomeData: ResponseInfo.LocIncome?=null
    private var income = 0
    private var claimant = 0
    private var totalIncome = 0
    private var start: DateUnit? = null
    private var locSerNum = ""
    var receiveData = ManagerLocIncomeResponse.IncomeResult()
    private lateinit var binding:FragLocincomeBinding

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragLocincomeBinding.inflate(inflater,container,false)
        return binding
    }

    override fun initFragView() {
        super.initFragView()

        getData()
        initView()

        binding.locIncomeProgress.relativeLayout.visibility = View.GONE
    }

    override fun setData(data: ManagerLocIncomeResponse.IncomeResult?) {
        incomeData =data?.Result?.firstOrNull()
    }

    private fun getData(){

        incomeData = receiveData.Result.firstOrNull()
        locSerNum = receiveData.SerNum

        income=incomeData?.Income?.toInt() ?:0
        claimant=incomeData?.Claimant?.toInt() ?:0

        start=incomeData?.Start?.toDateTime()?.date

        totalIncome= income -claimant
    }

    private fun initView() {
        toolBarTitle="${start?.year}年${start?.month}月"

        binding.incomeText.text = getString(R.string.price,income)
        binding.claimantText.text = getString(R.string.negative_price,claimant)
        binding.totalAmountText.text = getString(R.string.price,totalIncome)

        if (claimant <= 0) {
            binding.locIncomeSwipeLayout.isEnabled = false
            binding.locIncomeRecycleView.visibility = View.GONE
        }
    }
}