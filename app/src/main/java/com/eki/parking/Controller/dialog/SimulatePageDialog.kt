package com.eki.parking.Controller.dialog

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.eki.parking.AppFlag
import com.eki.parking.Controller.builder.ISerialBuilder
import com.eki.parking.Controller.dialog.abs.BaseFragDialog
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IOnSerialContent
import com.eki.parking.Controller.impl.ISerialDialog
import com.eki.parking.Controller.impl.ISerialEvent
import com.eki.parking.R
import com.hill.devlibs.EnumClass.DialogOption
import com.hill.devlibs.extension.getColor
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.ITheme

/**
 * Created by Hill on 2020/04/10
 * 模擬整個畫面的 activity page
 */

class SimulatePageDialog<B : ISerialBuilder<ISerialDialog>>(private var serialBuilder: B) :
    BaseFragDialog<DialogChildFrag<*>>(),
    IOnSerialContent<ISerialDialog> {

    private var toolbar: RelativeLayout? = null
    private var titleText: TextView? = null
    private var cancelBtn: ImageView? = null

    companion object {
        @JvmStatic
        fun initWithFrag(
            frag: DialogChildFrag<*>,
            title: String = ""
        ): SimulatePageDialog<ISerialBuilder<ISerialDialog>> {
            return SimulatePageDialog(object : ISerialBuilder<ISerialDialog>() {
                override fun start(): ISerialDialog = object : ISerialDialog {
                    override val frag: DialogChildFrag<*>
                        get() = frag
                    override val title: String
                        get() = title

                    override fun setEvent(event: ISerialEvent) {}
                    override fun next(): ISerialDialog? = null
                }

                override fun onNext() {}
                override fun onPrevious() {}
            })
        }
    }

    override fun initDialogViewFrag(): DialogChildFrag<*>? {
        serialBuilder.onSerialContent = this
        val dialog = serialBuilder.start()
        toolbar = view?.findViewById<RelativeLayout>(R.id.toolbar)
        titleText = view?.findViewById(R.id.titleText)
        cancelBtn = view?.findViewById(R.id.cancelBtn)

        if (toolbar != null) {
            setTitle(dialog)
            setDialogTheme(dialog.frag)
        }
        return dialog.frag
    }

    private fun setDialogTheme(o: Any) {
        if (o is ITheme<*>) {
            //應該沒用 改變activity的theme 沒意義
            //activity?.setTheme(o.theme.styleRes())
            toolbar?.setBackgroundColor(getColor(o.theme.colorRes()))
        }
    }

    private fun setTitle(dialog: ISerialDialog) {
        titleText?.text = dialog.title
        if (dialog is ISerialDialog.TitleSet) {
            toolbar?.visibility = when (dialog.titleVisible) {
                true -> View.VISIBLE
                else -> View.GONE
            }
        } else {
            toolbar?.visibility = View.VISIBLE
        }
    }

    override fun setParentViewInflate(): Int = R.layout.dialog_simulate_page

    override fun setUpParentDialogComponent() {
        cancelBtn?.setOnClickListener { dismiss() }

    }

    override fun onNext(next: ISerialDialog) {
        setTitle(next)
        setDialogTheme(next.frag)
        replaceFragment(next.frag, next.frag.TAG, null, FragSwitcher.SWITCH_FADE)
    }

    override fun onPrevious(pre: ISerialDialog) {
        setTitle(pre)
        setDialogTheme(pre.frag)
        replaceFragment(pre.frag, pre.frag.TAG, null, FragSwitcher.SWITCH_FADE)
    }

    override fun onRestartViewState(savedState: Bundle?) {
        serialBuilder = savedState?.getSerializable(AppFlag.DATA_FLAG) as B
        setUpParentDialogComponent()
    }

    override fun onSaveState(outState: Bundle): Bundle {
        outState.putSerializable(AppFlag.DATA_FLAG, serialBuilder)
        return outState
    }

    override fun setUpDialogFeature(): DialogSetting = DialogSetting(
        DialogOption.NoTitle,
        DialogOption.Colorless,
        DialogOption.FullScreen,
        DialogOption.NoDim
    )
}