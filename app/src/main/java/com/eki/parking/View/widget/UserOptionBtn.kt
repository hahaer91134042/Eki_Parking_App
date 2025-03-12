package com.eki.parking.View.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eki.parking.Model.EnumClass.IdentityMode
import com.eki.parking.R
import com.eki.parking.Controller.listener.OptionListener
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.View.abs.ConstrainCustomView

/**
 * Created by Hill on 2019/5/3
 */
class UserOptionBtn(context: Context?, attrs: AttributeSet?) : ConstrainCustomView(context, attrs),
        View.OnClickListener {


    var member: EkiMember?=null
    set(value) {
        if (value?.beManager == false){
            optionStr=getString(R.string.Become_a_landlord)
            mode=IdentityMode.BeManager
        }else{
            optionStr=getString(R.string.Switch_Identity)
            mode=IdentityMode.User
        }
        field = value
    }

    //    val optionText:TextView by lazy {
    //        itemView.findViewById(R.id.mainText) as TextView
    //    }
    private var optionText:TextView = itemView.findViewById(R.id.mainText)

    var listener: OptionListener<IdentityMode>?=null

    var optionStr:String?= null
    set(value) {
        field=value
        optionText?.text=value
    }

    private var mode=IdentityMode.User

    init {
//        itemView.layoutParams= LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)

//        var toNextBtn=itemView.findViewById<StateButton>(R.id.toNextBtn)
//        toNextBtn.background=context?.getDrawable(R.drawable.icon_arrow_right_light_gray)
//          var parentView=itemView.findViewById<View>(R.id.parentView)
//        parentView.background=context?.getDrawable(R.this.selector_user_option_btn)

        optionText.text=optionStr

        setOnClickListener(this)
    }

    override fun onClick(v: View?){
        mode=when(mode){
            IdentityMode.BeManager->IdentityMode.BeManager
            IdentityMode.User->IdentityMode.LandOwner
            else -> IdentityMode.User
        }
        listener?.onOptionClick(mode)
    }

    override fun setStyleableRes(): IntArray? {
        return R.styleable.UserOptionBtn
    }

    override fun parseTypedArray(typedArray: TypedArray) {
        optionStr=typedArray.getString(R.styleable.UserOptionBtn_option_str)
    }

    override fun setInflatView():Int= R.layout.item_user_option_btn
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? {
        return null
    }
}