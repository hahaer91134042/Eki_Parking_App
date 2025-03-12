package com.eki.parking.View.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.eki.parking.AppConfig
import com.eki.parking.Controller.tools.AnimationToast
import com.eki.parking.Controller.tools.CheckRule
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.OrderInvoiceType
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView
import com.github.florent37.expansionpanel.ExpansionLayout
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.showToast

/**
 * Created by Hill on 2020/09/07
 */
class ChoiceInvoiceMethodView(context: Context?, attrs: AttributeSet?) :
    LinearCustomView(context, attrs) {

    private var expansionCollection = ExpansionLayoutCollection().apply { openOnlyOne(true) }

    private var choiceMethod = ChoiceMethod.MemberVector

    private val inputDonateCode: EditText = findViewById(R.id.inputDonateCode)
    private val inputSerNum: EditText = findViewById(R.id.inputSerNum)
    private val phoneVector: EditText = findViewById(R.id.phoneVector)
    private val natureVector: EditText = findViewById(R.id.natureVector)
    private val donateContent: ExpansionLayout = findViewById(R.id.donateContent)
    private val serNumContent: ExpansionLayout = findViewById(R.id.serNumContent)
    private val vectorContent: ExpansionLayout = findViewById(R.id.vectorContent)
    private val memberVectorView: RelativeLayout = findViewById(R.id.memberVectorView)
    private val ll_member: LinearLayout = findViewById(R.id.ll_member)
    private val defaultDonateView: RelativeLayout = findViewById(R.id.defaultDonateView)
    private val defaultDonateCheck: ImageView = findViewById(R.id.defaultDonateCheck)
    private val customDonateCheck: ImageView = findViewById(R.id.customDonateCheck)
    private val phoneCheck: ImageView = findViewById(R.id.phoneCheck)
    private val natureCheck: ImageView = findViewById(R.id.natureCheck)
    private val memberCheck: ImageView = findViewById(R.id.memberCheck)

    private enum class ChoiceMethod {
        DefaultDonate,
        CustomDonate,
        SerNum,
        PhoneVector,
        NatureVector,
        MemberVector
    }

    fun inputInvoice(): RequestBody.OrderInvoice? {
        if (checkInput()) {
            val invoice = RequestBody.OrderInvoice()

            when (choiceMethod) {
                ChoiceMethod.DefaultDonate -> {
                    invoice.type = OrderInvoiceType.Donate.value
                    invoice.loveCode = AppConfig.Invoice.defautLoveCode
                }
                ChoiceMethod.CustomDonate -> {
                    invoice.type = OrderInvoiceType.Donate.value
                    invoice.loveCode = inputDonateCode.text.toString().cleanTex
                }
                ChoiceMethod.SerNum -> {
                    invoice.type = OrderInvoiceType.Paper.value
                    invoice.ubn = inputSerNum.text.toString().cleanTex

                }
                ChoiceMethod.PhoneVector -> {
                    invoice.type = OrderInvoiceType.Phone.value
                    invoice.carrierNum = phoneVector.text.toString().cleanTex

                }
                ChoiceMethod.NatureVector -> {
                    invoice.type = OrderInvoiceType.Nature.value
                    invoice.carrierNum = natureVector.text.toString().cleanTex
                }
                ChoiceMethod.MemberVector -> {
                    invoice.type = OrderInvoiceType.EzPay.value
                }
            }

            return invoice
        }
        return null
    }

    private fun checkInput(): Boolean =
        when (choiceMethod) {
            ChoiceMethod.PhoneVector -> {
                var v = true
                val phoneVectorNum = phoneVector.text.toString().cleanTex
                if (!CheckRule.phoneVectorRule.isInRule(phoneVectorNum)) {
                    v = false
                    context.showToast("手機載具開頭請輸入\"/\"+7碼數字或大寫字母")
                }
                v
            }
            ChoiceMethod.NatureVector -> {
                var v = true
                val natureVectorNum = natureVector.text.toString().cleanTex
                if (!CheckRule.natureVectorRule.isInRule(natureVectorNum)) {
                    v = false
                    context.showToast("自然人載具格式錯誤")
                }
                v
            }

            else -> true
        }

    fun start() {
        expansionCollection.add(donateContent)
        expansionCollection.add(serNumContent)
        expansionCollection.add(vectorContent)

        setUpDonateInput()
        setUpSerNumInput()
        setUpVectorInput()

        setCheckIcon(choiceMethod)
    }

    private fun setUpVectorInput() {

        phoneVector.addTextChangedListener(object : SimpleTextWatch(ChoiceMethod.PhoneVector) {
        })

        natureVector.addTextChangedListener(object : SimpleTextWatch(ChoiceMethod.NatureVector) {
        })

        memberVectorView.setOnClickListener {
            choiceMethod = ChoiceMethod.MemberVector
            setCheckIcon(choiceMethod)
        }

        ll_member.setOnClickListener {
            val animationToast = AnimationToast(context)
            animationToast.initToast(false)
            animationToast.setToastShow()
        }

    }

    private fun setUpSerNumInput() {
        inputSerNum.addTextChangedListener(object : SimpleTextWatch(ChoiceMethod.SerNum) {})
    }

    private fun setUpDonateInput() {
        defaultDonateView.setOnClickListener {
            choiceMethod = ChoiceMethod.DefaultDonate
            setCheckIcon(choiceMethod)
        }
        inputDonateCode.addTextChangedListener(object :
            SimpleTextWatch(ChoiceMethod.CustomDonate) {})
    }

    private fun setCheckIcon(method: ChoiceMethod) {
        when (method) {
            ChoiceMethod.DefaultDonate -> {
                defaultDonateCheck.setImageResource(R.drawable.icon_choice_orange)
                customDonateCheck.setImageDrawable(null)
                inputDonateCode.setText("")
                inputSerNum.setText("")
                phoneCheck.setImageDrawable(null)
                phoneVector.setText("")
                natureCheck.setImageDrawable(null)
                natureVector.setText("")
                memberCheck.setImageDrawable(null)
            }
            ChoiceMethod.CustomDonate -> {
                defaultDonateCheck.setImageDrawable(null)
                customDonateCheck.setImageResource(R.drawable.icon_choice_orange)
                inputSerNum.setText("")

                phoneCheck.setImageDrawable(null)
                phoneVector.setText("")

                natureCheck.setImageDrawable(null)
                natureVector.setText("")
                memberCheck.setImageDrawable(null)
            }
            ChoiceMethod.SerNum -> {

                defaultDonateCheck.setImageDrawable(null)
                customDonateCheck.setImageDrawable(null)

                inputDonateCode.setText("")

                phoneCheck.setImageDrawable(null)
                phoneVector.setText("")
                natureCheck.setImageDrawable(null)
                natureVector.setText("")
                memberCheck.setImageDrawable(null)
            }
            ChoiceMethod.PhoneVector -> {
                defaultDonateCheck.setImageDrawable(null)
                customDonateCheck.setImageDrawable(null)
                inputDonateCode.setText("")

                phoneCheck.setImageResource(R.drawable.icon_choice_orange)
                inputSerNum.setText("")
                natureCheck.setImageDrawable(null)
                natureVector.setText("")
                memberCheck.setImageDrawable(null)
            }
            ChoiceMethod.NatureVector -> {

                defaultDonateCheck.setImageDrawable(null)
                customDonateCheck.setImageDrawable(null)
                inputDonateCode.setText("")
                inputSerNum.setText("")
                phoneCheck.setImageDrawable(null)
                phoneVector.setText("")
                natureCheck.setImageResource(R.drawable.icon_choice_orange)
                memberCheck.setImageDrawable(null)

            }
            ChoiceMethod.MemberVector -> {

                defaultDonateCheck.setImageDrawable(null)
                customDonateCheck.setImageDrawable(null)
                inputDonateCode.setText("")
                inputSerNum.setText("")
                phoneCheck.setImageDrawable(null)
                phoneVector.setText("")
                natureCheck.setImageDrawable(null)
                natureVector.setText("")
                memberCheck.setImageResource(R.drawable.icon_choice_orange)

            }
        }
    }

    private abstract inner class SimpleTextWatch(private var method: ChoiceMethod) : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.toString().notNull { input ->
                if (input.isNotEmpty()) {
                    choiceMethod = method
                    setCheckIcon(choiceMethod)
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    override fun setInflatView(): Int = R.layout.item_choice_invoice_method
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null
}