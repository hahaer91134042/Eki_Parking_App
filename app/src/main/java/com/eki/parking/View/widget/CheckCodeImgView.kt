package com.eki.parking.View.widget

import android.content.ContentResolver
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.eki.parking.R
import com.hill.devlibs.util.MathUtil
import com.eki.parking.View.abs.LinearCustomView
import kotlin.collections.ArrayList

/**
 * Created by Hill on 2018/11/19.
 * 確認碼
 */
class CheckCodeImgView(context: Context,attributeSet: AttributeSet) :LinearCustomView(context,attributeSet){

    private val key="img_"
    var codeMax=0
    var codeMin=0
    var showNum=0
    private var codeList=ArrayList<Int>()

    init {
        parseAttr(context.obtainStyledAttributes(attributeSet, R.styleable.CheckCodeImgView))
        creatView()
    }

    private fun creatView() {
        codeList.clear()

        if (showNum<1)
            return

        for (i in 1..showNum)
            codeList.add(MathUtil.ranInt(codeMin,codeMax))

        for (value in codeList){
            var imgView=ImageView(context).apply { layoutParams=getImgParams() }
            var picture=getPicture(value)
            imgView.setImageBitmap(picture)
            addView(imgView)
        }
    }


    private fun getImgParams(): LinearLayout.LayoutParams? {
        return LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT).apply {
            weight=1.0f
        }
    }

    private fun parseAttr(typedArr: TypedArray?) {
        codeMax= typedArr?.getInteger(R.styleable.CheckCodeImgView_code_range_max,codeMax)!!
        codeMin=typedArr?.getInteger(R.styleable.CheckCodeImgView_code_range_min,codeMin)!!
        showNum=typedArr?.getInteger(R.styleable.CheckCodeImgView_code_show_number,showNum)!!
        typedArr?.recycle()
    }


    var code=""
    get() {
        var buffer=StringBuffer()
        for (v in codeList){
            buffer.append(v.toString())
        }
        return buffer.toString()
    }


    fun reload(){
        removeAllViews()
        creatView()
    }

    private fun getPicture(value: Int): Bitmap {
        var imgName=key+value
        var uri=Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/this/$imgName")
        return BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
    }


    override fun setInflatView(): Int {
        return 0
    }

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? {
        return null
    }
}