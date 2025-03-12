package com.eki.parking.View.popup

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.TextView
import com.eki.parking.R
import com.eki.parking.extension.dpToPx
import com.eki.parking.extension.screenWidth
import com.eki.parking.Controller.listener.OnSelectListener
import com.eki.parking.Model.sql.EkiLocation
import com.eki.parking.View.abs.EkiPopupWindow
import com.eki.parking.View.recycleview.BaseRecycleView
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.View.widget.AutoLoadImgView
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.tools.Log
import com.hill.devlibs.util.SimpleAnimationUtils

/**
 * Created by Hill on 28,10,2019
 */
class LocationSearchPopup(context: Context?) : EkiPopupWindow(context), ISetData<ArrayList<EkiLocation>> {

    private var locList=ArrayList<EkiLocation>()
    var callback:OnSelectListener<ArrayList<EkiLocation>>?=null

    override fun setUpView() {
        val recycleView=contentView.findViewById<BaseRecycleView>(R.id.recycleView)
//        Log.i("$TAG recycleView->$recycleView")
        recycleView.useHorizonView()
        recycleView.adapter=ResultAdapter(context).apply {
            setItemListClickListener {
//                Log.d("Loc click ->${it}")
//                locList[it].printValue()
                callback?.onSelect(arrayListOf(locList[it]))
            }
        }

    }


    override fun setData(data: ArrayList<EkiLocation>?) {
        Log.w("$TAG data ->$data  size->${data?.size}")
        data.notNull { locList.addAll(it) }
//        Log.d("locList->$locList")
    }


    override fun blurBackground(): Boolean =false
    override fun popGravity(): Int =Gravity.BOTTOM
    override fun closeAnim(): Animation? =SimpleAnimationUtils.bottomDown()
    override fun showAnim(): Animation? =SimpleAnimationUtils.bottomPopUp()
    override fun layoutRes(): Int =R.layout.popup_location_search


    private inner class ResultAdapter(context: Context?) : BaseAdapter<SearchItem>(context) {

        override fun getItemCount(): Int =locList.size


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItem =
                SearchItem(getItemView(R.layout.item_location_search_result,parent)).also {
                    it.init(screenWidth()*3/10)
                }

        override fun onBindViewHolder(item: SearchItem, position: Int) {
            super.onBindViewHolder(item, position)
            item.refresh(locList[position])
        }
    }

    private class SearchItem(itemView: View) : ItemLayout<EkiLocation>(itemView) {
        val parent:View by lazy { itemView.findViewById<View>(R.id.parentView) }
        val img:AutoLoadImgView by lazy { itemView.findViewById<AutoLoadImgView>(R.id.imgView) }
        val addressTex:TextView by lazy { itemView.findViewById<TextView>(R.id.addressText) }
        val chargeTypeTex:TextView by lazy { itemView.findViewById<TextView>(R.id.chargeTypeText) }
        val socketTypeTex:TextView by lazy { itemView.findViewById<TextView>(R.id.socketTypeText) }
        val priceTex:TextView by lazy { itemView.findViewById<TextView>(R.id.priceTextView) }

        override fun init(lenght: Int) {
            super.init(lenght)

            parent.setOnClickListener {
                itemClick()
            }
        }

        override fun refresh(data: EkiLocation?) {
            super.refresh(data)
            data.notNull {loc->
                img.loadUrl(loc.imgUrl(), dpToPx(185f), dpToPx(130f))
                addressTex.text="${loc.Address?.City}${loc.Address?.Detail}"
                chargeTypeTex.text=getString(loc.Socket?.first()?.currentEnum?.str!!)
                socketTypeTex.text=loc.Socket?.first()?.chargeSocket.toString()
                priceTex.text=loc.Config?.billingMethod?.formatePrice(loc.Config?.Price!!)
            }
        }
    }
}