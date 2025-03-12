package com.eki.parking.View.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.R
import com.eki.parking.View.abs.RelativeCustomView
import com.eki.parking.extension.*
import com.hill.devlibs.extension.messageFormat
import com.hill.devlibs.tools.Log
import com.hill.devlibs.widget.AutoSizeTextView

/**
 * Created by Hill on 2019/11/25
 */
class DiscountSelectRow(context: Context?, attrs: AttributeSet?) : RelativeCustomView(context, attrs) {

//    private var titleStr=""
//    private var titleTextColor=Color.BLACK

    lateinit var title:TextView
    private lateinit var contentFrame:RelativeLayout

    lateinit var selectBtn:TextView

    lateinit var deleteBtn:ImageButton

    private lateinit var discountFrame:LinearLayout
    private lateinit var amtText:TextView

//    var onDiscountCancel:(()->Unit)?=null


    init {
        gravity=Gravity.CENTER_VERTICAL

    }

    fun onSelectDiscount(discount:Double){
        contentFrame.removeAllViews()
        amtText.text=getString(R.string.Minus_Price_format).messageFormat(discount)
        contentFrame.addView(discountFrame)
    }

    fun cancelDiscount(){
        Log.d("delete click")
        contentFrame.removeAllViews()
        contentFrame.addView(selectBtn)

    }

    override fun parseTypedArray(typedArray: TypedArray) {
        var titleStrRes=typedArray.getResourceId(R.styleable.DiscountSelectRow_dr_title_str,0)
        var titleStr=when{
            titleStrRes>0->getString(titleStrRes)
            else->typedArray.getString(R.styleable.DiscountSelectRow_dr_title_str)?:""
        }

        var colorRes=typedArray.getResourceId(R.styleable.DiscountSelectRow_dr_title_textColor,0)
        var titleTextColor=when{
            colorRes>0->getColor(colorRes)
            else->typedArray.getColor(R.styleable.DiscountSelectRow_dr_title_textColor,Color.BLACK)
        }
//        Log.w("InfoListRow titleStrRes->$titleStrRes  title->${titleStr}")

//        typedArray.recycle()

        title=AutoSizeTextView(context).also {
            it.id=R.id.titleText
            it.layoutParams=LayoutParams(dpToPx(120f), dpToPx(50f)).apply {
                addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            }
//            it.textSize= dimen(R.dimen.text_size_7)
            it.text=titleStr
            it.setTextColor(titleTextColor)
            addView(it)
        }

        contentFrame= RelativeLayout(context).also {
            it.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                addRule(END_OF,R.id.titleText)
                addRule(ALIGN_PARENT_RIGHT)
            }
            it.gravity=Gravity.RIGHT
//            it.background= drawable(R.color.Eki_green_3)
            addView(it)
        }

        selectBtn= AutoSizeTextView(context).also {
            it.layoutParams = LayoutParams(dpToPx(100f), dpToPx(25f))

            it.setPadding(7,5,5,7)
            it.gravity=Gravity.CENTER
//            it.textSize= dimen(R.dimen.text_size_6)
            it.text= string(R.string.Choose_coupon_code)
            it.setTextColor(color(R.color.Eki_orange_2))
            it.background= drawable(R.drawable.stroke_round_corner_orange2)

            contentFrame.addView(it)
        }



        deleteBtn=ImageButton(context).also {
            it.layoutParams= LayoutParams(dpToPx(25f), dpToPx(25f))
            it.setImageDrawable(drawable(R.drawable.icon_cancel_orange))
            it.background= drawable(R.color.color_white)
        }

        amtText=AutoSizeTextView(context).also {
            it.layoutParams= LayoutParams(LayoutParams.WRAP_CONTENT, dpToPx(25f)).apply {
                marginStart= dpToPx(5f)
            }
            it.gravity=Gravity.CENTER_VERTICAL
            it.text=""
//            it.textSize=dimen(R.dimen.text_size_7)
            it.setTextColor(color(R.color.Eki_red_1))
        }

        discountFrame=LinearLayout(context).also {
            it.layoutParams= LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
            it.orientation=LinearLayout.HORIZONTAL
            it.gravity=Gravity.CENTER_VERTICAL
            it.addView(deleteBtn)
            it.addView(amtText)
        }

//        info=TextView(context).also {
//            it.layoutParams=LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT).apply {
//                addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//            }
//            it.textSize= dimen(R.dimen.text_size_7)
//            it.setTextColor(getColor(R.color.text_color_1))
//            addView(it)
//        }
    }

    override fun setStyleableRes(): IntArray? = R.styleable.DiscountSelectRow
    override fun setInflatView(): Int =0
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}