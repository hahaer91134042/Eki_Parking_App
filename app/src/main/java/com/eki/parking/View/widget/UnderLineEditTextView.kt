package com.eki.parking.View.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.eki.parking.R
import com.eki.parking.View.abs.UnderLineLayout
import com.eki.parking.extension.color
import com.eki.parking.extension.pxToDp
import com.hill.devlibs.impl.IViewControl
import com.hill.devlibs.util.StringUtil

/**
 * Created by Hill on 2019/12/04
 */

class UnderLineEditTextView(context: Context?, attrs: AttributeSet?) :
    UnderLineLayout(context, attrs), TextWatcher {

    companion object {
        const val NONE = -1
        const val TEXT = 0
        const val PWD = 1
        const val NUMBER = 2
    }

    private val inputTex: EditText = findViewById(R.id.inputTex)
    private val tailIcon: ImageView = findViewById(R.id.tailIcon)
    private val tailText: TextView = findViewById(R.id.tailText)
    private val headerIcon: ImageView = findViewById(R.id.headerIcon)

    var input: String
        set(value) {
            inputTex.setText(value)
        }
        get() = StringUtil.cleanChar(inputTex.text.toString())

    var tailControl: IViewControl<ImageView>? = null
        set(value) {
            tailIcon.visibility = View.VISIBLE
            tailText.visibility = View.GONE

            value?.clickViewSet(tailIcon)
            field = value
        }

    var tailTextControl: IViewControl<TextView>? = null
        set(value) {
            tailIcon.visibility = View.GONE
            tailText.visibility = View.VISIBLE

            value?.clickViewSet(tailText)
            field = value
        }

    init {
        tailIcon.setOnClickListener {
            tailControl?.onAction(tailIcon, inputTex)
        }

        tailText.setOnClickListener {
            tailTextControl?.onAction(tailText, inputTex)
        }

        inputTex.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                onFocuse?.invoke()
            else
                onNoFocuse?.invoke()
        }
        inputTex.setOnClickListener {
            onInputClick?.invoke()
        }

        inputTex.addTextChangedListener(this)
    }

    var normalColor = Color.BLACK
    var errorColor = Color.RED

    private var onInputChange: ((String) -> Unit)? = null
    private var onNoFocuse: (() -> Unit)? = null
    private var onFocuse: (() -> Unit)? = null
    private var onInputClick: (() -> Unit)? = null

    fun whenInputOnFocuse(back: () -> Unit) {
        onFocuse = back
    }

    fun whenInputNoFocuse(back: () -> Unit) {
        onNoFocuse = back
    }

    fun whenInputChange(back: ((String) -> Unit)?) {
        onInputChange = back
    }

    fun whenInputClick(back: () -> Unit) {
        onInputClick = back
    }

    fun showNormal() {
        setLineColor(normalColor)
    }

    fun showError() {
        setLineColor(errorColor)
    }

    fun setHeaderIcon(@DrawableRes res: Int) {
        headerIcon.setImageResource(res)
    }

    fun setInputType(type: Int) {
        inputTex.inputType = when (type) {
            PWD -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            NUMBER -> InputType.TYPE_CLASS_NUMBER
            else -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
        }

    }

    fun setTextSize(size: Float) {
        inputTex.textSize = size
    }

    fun setHintStr(str: String) {
        inputTex.hint = str
    }

    fun setMaxInput(max: Int) {
        var lengthFilter = InputFilter.LengthFilter(max)
        inputTex.filters = arrayOf(lengthFilter)
    }

    fun closeEdit() {
        inputTex.isFocusable = false
    }

    fun openEdit() {
        inputTex.isFocusable = true
    }

    override fun setUpInflatView(typedArray: TypedArray) {
        var inputSize = typedArray?.getDimension(
            R.styleable.UnderLineEditTextView_ule_input_textSize,
            pxToDp(16)
        )
        var inputType = typedArray?.getInt(R.styleable.UnderLineEditTextView_ule_inputType, NONE)
        setTextSize(inputSize)
        if (inputType != NONE)
            setInputType(inputType)

        inputTex.setTextColor(color(R.color.text_color_1))
        inputTex.setHintTextColor(color(R.color.light_gray5))

        var textStr = typedArray?.getText(R.styleable.UnderLineEditTextView_ule_input_text)
        if (!textStr.isNullOrEmpty())
            inputTex.setText(textStr)

        var hintStr = typedArray?.getString(R.styleable.UnderLineEditTextView_ule_input_hint)

        if (!hintStr.isNullOrEmpty())
            setHintStr(hintStr)

        var maxNum = typedArray?.getInteger(R.styleable.UnderLineEditTextView_ule_max_input_num, 0)
        if (maxNum > 0) {
            setMaxInput(maxNum)
        }

        var headerIconRes =
            typedArray?.getResourceId(R.styleable.UnderLineEditTextView_ule_headerIcon, 0)
        if (headerIconRes > 0)
            headerIcon.setImageResource(headerIconRes)


        var lineColor = typedArray?.getColor(R.styleable.UnderLineEditTextView_ule_line_color, 0)
        when (lineColor > 0) {
            true -> normalColor = lineColor
            else -> {
                var lineColorRes =
                    typedArray?.getResourceId(R.styleable.UnderLineEditTextView_ule_line_color, 0)
                if (lineColorRes > 0)
                    normalColor = color(lineColorRes)
            }
        }

        var eColor = typedArray?.getColor(R.styleable.UnderLineEditTextView_ule_line_error_color, 0)
        when (eColor > 0) {
            true -> errorColor = eColor
            else -> {
                var lineColorRes = typedArray?.getResourceId(
                    R.styleable.UnderLineEditTextView_ule_line_error_color,
                    0
                )
                if (lineColorRes > 0)
                    errorColor = color(lineColorRes)
            }
        }

        inputTex.isEnabled =
            typedArray?.getBoolean(R.styleable.UnderLineEditTextView_ule_input_enable, true)

        setLineColor(normalColor)
    }

    override fun setStyleableRes(): IntArray? = R.styleable.UnderLineEditTextView
    override fun setInflatView(): Int = R.layout.item_inputview
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onInputChange?.invoke(StringUtil.cleanChar(s.toString()))
    }
}