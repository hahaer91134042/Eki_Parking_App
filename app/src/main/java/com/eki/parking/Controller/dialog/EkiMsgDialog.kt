package com.eki.parking.Controller.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IDialogFeatureSet
import com.eki.parking.R
import com.eki.parking.databinding.DialogMsgViewBinding
import com.eki.parking.extension.dimen
import com.eki.parking.extension.drawable
import com.eki.parking.extension.string
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/01/10
 */
class EkiMsgDialog(private var style: Style? = null) : DialogChildFrag<EkiMsgDialog>(),
    IDialogFeatureSet<EkiMsgDialog>,
    IDialogFeatureSet.IBtnClickEvent, IFragViewBinding {

    private lateinit var binding: DialogMsgViewBinding
    interface Style {
        val btnSet: BtnSet
        val contentSet: ContentSet
    }

    enum class BtnSet(var set: IDialogFeatureSet.IBtnFrameSet) {
        Default(DefaultBtnFrame()),
        Single(SingleBtnFrame()),
        Skip(SkipBtnFrame())
    }

    enum class ContentSet(var res: Int) {
        Default(R.color.colorless),
        Set1(R.drawable.bg_dialog_finish)
    }

    lateinit var msg: String
    var btnSet: BtnSet

    //    var msgContentSet=ContentSet.Default
    var msgTextSize = dimen(R.dimen.text_size_6)
    override var determinClick = {}
    override var cancelClick = {}

    init {
        btnSet = style?.btnSet ?: BtnSet.Default
    }

    override fun initFragView() {
        style.notNull {
            binding.parentView.background = drawable(it.contentSet.res)
        }

        binding.msgText.textSize = msgTextSize
        binding.msgText.text = msg
        if (btnSet != BtnSet.Default) {
            btnSet.set.cancelClick = cancelClick
            btnSet.set.determinClick = determinClick
        }
    }

    override val frag: EkiMsgDialog
        get() = this
    override val title: String
        get() = ""
    override val titleSet: IDialogFeatureSet.ITitleBarSet?
        get() = IDialogFeatureSet.NoDialogTitle()
    override val contentSet: IDialogFeatureSet.IContentSet?
        get() = null
    override val btnFrameSet: IDialogFeatureSet.IBtnFrameSet
        get() = btnSet.set

    override fun onDismissCheck(): Boolean {
        return true
    }

    private class DefaultBtnFrame : IDialogFeatureSet.IBtnFrameSet()

    private class SingleBtnFrame : IDialogFeatureSet.IBtnFrameSet() {
        override val useDefault: Boolean
            get() = false
        override val btnContent: IDialogFeatureSet.IClickBtnFrame?
            get() = object : IDialogFeatureSet.IClickBtnFrame() {
                override val viewRes: Int
                    get() = R.layout.item_single_btn_frame

                override fun setUpView(v: View) {
                    var determinBtn = v.findViewById<Button>(R.id.determinBtn)
                    determinBtn.setOnClickListener {
                        dismissClick?.invoke()
                        determinClick()
                    }
                }
            }
    }

    private class SkipBtnFrame : IDialogFeatureSet.IBtnFrameSet() {
        override val useDefault: Boolean
            get() = false
        override val btnContent: IDialogFeatureSet.IClickBtnFrame?
            get() = object : IDialogFeatureSet.IClickBtnFrame() {
                override val viewRes: Int
                    get() = R.layout.item_btn_frame

                override fun setUpView(v: View) {
                    var cancelBtn = v.findViewById<Button>(R.id.cancelBtn)
                    cancelBtn.text = string(R.string.Skip)
                    var determinBtn = v.findViewById<Button>(R.id.determinBtn)
                    cancelBtn.setOnClickListener {
                        dismissClick?.invoke()
                        cancelClick()
                    }
                    determinBtn.setOnClickListener {
                        dismissClick?.invoke()
                        determinClick()
                    }
                }
            }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = DialogMsgViewBinding.inflate(inflater,container,false)
        return binding
    }
}