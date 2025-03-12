package com.eki.parking.Controller.activity.frag.Login.child

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IDialogFeatureSet
import com.eki.parking.Model.sql.CountryCode
import com.eki.parking.R
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.databinding.ItemRecycleviewBinding
import com.eki.parking.extension.*
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.extension.toEmoji
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.tools.MobileInfo

/**
 * Created by Hill on 2019/12/12
 */
class CountrySelectFrag : DialogChildFrag<CountrySelectFrag>(),
    ISetData<ArrayList<CountryCode>>,
    IDialogFeatureSet<CountrySelectFrag>, IFragViewBinding {

    private lateinit var binding: ItemRecycleviewBinding

    companion object {
        fun creat(data: ArrayList<CountryCode>? = null): CountrySelectFrag = CountrySelectFrag()
            .also { frag ->
                if (data == null)
                    frag.setData(sqlDataList())
                else
                    frag.setData(data)
            }
    }

    private lateinit var codeList: ArrayList<CountryCode>
    var onSelectCode: ((String) -> Unit)? = null
    var onSelectCountry: (CountryCode) -> Unit = {}

    override fun setData(data: ArrayList<CountryCode>?) {
        data.notNull { codeList = it }
    }

    override fun initFragView() {

        binding.recycleView.useSimpleDivider()
        binding.recycleView.layoutParams.height = screenHeight() * 2 / 3
        binding.recycleView.adapter = CountryCodeAdaptor(context).apply {
            setItemListClickListener {
                dissmissDialog()
                onSelectCode?.invoke(codeList[it].code)
                onSelectCountry(codeList[it])
            }
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = ItemRecycleviewBinding.inflate(inflater, container, false)
        return binding
    }

    override val title: String
        get() = string(R.string.Telephone_country)
    override val titleSet: IDialogFeatureSet.ITitleBarSet?
        get() = object : IDialogFeatureSet.ITitleBarSet() {
            override val backgroundRes: Int
                get() = R.drawable.shape_eki_dialog_title_orange
            override val titleTextColor: Int
                get() = Color.WHITE
            override val titleTextSize: Float
                get() = dimen(R.dimen.text_size_6)
            override val closeBtnRes: Int
                get() = R.drawable.icon_close_white
        }
    override val btnFrameSet: IDialogFeatureSet.IBtnFrameSet
        get() = object : IDialogFeatureSet.IBtnFrameSet() {
            override val useDefault: Boolean
                get() = false
        }

    override fun onDismissCheck(): Boolean = true


    private inner class CountryCodeAdaptor(context: Context?) : BaseAdapter<CodeItem>(context) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodeItem =
            CodeItem(getItemView(R.layout.item_group_type_3, parent)).also { it.init(dpToPx(25f)) }

        override fun onBindViewHolder(item: CodeItem, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(codeList[position])
        }

        override fun getItemCount(): Int = codeList.size
    }

    private class CodeItem(itemView: View) : ItemLayout<CountryCode>(itemView) {
        private var parentView = itemView.findViewById<View>(R.id.parentView)
        private var header = itemView.findViewById<TextView>(R.id.headerText)
        private var main = itemView.findViewById<TextView>(R.id.mainText)
        override fun init(lenght: Int) {
            super.init(lenght)
            header.textSize = dimen(R.dimen.text_size_10)
            header.setPadding(dpToPx(20f), dpToPx(2f), dpToPx(7f), dpToPx(2f))
            parentView.setOnClickListener { itemClick() }
        }

        override fun refresh(data: CountryCode?) {
            super.refresh(data)
            data.notNull {

                header.text = it.short2.toEmoji()
                if (MobileInfo.language.equals("zh", ignoreCase = true)) {
                    main.text = it.fullCn
                } else {
                    main.text = it.fullEn
                }
            }
        }
    }

    override val frag: CountrySelectFrag
        get() = this
    override val contentSet: IDialogFeatureSet.IContentSet?
        get() = object : IDialogFeatureSet.IContentSet() {
            override val backgroundRes: Int
                get() = R.drawable.shape_eki_dialog_bottom
        }
}