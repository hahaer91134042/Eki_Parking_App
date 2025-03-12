package com.eki.parking.View.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.eki.parking.R
import com.eki.parking.extension.dimen
import com.eki.parking.View.abs.RelativeCustomView
import com.eki.parking.extension.dpToPx
import com.hill.devlibs.widget.AutoSizeTextView
//import kotlinx.android.synthetic.main.item_info_list_row.view.*


/**
 * Created by Hill on 2019/11/25
 */
class InfoListRow(context: Context?,attrs: AttributeSet?) : RelativeCustomView(context, attrs) {

//    private var titleStr=""
//    private var titleTextColor=Color.BLACK

    lateinit var title:TextView
    lateinit var info:TextView


//    override fun initInFlaterView() {
//        titleText.text=titleStr
//        titleText.setTextColor(titleTextColor)
//    }

    var titleStr=""
    var titleTextColor=0

    init {
        gravity=Gravity.CENTER_VERTICAL
    }

    override fun parseTypedArray(typedArray: TypedArray) {
        var titleStrRes=typedArray.getResourceId(R.styleable.InfoListRow_lr_title_str,0)
        titleStr=when{
            titleStrRes>0->getString(titleStrRes)
            else->typedArray.getString(R.styleable.InfoListRow_lr_title_str)?:""
        }

        var colorRes=typedArray.getResourceId(R.styleable.InfoListRow_lr_title_textColor,0)
        titleTextColor=when{
            colorRes>0->getColor(colorRes)
            else->typedArray.getColor(R.styleable.InfoListRow_lr_title_textColor,Color.BLACK)
        }
//        Log.w("InfoListRow titleStrRes->$titleStrRes  title->${titleStr}")

//        typedArray.recycle()

//        title=itemView.findViewById(R.id.titleText)
//        title.text=titleStr
//        title.setTextColor(titleTextColor)
        title=AutoSizeTextView(context).also {
            it.id=R.id.titleText
            it.layoutParams=LayoutParams(dpToPx(100f), dpToPx(25f)).apply {
                addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            }
//            it.textSize= dimen(R.dimen.text_size_7)
            it.text=titleStr
            it.setTextColor(titleTextColor)
            addView(it)
        }


//        info=infoText
//        infoText.setTextColor(getColor(R.color.text_color_1))
        info=AutoSizeTextView(context).also {
            it.layoutParams=LayoutParams(LayoutParams.WRAP_CONTENT, dpToPx(25f)).apply {
                addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                addRule(END_OF,R.id.titleText)
            }
            it.gravity=Gravity.RIGHT
//            it.textSize= dimen(R.dimen.text_size_7)
            it.setTextColor(getColor(R.color.text_color_1))
//            it.text="111222333"
            addView(it)
        }
    }


    override fun setStyleableRes(): IntArray? = R.styleable.InfoListRow
    override fun setInflatView(): Int =0
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}