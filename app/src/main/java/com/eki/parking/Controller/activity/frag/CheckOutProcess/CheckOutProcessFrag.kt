package com.eki.parking.Controller.activity.frag.CheckOutProcess

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppConfig
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.DiscountSelectActivity
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.ICameraFileSet
import com.eki.parking.Controller.manager.SysCameraManager
import com.eki.parking.Controller.tools.EkiCalculator
import com.eki.parking.Model.DTO.CheckoutInfo
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.EnumClass.ActionType
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.R
import com.eki.parking.databinding.FragCheckoutProcessBinding
import com.eki.parking.extension.dpToPx
import com.eki.parking.extension.screenWidth
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toBitMap
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.format
import com.hill.devlibs.tools.Log
import java.io.File

/**
 * Created by Hill on 19,11,2019
 */
class CheckOutProcessFrag : SearchFrag(),
    ISetData<EkiOrder>,
    SysCameraManager.CameraResultListener, IFragViewBinding {

    var isBackToMain = true

    private var pictureFile: File? = null
    private lateinit var order: EkiOrder
    private lateinit var sysCameraManager: SysCameraManager
    private var checkOutTime = DateTime()
    private lateinit var result: EkiCalculator.OrderCheckOutResult

    private var selectDiscount: ResponseInfo.Coupon? = null
    private var selectAction: ResponseInfo.Action? = null

    private var discountAmt = 0.0

    private lateinit var binding: FragCheckoutProcessBinding

    var onCheckoutBack: ((ProcessResult1) -> Unit)? = null

    class ProcessResult1 {
        var pic: File? = null
        lateinit var info: CheckoutInfo
        lateinit var calResult: EkiCalculator.OrderCheckOutResult
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragCheckoutProcessBinding.inflate(inflater, container, false)
        return binding
    }

    override fun setData(data: EkiOrder?) {
        data.notNull { order = it }
    }

    override fun initFragView() {
        toolBarTitle = "結清訂單"
        registerReceiver(AppFlag.DiscountSelect, AppFlag.ActionSelect)

        sysCameraManager = app.sysCamera
        sysCameraManager.onResume(activity)
        sysCameraManager.addCameraListenerFrom(this)
        setUpOrderInfo()

        binding.carNumCamera.onStartCamera = {
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

        binding.determinBtn.setOnClickListener {
            goCheckOutTask(pictureFile)
        }

    }

    private fun goCheckOutTask(pic: File?) {

        onCheckoutBack?.invoke(ProcessResult1().also {
            it.pic = pic
            it.info = CheckoutInfo(
                order.SerialNumber,
                checkOutTime.format(AppConfig.ServerSet.dateTimeFormat)
            ).also { info ->
                val gps = gps
                info.lat = gps.latitude
                info.lng = gps.longitude
                selectDiscount.notNull { dis ->
                    info.discount = dis.Code
                }
                selectAction.notNull { act ->
                    info.action = act.Serial
                }
            }
            it.calResult = result
        })
    }

    private fun setUpOrderInfo() {
        result = EkiCalculator.calCheckout(order, checkOutTime)

        setUpPriceRow()

        binding.discountRow.selectBtn.setOnClickListener {
            startActivitySwitchAnim(Intent().apply {
                putExtra(AppFlag.IsBackToMain, isBackToMain)
                setClass(requireContext(), DiscountSelectActivity::class.java)
            })
        }
        binding.discountRow.deleteBtn.setOnClickListener {
            binding.discountRow.cancelDiscount()
            onDiscountCancel()
        }
    }

    private fun setUpPriceRow() {
        Log.i("formatCheckOutResult->${result.standarCheckOut}")

        Log.w("startTime->${result.startTime} ")
        Log.d("damageDuringHour->${result.damageDuringHour} damageCost->${result.damageCost}  totalCost->${result.finalCost}")

        binding.orderInfoTextView.text = getString(R.string.CheckOut_order_info).messageFormat(
            result.startTime.format(AppConfig.ServerSet.dateTimeFormat2),
            result.standarCheckOut.format(AppConfig.ServerSet.dateTimeFormat2),
            result.totalDuringHour
        )

        binding.priceTextView.text = getString(R.string.Price_format).messageFormat(result.finalCost)
//        Log.d("$TAG row tieleText->${amountRow.title} infoText->${amountRow.info}")
        binding.amountRow.info.text = getString(R.string.Price_format).messageFormat(result.normalCost)
        binding.otherFeeRow.info.text = getString(R.string.Price_format).messageFormat(0)
        binding.defaultFeeRow.info.text = getString(R.string.Price_format).messageFormat(result.damageCost)


        binding.totalAmountRow.info.text = getString(R.string.Price_format).messageFormat(result.finalCost)
    }

    override fun onCatchReceive(action: String?, intent: Intent?) {
//        Log.w("Get Receive->$action  data->${intent?.getSerializableExtra(AppFlag.DATA_FLAG)}")
        when (action) {
            AppFlag.DiscountSelect -> {
                val dis = intent?.getSerializableExtra(AppFlag.DATA_FLAG) as ResponseInfo.Coupon
                addDiscountToAmt(dis)
            }
            AppFlag.ActionSelect -> {
                val actionScan =
                    intent?.getSerializableExtra(AppFlag.DATA_FLAG) as ResponseInfo.Action

                addAction(actionScan)
            }
        }
    }

    private fun onDiscountCancel() {
        result.discountAmt = -discountAmt
        discountAmt = 0.0
        selectDiscount = null
        setUpPriceRow()
    }

    private fun addAction(action: ResponseInfo.Action) {
        selectAction = action
        discountAmt = when (action.type) {
            ActionType.Discount -> {
                var d = 0.0
                action.discount.notNull { dis ->
                    d = if (dis.Number == 0.0) {
                        result.normalCost * (1 - dis.Ratio)
                    } else {
                        when (result.normalCost < dis.Number) {
                            true -> result.normalCost
                            else -> dis.Number
                        }
                    }
                }
                d
            }
        }

        binding.discountRow.onSelectDiscount(discountAmt)
        result.discountAmt = discountAmt
        setUpPriceRow()
    }

    private fun addDiscountToAmt(discount: ResponseInfo.Coupon) {
        selectDiscount = discount
        when {
            result.normalCost > discount.Amt -> {
                //因為目前折價只能扣抵正常的停車費用 不能扣抵違規的費用
                discountAmt = when (discount.IsRange) {
                    true -> {
                        var a = 0.0
                        if (discount.End.toDateTime().date >= DateTime().date)
                            a = discount.Amt
                        a
                    }
                    else -> discount.Amt
                }
            }
            else -> {
                discountAmt = result.normalCost
            }
        }

        binding.discountRow.onSelectDiscount(discountAmt)
        result.discountAmt = discountAmt
        setUpPriceRow()
    }

    override fun onDestroyView() {
        sysCameraManager.removeCameraListener(this)
        super.onDestroyView()
    }

    override fun onPicture(pic: File) {
        pictureFile = pic
        Log.w("--getPicture-- path->${pic.path}")
        binding.carNumCamera.showPicture(pic.toBitMap())
    }

    override fun onPictureError() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }
}