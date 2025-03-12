package com.eki.parking.View.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.eki.parking.Model.EnumClass.Hour12Enum
import com.eki.parking.R
import com.eki.parking.View.abs.RelativeCustomView

/**
 * Created by Hill on 04,10,2019
 */
class DayTimePanel(context: Context?, attrs: AttributeSet?) : RelativeCustomView(context, attrs) {

    private val amTex: TextView = findViewById(R.id.amTex)
    private val pmTex: TextView = findViewById(R.id.pmTex)
    private val hourTex: TextView = findViewById(R.id.hourTex)
    private val minTex: TextView = findViewById(R.id.minTex)

    init {
        amTex.setOnClickListener {
            hc?.invoke(Hour12Enum.AM)
            amTex.setTextColor(getColor(R.color.Eki_orange_2))
            pmTex.setTextColor(getColor(R.color.text_color_1))
        }
        pmTex.setOnClickListener {
            hc?.invoke(Hour12Enum.PM)
            amTex.setTextColor(getColor(R.color.text_color_1))
            pmTex.setTextColor(getColor(R.color.Eki_orange_2))
        }
        hourTex.setOnClickListener {
            hl?.invoke()
            hourTex.setTypeface(null,Typeface.BOLD)
            minTex.setTypeface(null,Typeface.NORMAL)
        }
        minTex.setOnClickListener {
            ml?.invoke()
            hourTex.setTypeface(null,Typeface.NORMAL)
            minTex.setTypeface(null,Typeface.BOLD)
        }
    }

    private var hc: ((Hour12Enum) -> Unit)? =null
    private var hl:(()->Unit)?=null
    private var ml:(()->Unit)?=null

    var hour: Int
        get() = hourTex.text.toString().toInt()
        set(value) {
            hourTex.text = "%02d".format(value)
        }
    var min:Int
        get() = minTex.text.toString().toInt()
        set(value) {
            minTex.text = "%02d".format(value)
        }

    fun whenHour12Click(f:(Hour12Enum)->Unit){
        hc={
            f(it)
        }
    }
    fun whenHourTexClick(l:()->Unit){
        hl={l()}
    }
    fun whenMinTexClick(l:()->Unit){
        ml={l()}
    }

    override fun setInflatView(): Int = R.layout.item_day_time_panel

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null
}