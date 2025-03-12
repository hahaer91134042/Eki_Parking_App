package com.eki.parking.Controller.dialog.child

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IDialogFeatureSet
import com.eki.parking.Model.sql.BankCode
import com.eki.parking.R
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.databinding.DialogBankSelectBinding
import com.eki.parking.extension.*
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/04/20
 */
class BankCodeSelectFrag : DialogChildFrag<BankCodeSelectFrag>(),
    IDialogFeatureSet<BankCodeSelectFrag>, IFragViewBinding {

    private lateinit var binding: DialogBankSelectBinding
    private var bankList = sqlDataList<BankCode>()

    var onBankSelect: (BankCode) -> Unit = {}

    override fun initFragView() {
        binding.recycleView.useSimpleDivider()
        binding.recycleView.adapter = bankAdaptor(bankList)
        binding.codeInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var text = s.toString()
                binding.recycleView.adapter = when (text.isEmpty()) {
                    true -> bankAdaptor(bankList)
                    else -> bankAdaptor(bankList.filter { it.name.contains(text, true) })
                }
            }
        })

    }

    private fun bankAdaptor(list: List<BankCode>): BankAdaptor =
        BankAdaptor(list).also {
            it.onBankSelect = { bank ->
                onBankSelect(bank)
                dissmissDialog()
            }
        }

    private inner class BankAdaptor(private val list: List<BankCode>) :
        BaseAdapter<BankItem>(context) {
        var onBankSelect: (BankCode) -> Unit = {}

        init {
            setItemListClickListener {
                onBankSelect(list[it])
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankItem =
            BankItem(getItemView(R.layout.item_group_type_3, parent)).also { it.init() }

        override fun onBindViewHolder(item: BankItem, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(list[position])
        }

        override fun getItemCount(): Int = list.size
    }

    private class BankItem(itemView: View) : ItemLayout<BankCode>(itemView) {
        var header = itemView.findViewById<TextView>(R.id.headerText)
        var main = itemView.findViewById<TextView>(R.id.mainText)
        var parent = itemView.findViewById<LinearLayout>(R.id.parentView)

        override fun init() {
            parent.setPadding(dpToPx(12f), 0, 0, 0)
            header.setTextColor(getColor(R.color.text_color_1))
            header.textSize = dimen(R.dimen.text_size_6)
            main.setPadding(dpToPx(10f), 0, 0, 0)
            parent.setOnClickListener {
                itemClick()
            }
        }

        override fun refresh(data: BankCode?) {
            super.refresh(data)
            data.notNull { bank ->
                header.text = bank.code
                main.text = bank.name
            }
        }
    }

    override val frag: BankCodeSelectFrag
        get() = this
    override val title: String
        get() = ""
    override val titleSet: IDialogFeatureSet.ITitleBarSet?
        get() = TitleSet()
    override val contentSet: IDialogFeatureSet.IContentSet?
        get() = null
    override val btnFrameSet: IDialogFeatureSet.IBtnFrameSet
        get() = SingleBtnFrame()

    override fun onDismissCheck(): Boolean = true

    private class TitleSet : IDialogFeatureSet.ITitleBarSet() {
        override val visible: Boolean
            get() = false
        override val backgroundRes: Int
            get() = 0
        override val titleTextColor: Int
            get() = 0
        override val titleTextSize: Float
            get() = 0f
    }

    private class SingleBtnFrame : IDialogFeatureSet.IBtnFrameSet() {
        override val useDefault: Boolean
            get() = false
        override val btnContent: IDialogFeatureSet.IClickBtnFrame?
            get() = object : IDialogFeatureSet.IClickBtnFrame() {
                override val viewRes: Int
                    get() = R.layout.item_single_btn_frame

                override fun setUpView(v: View) {
                    var determinBtn = v.findViewById<Button>(R.id.determinBtn)
                    determinBtn.text = string(R.string.Cancel)
                    determinBtn.setTextColor(color(R.color.text_color_1))
                    determinBtn.setOnClickListener {
                        dismissClick?.invoke()
                    }
                }
            }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = DialogBankSelectBinding.inflate(inflater, container, false)
        return binding
    }
}