package com.eki.parking.View.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import com.eki.parking.R
import com.eki.parking.Controller.listener.OnPhotoEventListener
import com.eki.parking.View.abs.FrameCustomView
import com.hill.devlibs.util.StringUtil
import com.hill.devlibs.widget.libs.CircleImageView
import com.squareup.picasso.Picasso

/**
 * Created by Hill on 2019/5/6
 */
class UserIconCameraView(context: Context?, attrs: AttributeSet?) : FrameCustomView(context, attrs) {

    var listener:OnPhotoEventListener?=null
    var icon:CircleImageView = itemView.findViewById(R.id.userIconImg)
    private var defaultImg=R.drawable.none_img

    init {
        icon.setOnClickListener{
            listener?.onPhotoEvent()
        }

        var cameraBtn=itemView.findViewById<ImageView>(R.id.cameraBtn)
        cameraBtn.setOnClickListener {
            listener?.onCameraEvent()
        }

    }

    override fun setUpInflatView(typedArray: TypedArray) {
        var imgRes=typedArray.getResourceId(R.styleable.UserIconCameraView_defaultImg,0)
        if (imgRes>0){
            defaultImg=imgRes
            icon.setImageResource(defaultImg)
        }
    }

    fun loadUrl(url:String){
        if (!StringUtil.isEmptyString(url))
            Picasso.with(context)
                    .load(url)
                    .placeholder(defaultImg)
                    .into(icon)
        else
            Picasso.with(context).load(defaultImg).into(icon)
    }


    override fun setStyleableRes(): IntArray? =R.styleable.UserIconCameraView
    override fun setInflatView(): Int = R.layout.item_user_icon_camera_view

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}