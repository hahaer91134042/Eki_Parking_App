package com.eki.parking.Controller.activity.frag.BillingOverview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.BillingOverview.adapter.ActualIncomeAdapter
import com.eki.parking.Controller.activity.frag.BillingOverview.bean.ActualIncomeBean
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.ManagerLocIncomeResponse
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.databinding.FragActualIncomeBinding
import com.eki.parking.extension.*
import com.hill.devlibs.extension.showToast
import com.hill.devlibs.impl.IFragViewBinding
import java.util.*

class ActualIncomeFrag : SearchFrag(),IFragViewBinding {

    private var intYear: Int = 0
    private var intMonth: Int = 0
    private var intDay: Int = 0
    var startDate: String = ""
    var endDate: String = ""
    private var currentDateTime: String = ""
    private lateinit var binding: FragActualIncomeBinding
    private lateinit var actualIncomeAdapter: ActualIncomeAdapter

    var onStoreIncomeDate:((String,String) -> Unit)? = null

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragActualIncomeBinding.inflate(inflater, container, false)
        actualIncomeAdapter = ActualIncomeAdapter(requireContext(),ActualIncomeAdapter.OnClickListener{
        })

        return binding
    }

    override fun initFragView() {
        super.initFragView()

        initView()
        initCalender()
        setTextViewListener()
        initDate()
        setBtn()
    }

    fun initView() {

        toolBarTitle = getString(R.string.Actual_income_of_each_parking_space)

        binding.tvQueryIncomeTime.text = getString(R.string.Query_income_time)
        binding.tvQueryIncomeTime.setTextAppearance(R.style.headerTexStyle)
        binding.incomeProgressBar.progress.visibility = View.VISIBLE
    }

    private fun initCalender() {

        val calendar = Calendar.getInstance()

        intYear = calendar[Calendar.YEAR]
        intMonth = calendar[Calendar.MONTH] + 1
        intDay = calendar[Calendar.DATE]
    }

    fun format2Digits(number: Int): String {
        return String.format("%02d", number)
    }

    fun initDate() {  // month需 + 1

        //格式: 月/日/年
        currentDateTime = getString(
            R.string.date_time_slash,
            format2Digits(intMonth),
            format2Digits(intDay),
            intYear.toString()
        )

        val startList = startDate.split("-")
        val endList = endDate.split("-")

        if ((startDate == "" || endDate == "") ||
            (startList.size !=3 || endList.size != 3)) {
            //初始日期為當前時間
            binding.tvHeaderDateStart.text = currentDateTime
            binding.tvHeaderDateEnd.text = currentDateTime

            //請求時間格式:年-月-日
            startDate = getString(
                R.string.date_time_dash,
                intYear.toString(),
                format2Digits(intMonth),
                format2Digits(intDay)
            )
            endDate = getString(
                R.string.date_time_dash,
                intYear.toString(),
                format2Digits(intMonth),
                format2Digits(intDay)
            )
        } else {
            binding.tvHeaderDateStart.text = getString(
                R.string.date_time_slash,
                format2Digits(startList[1].toInt()),
                format2Digits(startList[2].toInt()),
                startList[0]
            )
            binding.tvHeaderDateEnd.text = getString(
                R.string.date_time_slash,
                format2Digits(endList[1].toInt()),
                format2Digits(endList[2].toInt()),
                endList[0]
            )
            replaceChildFrag()
        }
        binding.incomeProgressBar.relativeLayout.visibility = View.GONE
        onStoreIncomeDate?.invoke(startDate,endDate)
    }

    private fun setTextViewListener() {
        binding.tvHeaderDateStart.setOnClickListener {
            setDatePicker(0)
        }
        binding.tvHeaderDateEnd.setOnClickListener {
            setDatePicker(1)
        }
    }

    private fun setDatePicker(flag: Int) {

        val dialogView = View.inflate(context, R.layout.dialog_calender_datepicker, null)
        val datePicker = dialogView?.findViewById<DatePicker>(R.id.datePicker)

        // month需 - 1
        datePicker?.init(intYear, intMonth - 1, intDay) { _, year, month, day ->
            setDateChange(month, day, year, flag)
        }

        val actualIncomeAlertDialog = ActualIncomeSetAlertDialog()
        val builder = actualIncomeAlertDialog.setAlertDialogBuilder(context)
        dialogView?.let { builder.let { it1 -> actualIncomeAlertDialog.setAlertDialog(it, it1) } }
    }

    private fun setDateChange(monthOfYear: Int, dayOfMonth: Int, year: Int, flag: Int) {

        currentDateTime = getString(
            R.string.date_time_slash,
            format2Digits(monthOfYear + 1),
            format2Digits(dayOfMonth),
            year.toString()
        )

        if (flag == 0) {

            startDate = getString(
                R.string.date_time_dash,
                year.toString(),
                format2Digits(monthOfYear + 1),
                format2Digits(dayOfMonth)
            )

            binding.tvHeaderDateStart.text = currentDateTime

        } else {

            endDate = getString(
                R.string.date_time_dash,
                year.toString(),
                format2Digits(monthOfYear + 1),
                format2Digits(dayOfMonth)
            )

            binding.tvHeaderDateEnd.text = currentDateTime

        }

        onStoreIncomeDate?.invoke(startDate,endDate)

    }

    //將起訖時間送入請求
    private fun setBtn() {

        val pattern = "yyyy-MM-dd"

        binding.incomeDetermineButton.btnGreen4.setOnClickListener {
            binding.incomeProgressBar.relativeLayout.visibility = View.VISIBLE
            if (compareBiggerOrSameDate(startDate, endDate, pattern)) {//判斷起始時間有無超過結束時間
                this.replaceChildFrag()
            } else if (!compareBiggerOrSameDate(startDate, endDate, pattern)) {
                requireContext().showToast(string(R.string.Date_End_Greater_Start))
            }
            binding.incomeProgressBar.relativeLayout.visibility = View.GONE
        }
    }

    private fun replaceChildFrag() {
        binding.incomeRecyclerView.refreshView.setSize(Int.MIN_VALUE)
        binding.incomeRecyclerView.refreshView.setProgressViewOffset(false
            , 0
            , (binding.incomeRecyclerView.refreshView.resources.displayMetrics.heightPixels*0.35).toInt()
        )
        binding.incomeRecyclerView.refreshView.setColorSchemeResources(R.color.Eki_green_2, R.color.Eki_green_2, R.color.Eki_green_2)
        binding.incomeRecyclerView.refreshView.setOnRefreshListener {

            binding.incomeRecyclerView.refreshView.isRefreshing = false
            getData(requireContext())
        }

        binding.incomeProgressBar.progress.visibility = View.VISIBLE
        binding.incomeRecyclerView.recycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.incomeRecyclerView.recycleView.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(requireContext(),R.anim.income_recycler_view)

        getData(requireContext())
    }

    fun getData(context: Context) {

        val arrManagerLocation = sqlDataList<ManagerLocation>()
        val arrSerNum = ArrayList<String>() //db的車位編號

        //準備發請求
        if( arrManagerLocation.size > 0) {
            arrManagerLocation.forEach { location ->
                arrSerNum.add(location.Info?.SerialNumber ?: "")
            }
        }

        EkiRequest<SendIdBody>().also {
            it.body = SendIdBody(EkiApi.ManagerLocIncome)
            it.body.serNum = arrSerNum
            it.body.times = ArrayList()
            it.body.times = listOf(RequestBody.TimeSpan().apply {
                start = startDate
                end = endDate
            })
        }.sendRequest(context, showProgress = false, listener = object :
            OnResponseListener<ManagerLocIncomeResponse> {
            override fun onReTry() {
                binding.incomeProgressBar.relativeLayout.visibility = View.VISIBLE
                binding.incomeRecyclerView.refreshView.isRefreshing = true
            }

            override fun onFail(errorMsg: String, code: String) {
                binding.incomeProgressBar.relativeLayout.visibility = View.GONE
                binding.incomeRecyclerView.refreshView.isRefreshing = false
            }

            override fun onTaskPostExecute(result: ManagerLocIncomeResponse) {
                if (result.info.size != 0) {

                    //response 資料暫存
                    val resultIncome = mutableListOf<Double>()
                    val resultDefaultFee = mutableListOf<Double>()
                    val resultSerNum = mutableListOf<String>()
                    result.info.forEach { r ->
                        resultIncome.add(r.Result.sumOf { it.Income })
                        resultDefaultFee.add(r.Result.sumOf { it.Claimant })
                        resultSerNum.add(r.SerNum)
                    }

                    //database 資料暫存
                    val dbSerNum = mutableListOf<String>()
                    val dbContent = mutableListOf<String>()
                    val dbPicture = mutableListOf<String>()
                    arrManagerLocation.forEach {
                        dbSerNum.add(it.Info?.SerialNumber ?: "")
                        dbContent.add(it.Info?.Content ?: "")
                        dbPicture.add( if (it.Img.isNotEmpty()) {
                            it.Img[0].Url
                        } else {
                            ""
                        })
                    }

                    //資料比對和整理,準備輸進Adapter
                    var listSerNum = ""
                    var listContent = ""
                    var listPicture = ""
                    var totalAmount = 0
                    var defaultFee = 0
                    val listData = mutableListOf<ActualIncomeBean>()

                    for ( index in dbSerNum.indices) {
                        for ( num in resultSerNum.indices) {
                            if (resultSerNum[num] == dbSerNum[index] ) {
                                listSerNum = dbSerNum[index]
                                listContent = dbContent[index]
                                listPicture = dbPicture[index]
                                totalAmount = resultIncome[index].toCurrency(EkiOrder().currencyUnit).toInt()
                                defaultFee = resultDefaultFee[index].toCurrency(EkiOrder().currencyUnit).toInt()

                                listData.add(
                                    ActualIncomeBean(listSerNum
                                    , listContent, listPicture
                                    , totalAmount
                                    , defaultFee
                                    , totalAmount - defaultFee)
                                )
                            }
                        }
                    }

                    binding.incomeRecyclerView.recycleView.adapter = actualIncomeAdapter
                    actualIncomeAdapter.submitList(listData)

                    binding.incomeProgressBar.relativeLayout.visibility = View.GONE
                    binding.incomeRecyclerView.refreshView.isRefreshing = false
                }
            }
        })
    }
}