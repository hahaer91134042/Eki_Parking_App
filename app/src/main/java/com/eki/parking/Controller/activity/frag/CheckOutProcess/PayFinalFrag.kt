package com.eki.parking.Controller.activity.frag.CheckOutProcess

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.OrderDetail.child.RatingFrag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IDialogFeatureSet
import com.eki.parking.Controller.process.RatingProcess
import com.eki.parking.Model.DTO.CheckoutFinal
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.body.RatingBody
import com.eki.parking.R
import com.eki.parking.databinding.FragPayFinalBinding
import com.eki.parking.extension.show
import com.eki.parking.extension.string
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.extension.mod02d
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.time.ext.formateShort
import com.hill.devlibs.util.StringUtil

/**
 * Created by Hill on 2020/05/27
 */
class PayFinalFrag : SearchFrag(), ISetData<CheckoutFinal>, IFragViewBinding {

    private var checkoutFinal: CheckoutFinal? = null
    private lateinit var binding: FragPayFinalBinding

    override fun initFragView() {

        checkoutFinal.notNull { final ->
            binding.amountText.text = getString(R.string.Price_format).messageFormat(final.amt)
            binding.serialText.text = final.serial

            StringUtil.getImgStringBuilder()
                .setIcon(R.drawable.icon_credit_card)
                .setText(" ${final.card4}")
                .into(binding.methodText)

            binding.timeText.text =
                "${final.time.year}/${final.time.month.mod02d()}/${final.time.day.mod02d()} ${final.time.time.formateShort()}"
        }

        binding.toRatingBtn.setOnClickListener {
            RatingFrag.creat().also {
                it.fragTitleSet = RatingFrag.FragTitleSet().apply {
                    title = string(R.string.Evaluate_the_parking_space)
                    titleSet = IDialogFeatureSet.OrangeDialogTitle()
                }
                it.onRatingStartBack = { star ->
                    checkoutFinal.notNull { f ->
                        LocationRating(f.serial, star).run()
                    }
                }
            }.show(childFragmentManager)
        }
    }

    private inner class LocationRating(val serial: String, var star: Double) :
        RatingProcess(context) {
        init {
            onFail = {

            }
            onSuccess = {
                toMainActivity()
            }
        }

        override val body: RatingBody
            get() = RatingBody(EkiApi.RatingLocation).also {
                it.serial = serial
                it.star = star
            }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragPayFinalBinding.inflate(inflater, container, false)
        return binding
    }

    override fun setData(data: CheckoutFinal?) {
        checkoutFinal = data
    }
}