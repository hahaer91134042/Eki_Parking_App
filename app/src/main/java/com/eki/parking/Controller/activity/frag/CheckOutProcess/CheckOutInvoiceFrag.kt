package com.eki.parking.Controller.activity.frag.CheckOutProcess

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.OrderInvoiceType
import com.eki.parking.R
import com.eki.parking.databinding.FragCheckoutInvoiceBinding
import com.eki.parking.extension.toEnum
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/08/24
 */

class CheckOutInvoiceFrag : SearchFrag(), IFragViewBinding {

    var onStartCheckout: ((RequestBody.OrderInvoice) -> Unit)? = null
    private lateinit var binding: FragCheckoutInvoiceBinding

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragCheckoutInvoiceBinding.inflate(inflater,container,false)
        return binding
    }

    override fun initFragView() {
        toolBarTitle = getString(R.string.bill_setting)

        binding.choiceInvoiceView.start()

        binding.determinBtn.setOnClickListener {

            binding.choiceInvoiceView.inputInvoice().notNull { invoice ->

                invoice.name = binding.invoiceTitle.text.toString().cleanTex
                invoice.address = binding.addressText.text.toString().cleanTex
                invoice.mail = binding.emailText.text.toString().cleanTex

                when (invoice.type.toEnum<OrderInvoiceType>()) {
                    OrderInvoiceType.Paper -> {
                        if (invoice.name.isEmpty() || invoice.address.isEmpty() || invoice.mail.isEmpty()) {
                            showToast(getString(R.string.basic_info_error_msg))
                        } else {
                            onStartCheckout?.invoke(invoice)
                        }
                    }
                    else -> {
                        onStartCheckout?.invoke(invoice)
                    }
                }

            }
        }
    }
}