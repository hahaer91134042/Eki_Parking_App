package com.eki.parking.Controller.activity.frag.Reserva

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppConfig
import com.eki.parking.Controller.activity.frag.Reserva.adaptor.VehicleSelectAdaptor
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.DTO.OrderReservaTime
import com.eki.parking.Model.EnumClass.CurrencyUnit
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.OrderAddBody
import com.eki.parking.Model.response.OrderAddResponse
import com.eki.parking.Model.sql.EkiLocation
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.databinding.FragSendReservaBinding
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.sqlAppend
import com.eki.parking.extension.sqlData
import com.eki.parking.extension.sqlSaveOrUpdate
import com.hill.devlibs.extension.*
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.format
import com.hill.devlibs.time.ext.formateShort
import com.hill.devlibs.time.ext.minSpan
import com.hill.devlibs.util.TimeUtil

/**
 * Created by Hill on 06,11,2019
 */

class SendReservaFrag : SearchFrag(), ISetData<OrderReservaTime>, IFragViewBinding {

    private lateinit var binding: FragSendReservaBinding
    private lateinit var orderReservaTime: OrderReservaTime
    private lateinit var loc: EkiLocation
    private lateinit var start: DateTime
    private lateinit var end: DateTime

    override fun setData(data: OrderReservaTime?) {
        data.notNull {
            orderReservaTime = it
        }
    }

    fun setReservaTime(s: DateTime, e: DateTime) {
        start = s
        end = e
    }

    fun setLocation(location: EkiLocation) {
        loc = location
    }

    override fun onResumeFragView() {
        toolBarTitle = getString(R.string.Book_a_trip)
    }

    override fun initFragView() {

        binding.addressText.text = "${loc.Address?.City}${loc.Address?.Detail}"
        binding.parkingNumText.text = "${loc.Info?.SerialNumber}"
        binding.reservaStartText.text =
            "${start.date.format(TimeUtil.dateFormat)} ${start.time.formateShort()}"
        binding.reservaEndText.text = "${end.date.format(TimeUtil.dateFormat)} ${end.time.formateShort()}"

        val countSpan = AppConfig.ReservaGapMin.minSpan
        val span = end - start
        val amount = when (loc.Config?.currencyUnit) {
            CurrencyUnit.USD -> loc.Config?.Price?.times((span / countSpan))!!//有含小數
            else -> loc.Config?.Price?.times((span / countSpan))!!.ceil()
        }
        binding.amountText.text =
            getString(R.string.Amount_price_format).messageFormat(loc.Config?.Price!!, amount)

        binding.totalAppointmentText.text = getString(R.string.Hour_formate).messageFormat(span.totalHour)

        binding.recycleView.useVerticalView()

        sqlData<EkiMember>().notNull { member ->
            if (member.vehicle.size > 0) {
                if (member.vehicle.any { it.IsDefault }) {
                    val carNumber = member.vehicle.first { it.IsDefault }.Number
                    binding.carNumText.text = carNumber
                    checkCheckOutValid(carNumber)
                }
            }

            binding.recycleView.adapter = VehicleSelectAdaptor(context, member.vehicle).apply {
                onNumberChange = { carNumber ->
                    binding.carNumText.text = carNumber
                    checkCheckOutValid(carNumber)
                }
            }
        }

        binding.checkoutBtn.setOnClickListener {
            orderReservaTime.carNum = binding.carNumText.text.toString().cleanTex
            goReservaTask(orderReservaTime)
        }

        setNotice()
    }

    private fun checkCheckOutValid(carNumber: String) {
        carNumber.isNullOrEmpty {
            binding.checkoutBtn.isEnabled = false
        }.onFalse {
            binding.checkoutBtn.isEnabled = true
        }
    }

    private fun goReservaTask(reservaData: OrderReservaTime) {

        EkiRequest<OrderAddBody>().apply {
            body = OrderAddBody().apply { times.add(reservaData) }
        }.sendRequest(context, true, object : OnResponseListener<OrderAddResponse> {
            override fun onReTry() {

            }

            override fun onTaskPostExecute(result: OrderAddResponse) {
                //--目前 都是單筆預約--
                result.notNull { response ->
                    if (response.info[0].Success) {
                        //之後可能要新增訂單到sql

                        val order = response.info[0].Order?.toSql()
                        if (order != null) {
                            order.sqlAppend()

                            app.timeAlarmManager.addOrder(order)

                            showToast(getString(R.string.Successful_appointment))
                            toMainActivity()
                        } else {
                            showToast(getString(R.string.Appointment_failed_during_this_time_please_reselect))
                            backFrag()
                        }
                    } else {
                        response.info[0].LoadData?.toSql()?.sqlSaveOrUpdate()
                        showToast(getString(R.string.Appointment_failed_during_this_time_please_reselect))
                        backFrag()
                    }
                }
            }

            override fun onFail(errorMsg: String, code: String) {
                showToast(errorMsg)
                backFrag()
            }
        })

    }

    private fun setNotice() {
        binding.textView.textSize = 12f
        binding.textView.setLines(4)
        binding.textView.isSingleLine = false

        val str = "<font color=\"#707070\">" + "註 : 因故無法如期停車請" + "</font>" +
                "<font color=\"#d35757\">" + "務必於預約時間開始前取消預約" + "</font>" +
                "<font color=\"#707070\">" + "，" + "</font>" + "<br>" +
                "<font color=\"#d35757\">" + "若要取消請至訂單取消，否則預約開始後將視為一般停車開始計費。" + "</font>" + "<br>" +
                "<font color=\"#707070\">" + "註 : 若對該車位有任何疑慮，" + "</font>" +
                "<font color=\"#d35757\">" + "請自行拍照以保障雙方權利" + "</font>" +
                "<font color=\"#707070\">" + "。" + "</font>"

        binding.textView.text = Html.fromHtml(str)
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragSendReservaBinding.inflate(inflater, container, false)
        return binding
    }
}