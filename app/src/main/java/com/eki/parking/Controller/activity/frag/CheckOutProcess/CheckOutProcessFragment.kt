package com.eki.parking.Controller.activity.frag.CheckOutProcess

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppConfig
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.DiscountSelectActivity
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.ICameraFileSet
import com.eki.parking.Controller.manager.SysCameraManager
import com.eki.parking.Controller.tools.EkiCalculator
import com.eki.parking.Model.DTO.CheckoutInfo
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.EnumClass.WeekDay
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.databinding.DialogOrangeMsgBinding
import com.eki.parking.databinding.FragmentCheckoutProcessBinding
import com.eki.parking.extension.*
import com.eki.parking.utils.Logger
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.extension.multiply
import com.hill.devlibs.extension.toBitMap
import com.hill.devlibs.extension.toJsonStr
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.format
import com.hill.devlibs.time.ext.formateShort
import com.hill.devlibs.time.ext.hourSpan
import java.io.File

class CheckOutProcessFragment : SearchFrag(),
    ISetData<EkiOrder>,
    SysCameraManager.CameraResultListener, IFragViewBinding {

    private lateinit var binding: FragmentCheckoutProcessBinding
    private lateinit var sysCameraManager: SysCameraManager
    private lateinit var order: EkiOrder
    private lateinit var result: EkiCalculator.OrderCheckOutResult

    private var checkOutTime = DateTime()
    private var pictureFile: File? = null

    var checkoutData = ProcessResult()
    var onReceiveBack: (() -> Unit)? = null
    var onCheckoutBack: ((Int,ProcessResult) -> Unit)? = null
    var onInitData: ((ProcessResult) -> Unit)? = null
    private var selectDiscount: ResponseInfo.Coupon? = null
    private var selectAction: ResponseInfo.Action? = null

    private val creditCard = 0
    private val linePay = 1
    private var paymentMethod = creditCard

    inner class ProcessResult {
        var pic: File? = null
        lateinit var info: CheckoutInfo
        lateinit var calResult: EkiCalculator.OrderCheckOutResult
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragmentCheckoutProcessBinding.inflate(inflater, container, false)
        setUI(order)
        return binding
    }

    override fun setData(data: EkiOrder?) {
        data?.let {
            order = it
            if (this::binding.isInitialized) {
                setUI(it)
            }
        }
    }

    override fun initFragView() {
        toolBarTitle = getString(R.string.car_owner_checkout)
        registerReceiver(AppFlag.DiscountSelect, AppFlag.ActionSelect)
        sysCameraManager = app.sysCamera
        sysCameraManager.onResume(activity)
        sysCameraManager.addCameraListenerFrom(this)

        result = EkiCalculator.calCheckout(order, checkOutTime)

        binding.checkoutCameraImage.setOnClickListener {
            sysCameraManager.startCamera(object : ICameraFileSet() {
                override val saveDir: File
                    get() = SysCameraManager.appCameraDir
                override val fileName: String
                    get() = DateTime().format("yyyyMMddHHmmss")
                override val scaleX: Int
                    get() = screenWidth()
                override val scaleY: Int
                    get() = dpToPx(300f)
                override val addDateLabel: Boolean
                    get() = true
                override val labelTime: DateTime
                    get() = checkOutTime
            })
        }

        checkoutData.also {
            it.pic = null
            it.info = CheckoutInfo(
                order.SerialNumber,
                checkOutTime.format(AppConfig.ServerSet.dateTimeFormat)
            ).also { info ->
                val gps = gps
                info.lat = gps.latitude
                info.lng = gps.longitude
                selectDiscount?.let { dis ->
                    info.discount = dis.Code
                }
                selectAction?.let { act ->
                    info.action = act.Serial
                }
            }
            it.calResult = result
        }
        onInitData?.invoke(checkoutData)

        binding.paymentMethodLayout.setOnClickListener {
            setPaymentDialog()
        }

        binding.checkoutDiscountLayout.setOnClickListener {
            startActivitySwitchAnim(Intent().apply {
                putExtra(AppFlag.IsBackToMain, true)
                setClass(requireContext(), DiscountSelectActivity::class.java)
            })
        }

        binding.checkoutReceiptLayout.setOnClickListener {
            onReceiveBack?.invoke()
        }

        binding.checkoutButton.setOnClickListener {
            goCheckOutTask(paymentMethod,pictureFile)
        }
    }

    override fun onCatchReceive(action: String?, intent: Intent?) {
        when (action) {
            AppFlag.DiscountSelect -> {
                val dis = intent?.getSerializableExtra(AppFlag.DATA_FLAG) as ResponseInfo.Coupon
            }
            AppFlag.ActionSelect -> {
                val actionScan =
                    intent?.getSerializableExtra(AppFlag.DATA_FLAG) as ResponseInfo.Action
            }
        }
    }

    override fun onDestroyView() {
        sysCameraManager.removeCameraListener(this)
        super.onDestroyView()
    }

    override fun onPicture(pic: File) {
        pictureFile = pic
        checkoutData.pic = pic
        binding.checkoutCameraImage.setImageBitmap(pic.toBitMap())
    }

    override fun onPictureError() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setUI(order: EkiOrder) {
        val start = order.startDateTime()
        val end = order.endDateTime()
        binding.checkoutStartDateText.text =
            getString(R.string.date_time_week,
                start.month.mod02d(),
                start.day.mod02d(),
                getString(start.weekDay.toEnum<WeekDay>().strRes))

        binding.checkoutEndDateText.text =
            getString(R.string.date_time_week,
                end.month.mod02d(),
                end.day.mod02d(),
                getString(end.weekDay.toEnum<WeekDay>().strRes))

        binding.checkoutStartTimeText.text = start.time.formateShort()
        binding.checkoutEndTimeText.text = end.time.formateShort()

        binding.checkoutLocationPriceText.text = getString(R.string.price,order.Cost.toInt())

        val startTime = order.ReservaTime.startDateTime()
        val endTime = order.ReservaTime.endDateTime()
        val standardCheckOut = DateTime().standarToCheckOut(order.ReservaTime)
        val totalDuringHour = (standardCheckOut - startTime) / 1.hourSpan
        Logger.v("total time = ${(standardCheckOut - startTime).toJsonStr()}")
        binding.checkoutPriceHourText.text = //getString(R.string.hour_brackets,totalDuringHour)
            if ((standardCheckOut - startTime).hour == 0) {
                when ((standardCheckOut - startTime).min) {
                    15 -> {
                        getString(R.string.hour_brackets, 0.25)
                    }
                    30 -> {
                        getString(R.string.hour_brackets, 0.5)
                    }
                    45 -> {
                        getString(R.string.hour_brackets, 0.75)
                    }
                    else -> {
                        getString(R.string.hour_brackets, totalDuringHour)
                    }
                }
            } else {
                getString(R.string.hour_brackets, totalDuringHour)
            }

        val normalSpan = if (standardCheckOut <= endTime) {
            standardCheckOut - startTime
        } else {
            endTime - startTime
        }
        val hourPrice = order.LocPrice.multiply(2.0)
        val normalDuringHour= (normalSpan / 1.hourSpan)
        val normalCost=(hourPrice.multiply(normalDuringHour)).toCurrency(order.Unit)

        binding.checkoutPriceText2.text =
            getString(R.string.price_half_hour_parking,order.LocPrice.toInt(),normalCost.toInt())
        binding.checkoutOtherPriceText.text = getString(R.string.price,order.HandlingFee.toInt())

        val damageDuringHour = if (standardCheckOut >= endTime) {
            (standardCheckOut - endTime) / 1.hourSpan
        } else {
            0.0
        }
        val damageCost =
            ((hourPrice.multiply(damageDuringHour))
                .multiply(AppConfig.ServerSet.orderClaimantRate)).toCurrency(order.Unit)
        binding.checkoutDefaultPriceText.text = getString(R.string.price,damageCost.toInt())

        val discountAmt = 0.0
        val finalCost = (normalCost + damageCost - discountAmt).toCurrency(order.Unit)
        binding.checkoutTotalPriceText.text = getString(R.string.price,finalCost.toInt())

        //顯示發票、支付方式預設值:會員載具、信用卡
        binding.receiptMethodText.text = getString(R.string.member_barcode)
        binding.paymentMethodText.text = getString(R.string.credit_card)
    }

    fun setUiData(invoice: RequestBody.OrderInvoice) {
        //type:
        //1: 捐贈碼
        //2: 統編
        //3: 手機載具
        //4: 自然人
        //5: 會員載具(預設)
        binding.receiptMethodText.text =
            when (invoice.type) {
                1 -> { getString(R.string.donate_receive) }
                2 -> { getString(R.string.uniform_number)}
                3 -> { getString(R.string.mobile_barcode)}
                4 -> { getString(R.string.natural_barcode)}
                5 -> { getString(R.string.member_barcode)}
                else -> { "error" }
            }
    }

    private fun goCheckOutTask(flag:Int,pic: File?) {
        onCheckoutBack?.invoke(flag,checkoutData.also {
            it.pic = pic
            it.info = CheckoutInfo(
                order.SerialNumber,
                checkOutTime.format(AppConfig.ServerSet.dateTimeFormat)
            ).also { info ->
                val gps = gps
                info.lat = gps.latitude
                info.lng = gps.longitude
                selectDiscount?.let { dis ->
                    info.discount = dis.Code
                }
                selectAction?.let { act ->
                    info.action = act.Serial
                }
            }
            it.calResult = result
        })
    }

    private fun setPaymentDialog() {
        val dialog = Dialog(requireContext())
        dialog.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val binding = DialogOrangeMsgBinding.inflate(LayoutInflater.from(requireContext()))
            binding.dialogTitleText.text = getString(R.string.payment_method)

            binding.dialogText1.text = getString(R.string.credit_card)
            binding.dialogImage2.setImageDrawable(
                ResourcesCompat.getDrawable(resources,R.drawable.line_pay,null))

            binding.dialogCancelButton.setOnClickListener{
                dialog.dismiss()
            }
            binding.dialogText1.setOnClickListener {
                paymentMethod = creditCard
            }
            binding.dialogImage2.setOnClickListener {
                paymentMethod = linePay
            }

            it.setCanceledOnTouchOutside(true)
            it.setContentView(binding.root)
            it.create()
        }
        dialog.show()
    }
}