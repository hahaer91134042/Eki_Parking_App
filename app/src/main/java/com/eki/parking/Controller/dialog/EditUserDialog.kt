package com.eki.parking.Controller.dialog

import android.content.Context

import android.view.View
import android.widget.EditText
import com.eki.parking.R
import com.eki.parking.Controller.dialog.abs.BaseAlertDialog
import com.eki.parking.View.widget.DialogCancelBtnView
import com.eki.parking.View.libs.StateButton
import com.hill.devlibs.EnumClass.DialogOption
import com.hill.devlibs.util.StringUtil

/**
 * Created by Hill on 2018/11/14.
 */
class EditUserDialog(context: Context) : BaseAlertDialog(context),View.OnClickListener{

    var name:String=""
    set(value) {nameEditText.setText(value)}

    var email:String=""
    set(value) {emailEditText.setText(value)}

    var phone:String=""
    set(value) {phoneEditText.setText(value)}

    private lateinit var parentView:DialogCancelBtnView
    private lateinit var nameEditText:EditText
    private lateinit var emailEditText:EditText
    private lateinit var phoneEditText:EditText
    private lateinit var determinBtn:StateButton
    var listener:EditUserListener?=null

    interface EditUserListener{
        fun onEditUser(name:String,email:String,phone:String)
    }

    override fun setUpDialogFeature() = DialogSetting(
            DialogOption.NoTitle,
            DialogOption.Colorless
    )

    override fun initView() {
        parentView=itemView.findViewById(R.id.parentView)
        determinBtn=itemView.findViewById(R.id.determinBtn)
        parentView.cancelBtn.setOnClickListener(this)
        determinBtn.setOnClickListener(this)

        nameEditText=itemView.findViewById(R.id.nameEditText)
        emailEditText=itemView.findViewById(R.id.emailEditText)
        phoneEditText=itemView.findViewById(R.id.phoneNumEditText)


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
        when(v?.id){
            R.id.determinBtn->listener?.onEditUser(
                    StringUtil.cleanChar(nameEditText.text.toString()),
                    StringUtil.cleanChar(emailEditText.text.toString()),
                    StringUtil.cleanChar(phoneEditText.text.toString())
            )
        }
        dismiss()
    }

    override fun setInflatView()= R.layout.item_edit_user
}