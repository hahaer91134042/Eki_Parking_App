package com.eki.parking.Controller.activity.frag.OrderDetail.child

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IDialogFeatureSet
import com.eki.parking.R
import com.eki.parking.databinding.DialogRatingPickerBinding
import com.eki.parking.extension.string
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/05/20
 */
class RatingFrag : DialogChildFrag<RatingFrag>(),
    IDialogFeatureSet<RatingFrag>, IFragViewBinding {

    private lateinit var binding: DialogRatingPickerBinding

    companion object {
        fun creat(): RatingFrag = RatingFrag()
            .also { }
    }

    class FragTitleSet {
        var title = string(R.string.Evaluate_the_owner)
        var titleSet: IDialogFeatureSet.ITitleBarSet = IDialogFeatureSet.GreenDialogTitle()
    }

    var fragTitleSet = FragTitleSet()

    private var start = 0.0
    var onRatingStartBack: ((Double) -> Unit)? = null
    override fun initFragView() {
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            start = rating.toDouble()
        }

    }

    override fun onDismissCheck(): Boolean {
        onRatingStartBack?.invoke(start)
        return true
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = DialogRatingPickerBinding.inflate(inflater, container, false)
        return binding
    }

    override val frag: RatingFrag
        get() = this
    override val title: String
        get() = fragTitleSet.title
    override val titleSet: IDialogFeatureSet.ITitleBarSet?
        get() = fragTitleSet.titleSet
    override val contentSet: IDialogFeatureSet.IContentSet?
        get() = null
    override val btnFrameSet: IDialogFeatureSet.IBtnFrameSet
        get() = SingleBtnFrame()

    private class SingleBtnFrame : IDialogFeatureSet.IBtnFrameSet() {
        override val useDefault: Boolean
            get() = false
        override val btnContent: IDialogFeatureSet.IClickBtnFrame?
            get() = object : IDialogFeatureSet.IClickBtnFrame() {
                override val viewRes: Int
                    get() = R.layout.item_single_btn_frame

                override fun setUpView(v: View) {
                    var determinBtn = v.findViewById<Button>(R.id.determinBtn)
                    determinBtn.text = string(R.string.Finish)
                    determinBtn.setOnClickListener {
                        dismissClick?.invoke()
                    }
                }
            }
    }
}