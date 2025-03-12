package com.eki.parking.View.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView
import com.eki.parking.extension.color
import com.eki.parking.extension.drawable
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IViewControl

/**
 * Created by Hill on 02,10,2019
 */
class LocationSearchBar(context: Context?, attrs: AttributeSet?) :
    LinearCustomView(context, attrs) {

    private val ll_steamLocomotive:LinearLayout = findViewById(R.id.ll_steamLocomotive)
    private val ll_car:LinearLayout = findViewById(R.id.ll_car)
    private val ll_locomotive:LinearLayout = findViewById(R.id.ll_locomotive)

    interface OnSearchBarClickListener {
        fun onSlmSelect()//選擇汽機車
        fun onCarSelect()//選擇汽車
        fun onLmSelect()//選擇機車
        fun onCancelClick()//當點擊取消紐
    }

    companion object Option {
        const val steamlocomotive = 0//汽機車
        const val car = 1//汽車
        const val locomotive = 2//機車
    }

    private var listener: OnSearchBarClickListener? = null

    private var slmBtn = object : BtnCtrl(steamlocomotive, ll_steamLocomotive) {
        override val textRes: Int
            get() = R.id.tv_steamLocomotive
        override val cancelRes: Int
            get() = R.id.iv_steamLocomotive

        override fun viewAfterClick(clickView: LinearLayout) {
            super.viewAfterClick(clickView)
            listener?.onSlmSelect()
        }

        override fun cancelClick(view: View) {
            removeCanel()
            listener?.onCancelClick()
        }
    }

    private var carBtn = object : BtnCtrl(car, ll_car) {
        override val textRes: Int
            get() = R.id.tv_car
        override val cancelRes: Int
            get() = R.id.iv_car

        override fun viewAfterClick(clickView: LinearLayout) {
            super.viewAfterClick(clickView)
            listener?.onCarSelect()
        }

        override fun cancelClick(view: View) {
            removeCanel()
            listener?.onCancelClick()
        }
    }

    private var lmBtn = object : BtnCtrl(locomotive, ll_locomotive) {
        override val textRes: Int
            get() = R.id.tv_locomotive
        override val cancelRes: Int
            get() = R.id.iv_locomotive

        override fun viewAfterClick(clickView: LinearLayout) {
            super.viewAfterClick(clickView)
            listener?.onLmSelect()
        }

        override fun cancelClick(view: View) {
            removeCanel()
            listener?.onCancelClick()
        }
    }

    private val btnList = arrayListOf(
        slmBtn,
        carBtn,
        lmBtn
    )

    init {
        slmBtn.select()
    }


    fun showCancel() {
        btnList.firstOrNull { it.isSelect }?.showCancel()
    }

    fun clean() {
    }

    fun setBarListener(l: OnSearchBarClickListener) {
        listener = l
    }

    private abstract inner class BtnCtrl(var flag: Int, private var view: LinearLayout) :
        IViewControl<LinearLayout>() {
        init {
            useDefaultSet(view)
        }

        override fun clickViewSet(clickView: LinearLayout) {
            textView = clickView.findViewById(textRes)
            cancelBtn = clickView.findViewById(cancelRes)
            cancelBtn.setOnClickListener {
                cancelClick(it)
            }
        }

        abstract val textRes: Int
        abstract val cancelRes: Int
        abstract fun cancelClick(view: View)

        lateinit var textView: TextView
        lateinit var cancelBtn: ImageView

        var isSelect = false

        override fun viewAfterClick(clickView: LinearLayout) {
            btnList.firstOrNull { it.flag == flag }.notNull {
                it.select()
            }
            btnList.filter { it.flag != flag }
                .forEach {
                    it.unSelect()
                }
        }

        fun showCancel() {
            cancelBtn.visibility = VISIBLE
        }

        fun removeCanel() {
            cancelBtn.visibility = GONE
        }

        fun select() {
            isSelect = true
            view.background = drawable(R.drawable.shape_round_stroke_orange)
            textView.setTextColor(color(R.color.Eki_orange_4))
            //避免重複點擊觸發事件
            view.isClickable = false
        }

        fun unSelect() {
            isSelect = false
            view.background = drawable(R.drawable.shape_round_stroke_white)
            textView.setTextColor(color(R.color.text_color_1))
            view.isClickable = true
        }
    }

    override fun setInflatView(): Int = R.layout.item_location_search_bar

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null
}