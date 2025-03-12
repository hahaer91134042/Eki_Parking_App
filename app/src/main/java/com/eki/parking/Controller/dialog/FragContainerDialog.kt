package com.eki.parking.Controller.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.*
import com.eki.parking.Controller.dialog.abs.BaseFragDialog
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IDialogFeatureSet
import com.eki.parking.R
import com.hill.devlibs.EnumClass.DialogOption
import com.hill.devlibs.extension.notNull

/**
 * Created by Hill on 09,10,2019
 */
open class FragContainerDialog : BaseFragDialog<DialogChildFrag<*>>() {

    private lateinit var childDelegate: IDialogFeatureSet<*>

    private var childFrag: DialogChildFrag<*>? = null
    private var btnEvent: IDialogFeatureSet.IBtnClickEvent? = null

    override fun setUpDialogFeature(): DialogSetting = DialogSetting(
        DialogOption.NoTitle,
        DialogOption.CantCancelable,
        DialogOption.Colorless,
        DialogOption.FullDialogWidth
    )

    fun <F : DialogChildFrag<*>> setContent(set: IDialogFeatureSet<F>) {
        childFrag = set.frag
        childDelegate = set
        if (childFrag is IDialogFeatureSet.IBtnClickEvent)
            btnEvent = childFrag as IDialogFeatureSet.IBtnClickEvent
    }

    override fun initDialogViewFrag(): DialogChildFrag<*>? = childFrag

    override fun setParentViewInflate(): Int = R.layout.dialog_select_frag_view

    override fun setUpParentDialogComponent() {
        val dialogTitle = view?.findViewById<TextView>(R.id.dialogTitle)
        val titleFrame = view?.findViewById<RelativeLayout>(R.id.titleFrame)
        val closeBtn = view?.findViewById<ImageView>(R.id.closeBtn)
        val contentFrame = view?.findViewById<LinearLayout>(R.id.contentFrame)

        dialogTitle?.text = childDelegate.title

        childDelegate.titleSet.notNull { style ->
            titleFrame?.visibility = when (style.visible) {
                true -> View.VISIBLE
                else -> View.GONE
            }
            if (style.backgroundRes > 0) {
                titleFrame?.setBackgroundResource(style.backgroundRes)
            }
            dialogTitle?.apply {
                setTextColor(style.titleTextColor)
                textSize = style.titleTextSize
            }

            if (style.closeBtnRes > 0) {
                closeBtn?.apply {
                    setImageResource(style.closeBtnRes)
                    visibility = View.VISIBLE
                    setOnClickListener { dismiss() }
                }
            }
        }
        childDelegate.contentSet.notNull { set ->
            contentFrame?.setBackgroundResource(set.backgroundRes)
        }

        val btnFrame = view?.findViewById<FrameLayout>(R.id.btnFrame)
        when (childDelegate.btnFrameSet.useDefault) {
            true -> {
                val btnFrame = view?.findViewById<ViewStub>(R.id.btnStub)?.inflate()
                val cancelBtn = btnFrame?.findViewById<Button>(R.id.cancelBtn)
                val determinBtn = btnFrame?.findViewById<Button>(R.id.determinBtn)
                cancelBtn?.setOnClickListener {
                    btnEvent?.cancelClick?.invoke()
                    dismiss()
                }
                determinBtn?.setOnClickListener {
                    if (childDelegate.onDismissCheck()) {
                        btnEvent?.determinClick?.invoke()
                        dismiss()
                    }
                }
            }
            else -> {
                childDelegate.btnFrameSet.btnContent.notNull { set ->
                    val view = LayoutInflater.from(context).inflate(set.viewRes, null)
                    set.setUpView(view)
                    set.dismissClick = {
                        if (childDelegate.onDismissCheck()) {
                            dismiss()
                        }
                    }
                    btnFrame?.addView(view)
                }
            }
        }

    }
}