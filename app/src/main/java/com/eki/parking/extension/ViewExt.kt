package com.eki.parking.extension

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.eki.parking.App
import com.eki.parking.R
import com.hill.devlibs.extension.loadImgInto
import com.hill.devlibs.impl.IActivityIntent
import com.hill.devlibs.impl.IActivityIntent.ForResultBack
import com.hill.devlibs.impl.IImgUrl

/**
 * Created by Hill on 2020/01/30
 */

inline fun View.startActivityAnim(intent: Intent, isAnim: Boolean = true){
    context.startActivity(intent)
    //有強轉 要注意
    if (isAnim)
        if (context is Activity){
            (context as Activity).overridePendingTransition(R.anim.from_black_to_white, R.anim.from_white_to_black)
        }
}

inline fun View.startActivityAnim(impl: IActivityIntent, isAnim: Boolean = true){
    if (impl is ForResultBack) {
        this.startActivityForResult(impl.intent, (impl as ForResultBack).requestCode)
    } else {
        this.startActivityAnim(impl.intent, isAnim)
    }
}

inline fun View.startActivityForResult(intent: Intent, requestCode: Int) {
    App.getInstance().topStackActivity.startActivityForResult(intent, requestCode)
}

inline fun View.startActivityAnim(clazz: Class<*>, isAnim: Boolean = true){
    this.startActivityAnim(Intent().apply { setClass(context, clazz) }, isAnim)
}

fun IImgUrl.loadImgInto(view: ImageView, isFit: Boolean = false){
    this.loadImgInto(view, isFit, R.drawable.none_img)
}