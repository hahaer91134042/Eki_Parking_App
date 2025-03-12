package com.eki.parking.Controller.activity.frag.CarSetting.child

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eki.parking.Controller.dialog.child.SingleSelectFrag
import com.eki.parking.Model.sql.CarBrand
import com.eki.parking.R
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.extension.dpToPx
import com.eki.parking.extension.screenHeight
import com.eki.parking.extension.sqlDataList
import com.eki.parking.extension.string
import com.hill.devlibs.extension.notNull

/**
 * Created by Hill on 2019/12/31
 */
class CarBrandSelectFrag:SingleSelectFrag<CarBrandSelectFrag.CarBrandAdaptor>(){

    var onBrandSelect:((String)->Unit)?=null

    private val brandList= sqlDataList<CarBrand>()

    override val dialogHeight: Int
        get() = screenHeight() *2/3
    override val title: String
        get() = string(R.string.Car_brand)
    override fun onDismissCheck(): Boolean =true

    override fun setOptionAdaptor(): CarBrandAdaptor =CarBrandAdaptor(context)
            .apply {
                setItemListClickListener {
                    onBrandSelect?.invoke(brandList[it].name)
                    dissmissDialog()
                }
            }

    inner class CarBrandAdaptor(context: Context?) : BaseAdapter<CarBrandItem>(context) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarBrandItem =
                CarBrandItem(getItemView(R.layout.item_group_type_8,parent))
                        .also { it.init(dpToPx(65f)) }

        override fun onBindViewHolder(item: CarBrandItem, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(brandList[position])
        }
        override fun getItemCount(): Int =brandList.size
    }

    class CarBrandItem(itemView: View) : ItemLayout<CarBrand>(itemView) {

        private val brandText:TextView =itemView.findViewById(R.id.mainText)

        override fun init(lenght: Int) {
            super.init(lenght)
            brandText.setPadding(dpToPx(20f),0,0,0)
            brandText.setOnClickListener { itemClick() }
        }

        override fun refresh(data: CarBrand?) {
            super.refresh(data)
            data.notNull { brandText.text=it.name }
        }
    }
}