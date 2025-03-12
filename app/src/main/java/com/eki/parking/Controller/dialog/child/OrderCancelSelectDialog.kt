package com.eki.parking.Controller.dialog.child

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IDialogFeatureSet
import com.eki.parking.databinding.DialogOrderCancelSelectBinding
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/12/03
 */
class OrderCancelSelectDialog : DialogChildFrag<OrderCancelSelectDialog>(),
    IDialogFeatureSet<OrderCancelSelectDialog>, IFragViewBinding {

    private lateinit var binding: DialogOrderCancelSelectBinding

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = DialogOrderCancelSelectBinding.inflate(inflater, container, false)
        return binding
    }

    var onCancelByPerson: (() -> Unit)? = null
    var onCancelNonPerson: (() -> Unit)? = null

    override fun initFragView() {

        binding.cancelByPerson.setOnClickListener {
            onCancelByPerson?.invoke()
            dissmissDialog()
        }
        binding.cancelNonPerson.setOnClickListener {
            onCancelNonPerson?.invoke()
            dissmissDialog()
        }

        binding.cancelBtn.setOnClickListener {
            dissmissDialog()
        }
    }

    override val frag: OrderCancelSelectDialog
        get() = this
    override val title: String
        get() = ""
    override val titleSet: IDialogFeatureSet.ITitleBarSet?
        get() = IDialogFeatureSet.NoDialogTitle()
    override val contentSet: IDialogFeatureSet.IContentSet?
        get() = null
    override val btnFrameSet: IDialogFeatureSet.IBtnFrameSet
        get() = IDialogFeatureSet.NoBottomBtn()

    override fun onDismissCheck(): Boolean = true

}