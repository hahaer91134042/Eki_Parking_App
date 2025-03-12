package com.hill.devlibs.extension

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.hill.devlibs.impl.IAutoLoadImg
import com.hill.devlibs.impl.IImgUrl
import com.hill.devlibs.util.StringUtil
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

/**
 * Created by Hill on 2020/02/25
 */
fun IAutoLoadImg.loadUrl(url:String?,isFit:Boolean=false,@DrawableRes defaultImgRes:Int){
    var view=loadImg()
    if (!StringUtil.isEmptyString(url)) {
        val creator: RequestCreator = Picasso.with(view.context)
                .load(url)
                .placeholder(defaultImgRes)
        if (isFit) creator.fit() //填滿
        creator.into(view)
    } else Picasso.with(view.context).load(defaultImgRes).into(view)
}
fun IAutoLoadImg.loadUrl(url:String?,back:(RequestCreator)->RequestCreator){
    if (!StringUtil.isEmptyString(url))
        back(Picasso.with(loadImg().context).load(url)).into(loadImg())
}

fun IImgUrl.loadImgInto(view:ImageView, isFit:Boolean=false, @DrawableRes defaultImgRes:Int){
    var url=imgUrl()
    if (!StringUtil.isEmptyString(url)) {
        val creator = Picasso.with(view.context)
                .load(url)
                .placeholder(defaultImgRes)
        if (isFit) creator.fit() //填滿
        creator.into(view)
    } else Picasso.with(view.context).load(defaultImgRes).into(view)
}
fun IImgUrl.loadImgInto(view:ImageView,back:((RequestCreator)->RequestCreator)){
    var url=imgUrl()
    if (!StringUtil.isEmptyString(url)) {
        back(Picasso.with(view.context).load(url)).into(view)
    }
//    else Picasso.with(view.context).load(defaultImgRes).into(view)
}