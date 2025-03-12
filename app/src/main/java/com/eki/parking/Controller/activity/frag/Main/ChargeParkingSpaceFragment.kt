package com.eki.parking.Controller.activity.frag.Main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.EkiMsgDialog
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.ChargeBody
import com.eki.parking.Model.response.CheckChargeResponse
import com.eki.parking.Model.response.StartChargeResponse
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.databinding.FragmentChargeParkingSpaceBinding
import com.eki.parking.extension.checkOutOrder
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.show
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData


/**
 * Created by Linda
 * */
class ChargeParkingSpaceFragment : SearchFrag(), ISetData<ArrayList<EkiOrder>>,IFragViewBinding {

    private lateinit var binding:FragmentChargeParkingSpaceBinding
    lateinit var toMain: () -> Unit
    lateinit var token:String
    private val serNum = ArrayList<String>()
    private var chargeStatus = ""
    private var onTime = false
    private lateinit var order: EkiOrder

    companion object {
        const val leave = 0
        const val goCheckOut = 1
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragmentChargeParkingSpaceBinding.inflate(inflater,container,false)
        return binding
    }

    override fun initFragView() {
        super.initFragView()

        initText()
        initChargeButton()
        checkCharge()

        if (!onTime) {
            binding.chargeText.text = getString(R.string.appointment_time_not_yet)
            setChargeButton(ChargeStatus.Unavailable.name)
        }
    }

    private fun initText() {
        if (::order.isInitialized) {
            binding.parkingLocationText.text = order.Address.Detail
            binding.appointmentStartText.text = order.ReservaTime.StartTime
            binding.appointmentEndText.text = order.ReservaTime.EndTime
            binding.estimatedCostText.text = getString(R.string.Estimated_cost_integer,order.Cost.toInt())
        }
    }

    private fun initChargeButton() {
        binding.chargeButton.setOnClickListener {
            if (serNum.isNotEmpty()) {
                checkCharge()
            }
        }

        binding.estimateButton.setOnClickListener {
            if (chargeStatus != ChargeStatus.Charging.name) {
                setDialog(goCheckOut)
            }
        }
        binding.chargeCloseButton.setOnClickListener {
            setDialog(leave)
        }
    }

    private fun setChargeButton(chargeStatus:String) {
        when (chargeStatus) {
            ChargeStatus.Preparing.name -> {
                binding.chargeButton.text = getString(R.string.open_charge)
            }
            ChargeStatus.Charging.name -> {
                binding.chargeButton.text = getString(R.string.charging)
                binding.chargeButton.visibility = View.GONE
                binding.chargeCarImage.setImageDrawable(
                    ResourcesCompat.getDrawable(resources,R.drawable.charging,null))
                binding.chargeText.text = ""

                binding.estimateButton.setBackgroundColor(getColor(R.color.light_gray6))
                binding.estimateButton.setTextColor(getColor(R.color.light_gray5))
            }
            ChargeStatus.Available.name
                ,ChargeStatus.SuspendedEV.name -> {
                binding.chargeButton.text = getString(R.string.Restart)
                binding.chargeCarImage.setImageDrawable(
                    ResourcesCompat.getDrawable(resources,R.drawable.confirm_charge,null))
            }
            ChargeStatus.Finishing.name -> {
                binding.chargeButton.text = getString(R.string.finish)
            }
            else -> {
                binding.chargeButton.text = chargeStatus
            }
        }
    }

    private fun checkCharge() {
        EkiRequest<ChargeBody>().also {
            it.body = ChargeBody(EkiApi.CheckCharge).also { body -> body.serNum.addAll(serNum) }
        }.sendRequest(requireContext()
            ,false
            ,object : OnResponseListener<CheckChargeResponse>{
                override fun onFail(errorMsg: String, code: String) {
                }

                override fun onTaskPostExecute(result: CheckChargeResponse) {
                    chargeStatus = result.info?.get(0)?.CpStatus ?: ""

                    setChargeButton(chargeStatus)
                    if (chargeStatus == ChargeStatus.Preparing.name) {
                        startCharge()
                    } else if (chargeStatus == ChargeStatus.Charging.name) {
                        setChargeButton(chargeStatus)
                    }
                }

                override fun onReTry() {
                }
            })
    }

    private fun startCharge() {
        EkiRequest<ChargeBody>().also {
            it.body= ChargeBody(EkiApi.StartCharge).also { body -> body.serNum.addAll(serNum) }
        }.sendRequest(requireContext()
            ,false
            ,object : OnResponseListener<StartChargeResponse> {
                override fun onFail(errorMsg: String, code: String) {
                }

                override fun onTaskPostExecute(result: StartChargeResponse) {
                    if (result.isSuccess) {
                        chargeStatus = ChargeStatus.Charging.name
                        setChargeButton(chargeStatus)
                    } else {
                        chargeStatus = ChargeStatus.SuspendedEV.name
                        setChargeButton(chargeStatus)
                    }
                }

                override fun onReTry() {
                }
            })
    }

    override fun setData(data: ArrayList<EkiOrder>?) {
        data?.forEach {
            onTime = it.isBeSettle() || it.isReserved()
            order = it
            serNum.add(it.SerialNumber)
        }
    }

    enum class ChargeStatus(value:String) {
        Available("Available"),
        Preparing("Preparing"),
        Charging("Charging"),
        SuspendedEVSE("SuspendedEVSE"),
        SuspendedEV("SuspendedEV"),
        Finishing("Finishing"),
        Reserved("Reserved"),
        Unavailable("Unavailable"),
        Faulted("Faulted")
    }

    private fun setDialog(flag:Int) {
        if (flag == leave) {
            EkiMsgDialog().also {
                it.msg = getString(R.string.confirm_to_leave)
                it.determinClick = {
                    toMain.invoke()
                }
                it.cancelClick = {
                    it.onDismissCheck()
                }
            }.show(requireActivity().supportFragmentManager)
        } else if (flag == goCheckOut) {
            EkiMsgDialog().also {
                it.msg = getString(R.string.confirm_go_checkout)
                it.determinClick = {
                    checkOutOrder(order)
                }
                it.cancelClick = {
                    it.onDismissCheck()
                }
            }.show(requireActivity().supportFragmentManager)
        }
    }
}