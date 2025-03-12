package com.eki.parking.Controller.dialog

import android.content.Context

import android.view.View
import android.widget.EditText
import com.eki.parking.R
import com.eki.parking.Controller.dialog.abs.BaseAlertDialog
import com.eki.parking.View.widget.DialogCancelBtnView
import com.eki.parking.View.libs.StateButton
import com.hill.devlibs.EnumClass.DialogOption

/**
 * Created by Hill on 2018/11/14.
 */
class EditAddressDialog(context: Context) : BaseAlertDialog(context),View.OnClickListener{

//    var address:Car1Address= Car1Address()
//    set(ratio) {
//        field=ratio
//        zipCodeEditText.setText(ratio.ZipCode)
//        stateEditText.setText(ratio.State)
//        cityEditText.setText(ratio.City)
//        addressEditText.setText(ratio.Address)
//    }

    private lateinit var parentView:DialogCancelBtnView
    private lateinit var zipCodeEditText:EditText
    private lateinit var stateEditText:EditText
    private lateinit var cityEditText:EditText
    private lateinit var addressEditText:EditText
    private lateinit var determinBtn:StateButton
//    var listener:EditAddressListener?=null

//    interface EditAddressListener{
//        fun onEditAddress(address: Car1Address)
//    }

    override fun setUpDialogFeature() = DialogSetting(
            DialogOption.NoTitle,
            DialogOption.Colorless
    )

    override fun initView() {
        parentView=itemView.findViewById(R.id.parentView)
        determinBtn=itemView.findViewById(R.id.determinBtn)
        parentView.cancelBtn.setOnClickListener(this)
        determinBtn.setOnClickListener(this)

        zipCodeEditText=itemView.findViewById(R.id.zipCodeEditText)
        stateEditText=itemView.findViewById(R.id.stateEditText)
        cityEditText=itemView.findViewById(R.id.cityEditText)
        addressEditText=itemView.findViewById(R.id.addressEditText)


//        setOnDismissListener {
//            PrintLogKt.d("$TAG->Dismiss")
//        }
    }

//    fun setUserText(name:){
//        nameEditText.setText(name)
//        emailEditText.setText(email)
//        phoneEditText.setText(phone)
//        PrintLogKt.d("$TAG name->$name email->$email phone->$phone")
//    }
    override fun onClick(v: View?) {
//        address.ZipCode= StringUtil.cleanChar(zipCodeEditText.text.toString())
//        address.Country="Taiwan"
//        address.State=StringUtil.cleanChar(stateEditText.text.toString())
//        address.City=StringUtil.cleanChar(cityEditText.text.toString())
//        address.Address=StringUtil.cleanChar(addressEditText.text.toString())
//
//        when(v?.id){
//            R.id.determinBtn->listener?.onEditAddress(address)
//        }
        dismiss()
    }

    override fun setInflatView()= R.layout.item_edit_address
}