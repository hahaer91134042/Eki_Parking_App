package com.eki.parking.View.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.eki.parking.Model.EnumClass.ChargeSocket
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.R
import com.eki.parking.View.abs.ExpansionLinearCustomView
import com.eki.parking.View.abs.LinearCustomView
import com.eki.parking.View.recycleview.BaseRecycleView
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.extension.dimen
import com.eki.parking.extension.drawable
import com.github.florent37.expansionpanel.ExpansionLayout
import com.github.florent37.expansionpanel.viewgroup.ExpansionsViewGroupLinearLayout
import com.hill.devlibs.extension.getDrawable
import com.hill.devlibs.extension.notNull


/**
 * Created by Hill on 14,10,2019
 */
class CarTypeSearchList(context: Context?, attrs: AttributeSet?) :
    ExpansionLinearCustomView(context, attrs),
    View.OnClickListener {

    private interface AllClick {
        fun removeCheck()
    }

    class SelectStats {
        var allSelect = true
        var normalSelect = false
        var acSelect = false
        var acSockets = ArrayList<ChargeSocket>()
        var dcSelect = false
        var dcSockets = ArrayList<ChargeSocket>()
        fun selectAll() {
            allSelect = true
            normalSelect = false
            acSelect = false
            dcSelect = false
        }

        fun hasOptionSelect(): Boolean {
            return allSelect || normalSelect || acSelect || dcSelect
        }
    }


    var selectStats = SelectStats()
    private var acOption: ExpanCarTypeOptionItem = ExpanCarTypeOptionItem(context)
    private var dcOption: ExpanCarTypeOptionItem = ExpanCarTypeOptionItem(context)
    private var parentView: ExpansionsViewGroupLinearLayout = itemView.findViewById(R.id.parentView)

    private val optionView_All: LinearLayout = findViewById(R.id.optionView_All)
    private val optionView_Normal: LinearLayout = findViewById(R.id.optionView_Normal)
    private val checkImg_All: ImageView = findViewById(R.id.checkImg_All)
    private val checkImg_Normal: ImageView = findViewById(R.id.checkImg_Normal)

    init {

        optionView_All.setOnClickListener(this)
        optionView_All.callOnClick()

        optionView_Normal.setOnClickListener(this)

        acOption.optionText.text = getString(R.string.Electric_Car_AC)
        acOption.initView(CurrentEnum.AC)
        acOption.onSelectSockets = { sockets: ArrayList<ChargeSocket> ->
            selectStats.acSelect = sockets.size > 0
            selectStats.acSockets = sockets
            removeSelectAll(selectStats.acSelect)
        }

        dcOption.optionText.text = getString(R.string.Electric_Car_DC)
        dcOption.initView(CurrentEnum.DC)
        dcOption.onSelectSockets = { sockets: ArrayList<ChargeSocket> ->
            selectStats.dcSelect = sockets.size > 0
            selectStats.dcSockets = sockets
            removeSelectAll(selectStats.dcSelect)
        }

        parentView.addView(acOption)
        parentView.addView(dcOption)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.optionView_All -> {
                checkImg_All.background = getDrawable(R.drawable.icon_check_circle)
                checkImg_Normal.background = null
                acOption.removeCheck()
                dcOption.removeCheck()
                selectStats.selectAll()
            }
            R.id.optionView_Normal -> {
                if (selectStats.normalSelect) {
                    checkImg_Normal.background = null
                    selectStats.normalSelect = false
                } else {
                    checkImg_Normal.background = getDrawable(R.drawable.icon_check_circle)
                    selectStats.normalSelect = true
                }
                removeSelectAll(selectStats.normalSelect)
            }
        }
    }

    private fun removeSelectAll(b: Boolean) {
        if (b) {
            checkImg_All.background = null
            selectStats.allSelect = false
        }
    }

    override fun setInflatView(): Int = R.layout.item_car_type_search

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null

    private class ExpanCarTypeOptionItem(context: Context?) : LinearCustomView(context),
        AllClick {

        private val titleTextView:TextView = findViewById(R.id.titleTextView)
        private val headerImg:ImageView = findViewById(R.id.headerImg)
        private val recycleView: BaseRecycleView = findViewById(R.id.recycleView)

        override fun removeCheck() {
            cancelCheckIcon()
            expansionOptionView.collapse(false)
            optionAdaptor?.removeCheck()
        }

        var optionText = titleTextView
        var parentView: View = itemView.findViewById(R.id.parentView)
        var expansionOptionView: ExpansionLayout = itemView.findViewById(R.id.expansionLayout)
        var optionAdaptor: SocketOptionAdaptor? = null
        var onExpand: ((headerImg: ImageView) -> Unit)? = null
        var onSelectSockets: ((ArrayList<ChargeSocket>) -> Unit)? = null

        init {
            expansionOptionView.addListener { expansionLayout, expanded ->
//                Log.d("Expand ->$expanded")
                if (expanded) {
                    onExpand?.invoke(headerImg)
                }
            }
        }

        fun initView(current: CurrentEnum) {
            recycleView.useSimpleDivider()
            optionAdaptor = SocketOptionAdaptor(context, current.sockets).apply {
                onSelectSocket = {
                    onSelectSockets?.invoke(it)
                }
            }
            recycleView.adapter = optionAdaptor
        }

        private fun cancelCheckIcon() {
            headerImg.background = null
        }

        private fun showCheckIcon() {
            headerImg.background = drawable(R.drawable.icon_check_circle)
        }

        override fun setInflatView(): Int = R.layout.item_expansion_recycler_cell

        override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null

    }

    private class SocketOptionAdaptor(context: Context, var optionList: ArrayList<ChargeSocket>) :
        BaseAdapter<SocketItem>(context), AllClick {

        var itemList = ArrayList<SocketItem>()

        init {
            setItemListClickListener {
                var selectSocket = ArrayList<ChargeSocket>()
                itemList.forEach {
                    if (it.select) {
                        selectSocket.add(it.getSocket())
                    }
                }
                onSelectSocket?.invoke(selectSocket)
            }

        }

        var onSelectSocket: ((ArrayList<ChargeSocket>) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocketItem {
            return SocketItem(getItemView(R.layout.item_group_type_2, parent)).also {
                it.init((dimen(R.dimen.option_item_height) * 8 / 10).toInt())
                itemList.add(it)
            }
        }

        override fun getItemCount(): Int = optionList.size

        override fun onBindViewHolder(item: SocketItem, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(optionList[position])
        }

        override fun removeCheck() {
            itemList.forEach {
                it.removeCheck()
            }
        }

    }

    private class SocketItem(itemView: View) : ItemLayout<ChargeSocket>(itemView),
        AllClick {

        val checkImg: ImageView by lazy {
            return@lazy itemView.findViewById<ImageView>(R.id.checkImg)
        }
        val mainText: TextView by lazy {
            return@lazy itemView.findViewById<TextView>(R.id.mainText)
        }

        var select = false

        var getSocket: () -> ChargeSocket = fun(): ChargeSocket {
            return itemData
        }

        override fun init(lenght: Int) {
            super.init(lenght)
            var parent = itemView.findViewById<View>(R.id.parentView)
            parent.layoutParams.height = lenght
            parent.setOnClickListener {
                select = !select
                setCheckImg()
                itemClick()//這邊要最後再做
            }
            setCheckImg()
        }

        private fun setCheckImg() {
            if (select)
                checkImg.background = drawable(R.drawable.icon_check_circle)
            else
                checkImg.background = null
        }

        override fun refresh(data: ChargeSocket?) {
            super.refresh(data)
            data.notNull { mainText.text = getString(it.socketName) }

        }

        override fun removeCheck() {
            select = false
            setCheckImg()
        }
    }

}