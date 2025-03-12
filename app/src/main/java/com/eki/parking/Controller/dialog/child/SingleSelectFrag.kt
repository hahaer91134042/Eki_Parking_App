package com.eki.parking.Controller.dialog.child

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IDialogFeatureSet
import com.eki.parking.R
import com.eki.parking.View.recycleview.BaseRecycleView
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.databinding.ItemRecycleviewBinding
import com.eki.parking.extension.dimen
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2019/12/31
 */
abstract class SingleSelectFrag<T:BaseAdapter<*>>:
    DialogChildFrag<SingleSelectFrag<*>>(),
    IDialogFeatureSet<SingleSelectFrag<*>>,IFragViewBinding{

    private lateinit var binding: ItemRecycleviewBinding
    override fun initFragView() {
        setUpListView(binding.recycleView)
        binding.recycleView.layoutParams.height=dialogHeight
        binding.recycleView.adapter=setOptionAdaptor()
    }

    open fun setUpListView(recycleView: BaseRecycleView){
        recycleView.useSimpleDivider()
    }

    open val dialogHeight:Int=ViewGroup.LayoutParams.WRAP_CONTENT

    override val titleSet: IDialogFeatureSet.ITitleBarSet?
        get() = object :IDialogFeatureSet.ITitleBarSet(){
            override val backgroundRes: Int
                get() = R.drawable.shape_eki_dialog_title_orange
            override val titleTextColor: Int
                get() = Color.WHITE
            override val titleTextSize: Float
                get() = dimen(R.dimen.text_size_6)
            override val closeBtnRes: Int
                get() = R.drawable.icon_close_white
        }

    override val contentSet: IDialogFeatureSet.IContentSet?
        get() = null

    override val btnFrameSet: IDialogFeatureSet.IBtnFrameSet
        get() = object :IDialogFeatureSet.IBtnFrameSet(){
            override val useDefault: Boolean
                get() = false
        }

    abstract fun setOptionAdaptor():T
    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = ItemRecycleviewBinding.inflate(inflater,container,false)
        return binding
    }

    override val frag: SingleSelectFrag<*>
        get() = this
}