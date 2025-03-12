package com.eki.parking.View.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.eki.parking.Model.DTO.LocationImg
import com.eki.parking.R
import com.eki.parking.View.abs.ConstrainCustomView
import com.hill.devlibs.extension.whenNullBack
import com.hill.devlibs.tools.Log
import com.hill.devlibs.widget.AutoSizeTextView
import com.hill.viewpagerlib.adapter.RecyclingPagerAdapter

/**
 * Created by Hill on 2020/09/28
 */
class LocationImgSelectPager(context: Context?, attrs: AttributeSet?) : ConstrainCustomView(context, attrs) {

    companion object{
        const val pageNum=3
    }

    private var imgList=ArrayList<ImgItem>().apply {
        for (i in 0 until pageNum){
            add(ImgItem(AutoLoadImgView(context)))
        }
    }

    var selectPosition=0
    var onImgSelect:((LocationImg)->Unit)?=null
    private val pageCount: AutoSizeTextView = findViewById(R.id.pageCount)
    private val viewPager: ViewPager = findViewById(R.id.viewPager)

    init {
        pageCount.text="1/${imgList.size}"

        viewPager.adapter=ImgAdaptor()

        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                Log.w("page select->$position")
                selectPosition=position
                onImgSelect?.invoke(imgList[position].data)
                pageCount.text="${position+1}/${imgList.size}"
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    data class ImgItem(var view:AutoLoadImgView){
        var data=LocationImg()
            set(value) {
                view.loadUrl(value.Url,true)
                field=value
            }

        init {
            view.loadUrl("",true)
        }
    }

    fun initLocImg(img: ArrayList<LocationImg>) {
        img.sortBy { it.Sort }

        var size=when{
            imgList.size>img.size->img.size
            else->imgList.size
        }

        for (i in 0 until size){
            imgList[i].data=img[i]
        }
    }

    fun getSelectImg():ImgItem = imgList[selectPosition]

    private inner class ImgAdaptor: RecyclingPagerAdapter() {
        override fun getCount(): Int =imgList.size

        override fun getView(position: Int, convertView: View?, container: ViewGroup?): View {
            var item=imgList[position]
            var view=convertView.whenNullBack { item.view }

            return view
        }

    }

    override fun setInflatView(): Int = R.layout.page_location_img_select

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null

}