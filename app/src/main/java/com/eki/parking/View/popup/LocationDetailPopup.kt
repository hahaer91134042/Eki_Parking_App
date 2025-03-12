package com.eki.parking.View.popup

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.chahinem.pageindicator.PageIndicator
import com.eki.parking.R
import com.eki.parking.extension.string
import com.eki.parking.Model.sql.EkiLocation
import com.eki.parking.View.pager.view.LocationDetailPage
import com.eki.parking.View.abs.EkiPopupWindow
import com.hill.devlibs.extension.*
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.tools.Log
import com.hill.devlibs.util.SimpleAnimationUtils
import com.hill.viewpagerlib.adapter.RecyclingPagerAdapter
import com.hill.viewpagerlib.transformer.ZoomOutSlideTransformer

/**
 * Created by Hill on 29,10,2019
 */
class LocationDetailPopup(context: Context?) : EkiPopupWindow(context),
                                               ISetData<ArrayList<EkiLocation>> {


    private var locList=ArrayList<EkiLocation>()
    private var cancelBtn:ImageView=contentView.findViewById(R.id.cancelBtn)
    private var viewPager:ViewPager=contentView.findViewById(R.id.viewPager)
    private var pageIndicatorView:PageIndicator=contentView.findViewById(R.id.pageIndicatorView)
    private var totalNumText:TextView=contentView.findViewById(R.id.totalNumText)

//    private var serial:TextView=contentView.findViewById(R.id.serialText)
//    private var price:TextView=contentView.findViewById(R.id.priceTextView)
//    private var remark:TextView=contentView.findViewById(R.id.remarkText)
//    private var reservaBtn:StateButton=contentView.findViewById(R.id.reservaBtn)

    var onReservaLocation:((EkiLocation)->Unit)?=null
    var select=0

    private var reservaClick:((EkiLocation)->Unit)={loc->
        onReservaLocation?.invoke(loc)
        dismiss()
    }

    init {
        cancelBtn.setOnClickListener {
            dismiss()
        }

//        reservaBtn.setOnClickListener {
//            Log.d("$TAG reservation select click->$select")
//            onReservaLocation?.invoke(locList[select])
//            dismiss()
//        }


    }


    override fun setData(data: ArrayList<EkiLocation>?) {
//        Log.w("$TAG setData->$data")
        data.notNull { locList.addAll(it) }
    }

    override fun popGravity(): Int =Gravity.BOTTOM

    override fun blurBackground(): Boolean =false
    override fun backEnable(): Boolean =false
    override fun outSideDismiss(): Boolean =false

    override fun setUpView() {
        totalNumText.text= string(R.string.There_are_parking_spaces_at_this_address).messageFormat(locList.size)

        viewPager.setPageTransformer(true,ZoomOutSlideTransformer())
//        viewPager.offscreenPageLimit=locList.size
        viewPager.adapter=DetailAdaptor()
        if (locList.size>1)
            pageIndicatorView.attachTo(viewPager)
        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                Log.d("OnPageSelect posi->$position")
                select=position
                setLocInfoView(select)
            }
        })
        locList.isNotEmpty().isTrue {
            setLocInfoView(select)
        }
    }

    private fun setLocInfoView(position: Int) {
        var loc=locList[position]

//        serial.text= string(R.string.serial_number_str).messageFormat(loc.Info?.SerialNumber?:"")
//        price.text= loc.Config?.billingMethod?.formatePrice(loc.Config?.Price!!)
//        remark.text=loc.Info?.Content
    }

    override fun layoutRes(): Int =R.layout.popup_location_detail

    override fun showAnim(): Animation? =SimpleAnimationUtils.bottomPopUp()

    override fun closeAnim(): Animation? =SimpleAnimationUtils.bottomDown()

    private inner class DetailAdaptor: RecyclingPagerAdapter() {

        override fun getView(position: Int, convertView: View?, container: ViewGroup?): View {
//            Log.w("Adaptor getView 1 view->$convertView")
            var loc=locList[position]
            var view= convertView.whenNullBack{LocationDetailPage(context)}
//            Log.i("Adaptor getView 2 view->$view")
            view.instanceIs<LocationDetailPage> {page->
                page.setLoc(loc)
                page.onReservaClick=reservaClick
            }

            return view
        }

        override fun getCount(): Int =locList.size

    }
}