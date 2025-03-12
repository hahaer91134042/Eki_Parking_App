package com.eki.parking.View.widget

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.eki.parking.R
import com.eki.parking.View.abs.RelativeCustomView
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2019/11/21
 */
class CarNumberCamera(context: Context?, attrs: AttributeSet?) : RelativeCustomView(context, attrs) {

    var onStartCamera:(()->Unit)?=null

    private val startCameraView: RelativeLayout = findViewById(R.id.startCameraView)
    private val imgView: ImageView = findViewById(R.id.imgView)
    private val textView: TextView = findViewById(R.id.textView)

    init {
        startCameraView.setOnClickListener {
            onStartCamera?.invoke()
        }
        imgView.setOnClickListener {
            onStartCamera?.invoke()
        }
    }

    fun setText(str:String){
        textView.text=str
    }

    fun showPicture(src: Bitmap?) {
        Log.i("${this.javaClass.simpleName} bitmap->$src")
        src.notNull { bitmap->
            startCameraView.visibility= View.INVISIBLE
            imgView.visibility=View.VISIBLE
            imgView.setImageBitmap(bitmap)
        }
    }

    override fun setInflatView(): Int = R.layout.item_car_number_camera
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null

}