package com.eki.parking.Controller.activity.frag.BillingOverview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.BillingOverview.adapter.DefaultDetailsAdapter
import com.eki.parking.Controller.activity.frag.BillingOverview.bean.DefaultDetailsBean
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.ManagerLocMulctOrderResponse
import com.eki.parking.R
import com.eki.parking.databinding.FragDefaultDetailsBinding
import com.eki.parking.extension.sendRequest
import com.eki.parking.utils.Logger
import com.eki.parking.utils.TimeUtils
import com.hill.devlibs.extension.showToast
import com.hill.devlibs.impl.IFragViewBinding
import java.util.*

class DefaultDetailsFrag : SearchFrag(),IFragViewBinding {

    private lateinit var binding: FragDefaultDetailsBinding
    var claimant = 0.0
    var serialNumber = ""
    var startTime = ""
    var endTime = ""

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragDefaultDetailsBinding.inflate(inflater,container,false)
        return binding
    }

    override fun initFragView() {
        toolBarTitle = getResString(R.string.Default_details)
        initView()
    }

    fun getData(claimant:String,serialNumber:String) {
        this.claimant = claimant.toDouble()
        this.serialNumber = serialNumber
    }

    private var defaultDetailList: MutableList<DefaultDetailsBean> = mutableListOf()
    private lateinit var defaultDetailsAdapter: DefaultDetailsAdapter

    private fun initView() {
        if (claimant <= 0.0) {
            binding.defaultProgress.relativeLayout.visibility = View.GONE
            binding.defaultDetailRefreshView.refreshView.isEnabled = false
            binding.defaultZeroLayout.linearLayout.visibility = View.VISIBLE
        } else {
            binding.defaultDetailRefreshView.refreshView.isEnabled = true
            binding.defaultZeroLayout.linearLayout.visibility = View.GONE
            setSwipeRecyclerView()
            getResponseData()
        }
    }

    private fun setSwipeRecyclerView() {
        defaultDetailList = mutableListOf<DefaultDetailsBean>()
        defaultDetailsAdapter = DefaultDetailsAdapter(requireContext())
        binding.defaultDetailRefreshView.recycleView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.defaultDetailRefreshView.recycleView.adapter = defaultDetailsAdapter

        binding.defaultDetailRefreshView.refreshView.setOnRefreshListener{
            binding.defaultDetailRefreshView.refreshView.isRefreshing = false
            getResponseData()
        }
    }

    private fun getResponseData() {
        EkiRequest<SendIdBody>().also {
            it.body = SendIdBody(EkiApi.ManagerLocMulctOrder)
            it.body.serNum.add(serialNumber)
            it.body.times = listOf(RequestBody.TimeSpan().apply {
                start = startTime
                end = endTime
            })
        }.sendRequest(context, showProgress = false, listener = object :
            OnResponseListener<ManagerLocMulctOrderResponse> {
            override fun onReTry() {
                binding.defaultProgress.relativeLayout.visibility = View.VISIBLE
            }
            override fun onFail(errorMsg: String, code: String) {
                binding.defaultProgress.relativeLayout.visibility = View.GONE
            }
            override fun onTaskPostExecute(result: ManagerLocMulctOrderResponse) {
                if (result.info.isNotEmpty()) {
                    var dateTime: String
                    var amount: Int
                    var cancelTime: String

                    for (i in result.info.indices) {

                        val liSerNum = result.info[i].SerNum
                        val liResult = result.info[i].Result

                        if (serialNumber == liSerNum) {
                            for (j in liResult.indices) {
                                dateTime =
                                    formatDateTime(liResult[j].Start) + " - " +
                                            formatDateTime(liResult[j].End)
                                amount = liResult[j].Amt.toInt()
                                cancelTime = formatDateTime(liResult[j].CancelTime)

                                defaultDetailList.add(DefaultDetailsBean(dateTime, amount, cancelTime))
                            }
                        }
                    }
                    defaultDetailsAdapter.submitList(defaultDetailList)
                } else {
                    requireContext().showToast(getString(R.string.No_data_available_on_this_date))
                }
                binding.defaultProgress.relativeLayout.visibility = View.GONE
            }
        })
    }

    private fun formatDateTime(dateTime:String):String {
        Logger.i("default detail dateTime=$dateTime")
        var str = dateTime
        if (dateTime.contains("T")) {
            str = dateTime.replace("T"," ")
        }
        val stamp = TimeUtils.dashDateTimeToStamp(str,Locale.getDefault())
        return TimeUtils.stampToDateDefault(stamp, Locale.getDefault())
    }
}