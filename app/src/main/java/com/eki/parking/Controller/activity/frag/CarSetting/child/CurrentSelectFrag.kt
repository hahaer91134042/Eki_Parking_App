package com.eki.parking.Controller.activity.frag.CarSetting.child

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eki.parking.Controller.dialog.child.SingleSelectFrag
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.R
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.extension.dpToPx
import com.eki.parking.extension.string
import com.hill.devlibs.extension.notNull

/**
 * Created by Hill on 2020/01/02
 */
class CurrentSelectFrag: SingleSelectFrag<CurrentSelectFrag.CurrentAdaptor>() {

    var onCurrentSelect:((CurrentEnum,Int)->Unit)?=null

    private var currentList=ArrayList<CurrentEnum>().apply {
        add(CurrentEnum.NONE)
        add(CurrentEnum.AC)
        add(CurrentEnum.DC)
    }

    override fun setOptionAdaptor(): CurrentAdaptor =
            CurrentAdaptor(context).apply{
                setItemListClickListener {
                    val current=currentList[it]
                    val res= when(current){
                        CurrentEnum.AC->R.string.Electric_Car_AC
                        CurrentEnum.DC->R.string.Electric_Car_DC
                        CurrentEnum.NONE->R.string.General
                    }
                    onCurrentSelect?.invoke(current,res)
                    dissmissDialog()
                }
            }

    override val title: String
        get() = string(R.string.SteamLocomotive_Type)
    override fun onDismissCheck(): Boolean =true

    inner class CurrentAdaptor(context: Context?) : BaseAdapter<CurrentItem>(context) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentItem =
                CurrentItem(getItemView(R.layout.item_group_type_8,parent))
                        .also { it.init(dpToPx(65f)) }

        override fun onBindViewHolder(item: CurrentItem, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(currentList[position])
        }
        override fun getItemCount(): Int =currentList.size
    }

    class CurrentItem(itemView: View) : ItemLayout<CurrentEnum>(itemView) {
        private var mainText:TextView=itemView.findViewById(R.id.mainText)
        override fun init(lenght: Int) {
            super.init(lenght)
            mainText.gravity=Gravity.CENTER
            mainText.setOnClickListener { itemClick() }
        }

        override fun refresh(data: CurrentEnum?) {
            super.refresh(data)
            data.notNull {current->
                when(current){
                    CurrentEnum.AC->mainText.text=getString(R.string.Electric_Car_AC)
                    CurrentEnum.DC->mainText.text=getString(R.string.Electric_Car_DC)
                    CurrentEnum.NONE->mainText.text=getString(R.string.General)
                }
            }
        }
    }

}