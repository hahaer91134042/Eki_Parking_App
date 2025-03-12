package com.eki.parking.View

import android.content.Context
import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.eki.parking.AppConfig
import com.eki.parking.Controller.activity.intent.HtmlIntent
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView
import com.eki.parking.extension.color
import com.eki.parking.extension.dimen
import com.eki.parking.extension.startActivityAnim
import com.hill.devlibs.EnumClass.SpanHelper
import com.hill.devlibs.util.StringUtil

/**
 * Created by Hill on 2019/12/18
 */
class PrivacyPolicyView(context: Context?, attrs: AttributeSet?) : LinearCustomView(context, attrs) {

    var isChecked=false
    private var onCheckListener:((Boolean)->Unit)?=null

    private var imgView: ImageView = findViewById(R.id.imgView)
    private var textView: TextView = findViewById(R.id.textView)

    init {
        imgView.setImageResource(R.drawable.icon_checkbox_false_orange)
        imgView.setOnClickListener {
            isChecked=!isChecked
            if (isChecked){
                imgView.setImageResource(R.drawable.icon_checkbox_true_orange)
            }else{
                imgView.setImageResource(R.drawable.icon_checkbox_false_orange)
            }
            onCheckListener?.invoke(isChecked)
        }
        textView.textSize= dimen(R.dimen.text_size_5)
        textView.setTextColor(color(R.color.text_color_1))
        StringUtil.getSpanBuilder()
                .setSpanString(getString(R.string.Read_and_agree)+" ",SpanHelper.TYPE_NORMAL)
                .setSpanString(getString(R.string.Privacy_Policy),SpanHelper.TYPE_NORMAL
                        .setTextColor(color(R.color.Eki_orange_4))
                        .setClick(object :StringSpanClick(){
                            override fun onClick(widget: View) {
//                                Log.w("Open privacy policy")
                                startActivityAnim(HtmlIntent(context!!,AppConfig.Url.policyLink))
                            }
                        }))
                .setSpanString("、",SpanHelper.TYPE_NORMAL)
                .setSpanString("社群守則",SpanHelper.TYPE_NORMAL.setTextColor(color(R.color.Eki_orange_4))
                        .setClick(object :StringSpanClick(){
                            override fun onClick(widget: View) {
//                                Log.d("open rule")
                                startActivityAnim(HtmlIntent(context!!,AppConfig.Url.regulationLink))
                            }
                        }))
                .setSpanString("、", SpanHelper.TYPE_NORMAL)
                .setSpanString("APP條款", SpanHelper.TYPE_NORMAL.setTextColor(color(R.color.Eki_orange_4))
                        .setClick(object : StringSpanClick() {
                            override fun onClick(widget: View) {
//                                Log.i("open APP rule")
                                startActivityAnim(HtmlIntent(context!!,AppConfig.Url.appMemberLink))
                            }
                        }))
                .into(textView)

    }
    fun onCheckClick(back:(Boolean)->Unit){
        onCheckListener=back
    }

    override fun setInflatView(): Int = R.layout.item_group_type_5
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null

    private abstract class StringSpanClick:ClickableSpan(){
        override fun updateDrawState(ds: TextPaint) {
            ds.color= color(R.color.Eki_orange_4)
            ds.isUnderlineText=true
            ds.bgColor=Color.WHITE
        }
    }
}