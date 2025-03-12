package com.eki.parking.View.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import com.eki.parking.Model.EnumClass.SiteSize
import com.eki.parking.Model.EnumClass.SiteType
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.View.abs.FrameCustomView
import com.eki.parking.View.abs.LinearCustomView
import com.hill.devlibs.extension.cleanTex

/**
 * Created by Hill on 2021/12/20
 */
class SiteRestricSizeView(context: Context?, attrs: AttributeSet?) :
    FrameCustomView(context, attrs) {

    private var mLoc: ManagerLocation? = null
    private var viewCtrl: RestricSizeCtrl? = null

    fun creatFrom(loc: ManagerLocation) {
        mLoc = loc
        removeAllViews()

        viewCtrl = when (mLoc?.Info?.siteSize) {
            SiteSize.Motor -> MotorRestricSizeView(context)
            else -> CarRestricSizeView(context)
        }

        addView(viewCtrl?.view)
    }

    fun getEditLocInfo(loc: ManagerLocation) {
        viewCtrl?.editLocInfo(loc)
    }

    override fun setInflatView(): Int = 0
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null

    private inner class CarRestricSizeView(context: Context?) :
        LinearCustomView(context),
        RestricSizeCtrl {

        private lateinit var typeSelector: RadioGroup
        private lateinit var sizeSelector: RadioGroup
        private lateinit var heightSelector: RadioGroup
        private lateinit var heightInput: EditText
        private lateinit var weightSelector: RadioGroup
        private lateinit var weightInput: EditText

        override fun initInFlaterView() {
            typeSelector = findViewById(R.id.typeSelector)
            sizeSelector = findViewById(R.id.sizeSelector)
            heightSelector = findViewById(R.id.heightSelector)
            heightInput = findViewById(R.id.heightInput)
            weightSelector = findViewById(R.id.weightSelector)
            weightInput = findViewById(R.id.weightInput)

            typeSelector.check(
                when (mLoc?.Info?.siteType) {
                    SiteType.Flat -> R.id.flatSite
                    else -> R.id.mechSite
                }
            )
            sizeSelector.check(
                when (mLoc?.Info?.siteSize) {
                    SiteSize.Small -> R.id.small
                    else -> R.id.standard
                }
            )
            when {
                mLoc?.Info?.Height!! > 0 -> {
                    heightSelector.check(R.id.limitHeight)

                    heightInput.isEnabled = true
                    heightInput.setText(mLoc?.Info?.Height.toString())
                }
                else -> {
                    heightSelector.check(R.id.nonHeight)
                }
            }
            when {
                mLoc?.Info?.Weight!! > 0 -> {
                    weightSelector.check(R.id.limitWeight)
                    weightInput.isEnabled = true
                    weightInput.setText(mLoc?.Info?.Weight.toString())
                }
                else -> {
                    weightSelector.check(R.id.nonWeight)
                }
            }

            heightSelector.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.limitHeight -> {
                        heightInput.isEnabled = true
                    }
                    else -> {
                        heightInput.setText(
                            when {
                                mLoc?.Info?.Height!! <= 0 -> 0.toString()
                                else -> mLoc?.Info?.Height.toString()
                            }
                        )
                        heightInput.isEnabled = false
                    }
                }
            }

            weightSelector.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.limitWeight -> {
                        weightInput.isEnabled = true
                    }
                    else -> {
                        weightInput.setText(
                            when {
                                mLoc?.Info?.Weight!! <= 0 -> 0.toString()
                                else -> mLoc?.Info?.Weight.toString()
                            }
                        )
                        weightInput.isEnabled = false
                    }
                }
            }
        }

        override val view: View
            get() = this

        override fun editLocInfo(loc: ManagerLocation) {
            loc.Info?.siteType = when (typeSelector.checkedRadioButtonId) {
                R.id.flatSite -> SiteType.Flat
                else -> SiteType.Mechanical
            }
            loc.Info?.siteSize = when (sizeSelector.checkedRadioButtonId) {
                R.id.small -> SiteSize.Small
                else -> SiteSize.Standar
            }

            loc.Info?.Height = when (heightSelector.checkedRadioButtonId) {
                R.id.limitHeight -> {
                    var height = 0.0
                    if (heightInput.text.toString().cleanTex.isNotEmpty())
                        height = heightInput.text.toString().cleanTex.toDouble()
                    if (height < 0)
                        height = 0.0
                    height
                }
                else -> -1.0
            }
            loc.Info?.Weight = when (weightSelector.checkedRadioButtonId) {
                R.id.limitWeight -> {
                    var weight = weightInput.text.toString().cleanTex.toDouble()
                    if (weightInput.text.toString().cleanTex.isNotEmpty())
                        weight = weightInput.text.toString().cleanTex.toDouble()
                    if (weight < 0)
                        weight = 0.0
                    weight
                }
                else -> -1.0
            }
        }

        override fun setInflatView(): Int = R.layout.item_site_restriction_car
        override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null
    }

    private inner class MotorRestricSizeView(context: Context?) :
        LinearCustomView(context),
        RestricSizeCtrl {

        override fun initInFlaterView() {

        }

        override val view: View
            get() = this

        override fun editLocInfo(loc: ManagerLocation) {
            //機車暫時沒有什麼限制條件可供編輯
        }

        override fun setInflatView(): Int = R.layout.item_site_restriction_motor
        override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null
    }

    private interface RestricSizeCtrl {
        val view: View
        fun editLocInfo(loc: ManagerLocation)
    }
}