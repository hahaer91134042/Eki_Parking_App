package com.eki.parking.Controller.activity.frag.ParkingSite.adaptor

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.eki.parking.Controller.activity.intent.SiteDetailIntent
import com.eki.parking.Controller.dialog.EkiMsgDialog
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.EditLocationResponse
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.View.libs.RoundImageView
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.extension.loadUrl
import com.eki.parking.extension.mapCityDetail
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.show
import com.hill.devlibs.extension.*
import com.hill.devlibs.impl.IFragManager

/**
 * Created by Hill on 2020/02/25
 */
class SiteListAdaptor(var context: Context?, private var dataList: ArrayList<ManagerLocation>, override val fragManager: FragmentManager) : BaseAdapter<SiteListAdaptor.SiteItem>(context),IFragManager {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiteItem =
            SiteItem(getItemView(R.layout.item_parking_site_info,parent)).also { it.init() }

    override fun onBindViewHolder(item: SiteItem, position: Int) {
        super.onBindViewHolder(item, position)
        item.refresh(dataList[position])
    }

    override fun getItemCount(): Int =dataList.size

    inner class SiteItem(itemView: View) : ItemLayout<ManagerLocation>(itemView) {

        private var siteImg=itemView.findViewById<RoundImageView>(R.id.siteImg)
        private var titleView=itemView.findViewById<TextView>(R.id.titleView)
        private var priceView=itemView.findViewById<TextView>(R.id.priceView)
        private var addressText=itemView.findViewById<TextView>(R.id.addressText)
        private var serial=itemView.findViewById<TextView>(R.id.serialText)
//        private var switcher=itemView.findViewById<Switch>(R.id.switcher)

        private var editBtn=itemView.findViewById<ImageView>(R.id.editBtn)
        private var deleteBtn=itemView.findViewById<ImageView>(R.id.deletBtn)
        private val set = Set()

        override fun init() {

//           setAutoSize()

            editBtn.setOnClickListener {
                //Log.d("Location edit")
                context.startActivityAnim(SiteDetailIntent(context,itemData))
            }
            deleteBtn.setOnClickListener {
                //Log.d("Location delete")
                itemData.notNull { loc->
                    EkiMsgDialog().also {

                        it.msg=getString(R.string.Are_you_sure_you_want_to_delete_this_parking_space)
                        it.determinClick={
                            deleteSite(loc)
                        }

                    }.show(fragManager)

                }

            }

        }

//        private fun setAutoSize(){
//
//            val array = arrayOf(titleView, priceView, addressText, serial)
//            array.forEach {
//                setTextAutoSize(it)
//            }
//
//        }

        private fun deleteSite(loc: ManagerLocation) {

            set.deleteSite(context, dataList, this@SiteListAdaptor, loc )

        }

        override fun refresh(data: ManagerLocation?) {
            super.refresh(data)

            set.refresh(context, data, siteImg, titleView, priceView, addressText, serial, deleteBtn)

        }
    }

    private class Set{
        fun deleteSite(context: Context?, dataList:ArrayList<ManagerLocation>, siteListAdaptor: SiteListAdaptor, loc: ManagerLocation){

            EkiRequest<SendIdBody>().also {
                it.body= SendIdBody(EkiApi.ManagerDeleteLocation).apply {
                    id.add(loc.Id)
                }
            }.sendRequest(context,showProgress = false,listener = object :OnResponseListener<EditLocationResponse>{
                override fun onReTry() {

                }

                override fun onFail(errorMsg: String, code: String) {
                    context?.getString(R.string.Parking_space_deletion_failed)?.let { context.showShortToast(it) }
                }

                override fun onTaskPostExecute(result: EditLocationResponse) {
                    context?.getString(R.string.The_parking_space_has_been_deleted)?.let { context.showToast(it) }
                    dataList.remove(dataList.first { it.Id==loc.Id })

                    siteListAdaptor.notifyDataSetChanged()
                }
            },showErrorDialog = false)
        }

        fun refresh(context: Context?, data: ManagerLocation?, siteImg: RoundImageView, titleView: TextView, priceView: TextView, addressText: TextView, serial: TextView, deleteBtn: ImageView){

            data.notNull {

            siteImg.loadUrl(it.imgUrl())
            titleView.text=it.Info?.Content
            priceView.text=context?.getString(R.string.billing_method_30min)?.messageFormat(it.Config?.Price?:0)
            addressText.text=it.Address?.mapCityDetail()
            serial.text=it.Info?.SerialNumber

//                switcher.isChecked=it.Config?.beEnable?:false

            setDeleteBtnVisible(it.Deleteable, deleteBtn)

            }
        }

        fun setDeleteBtnVisible(b:Boolean, deleteBtn: ImageView){
            deleteBtn.visibility=when(b){
                true->View.VISIBLE
                false->View.INVISIBLE
            }
        }
    }
}