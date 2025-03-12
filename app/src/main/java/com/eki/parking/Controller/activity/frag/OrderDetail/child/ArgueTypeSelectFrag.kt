package com.eki.parking.Controller.activity.frag.OrderDetail.child

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IDialogFeatureSet
import com.eki.parking.Model.EnumClass.ArgueSource
import com.eki.parking.Model.EnumClass.ArgueType
import com.eki.parking.R
import com.eki.parking.View.ViewParams
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.databinding.ItemRecycleviewBinding
import com.eki.parking.extension.dimen
import com.eki.parking.extension.dpToPx
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2020/05/22
 */
class ArgueTypeSelectFrag(private var argueSource: ArgueSource = ArgueSource.Manager) :
    DialogChildFrag<ArgueTypeSelectFrag>(),
    IDialogFeatureSet<ArgueTypeSelectFrag>, IFragViewBinding {

    private lateinit var binding: ItemRecycleviewBinding
    private var list = listOf(
        ArgueType.TrashOrDamaged,
        ArgueType.BadAttitude,
        ArgueType.IllegalParking,
        ArgueType.FakePhoto,
        ArgueType.Other,
        ArgueType.Report
    )

    var onSelectArgueType: (ArgueType) -> Unit = {}

    override fun initFragView() {
        binding.recycleView.useSimpleDivider()

        binding.recycleView.adapter = TypeAdaptor().apply {
            setItemListClickListener { position ->

                Log.d("select type->${list[position]}")
                onSelectArgueType(list[position])
                dissmissDialog()
            }
        }
    }

    override val frag: ArgueTypeSelectFrag
        get() = this
    override val title: String
        get() = "申訴事項"
    override val titleSet: IDialogFeatureSet.ITitleBarSet?
        get() = when (argueSource) {
            ArgueSource.Manager -> IDialogFeatureSet.GreenDialogTitle()
            else -> IDialogFeatureSet.OrangeDialogTitle()
        }
    override val contentSet: IDialogFeatureSet.IContentSet?
        get() = IDialogFeatureSet.DefaultContentSet()
    override val btnFrameSet: IDialogFeatureSet.IBtnFrameSet
        get() = IDialogFeatureSet.NoBottomBtn()

    override fun onDismissCheck(): Boolean {
        return true
    }

    private inner class TypeAdaptor : BaseAdapter<TypeItem>(context) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeItem =
            TypeItem(getItemView(R.layout.item_textview, parent)).apply { init() }

        override fun onBindViewHolder(item: TypeItem, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(list[position])
        }

        override fun getItemCount(): Int = list.size
    }

    private class TypeItem(itemView: View) : ItemLayout<ArgueType>(itemView) {

        var textView = itemView.findViewById<TextView>(R.id.textView)

        override fun init() {
            textView.layoutParams = ViewGroup.LayoutParams(ViewParams.MATCH_PARENT, dpToPx(45f))
            textView.textSize = dimen(R.dimen.text_size_6)
            textView.setPadding(dpToPx(25f), 0, 0, 0)
            textView.gravity = Gravity.CENTER_VERTICAL
            textView.setOnClickListener { itemClick() }
        }

        override fun refresh(data: ArgueType?) {
            super.refresh(data)
            data.notNull { t ->
                textView.text = getString(t.str)
            }
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = ItemRecycleviewBinding.inflate(inflater, container, false)
        return binding
    }
}