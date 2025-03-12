package com.eki.parking.extension

import com.eki.parking.R
import com.hill.devlibs.extension.loadUrl
import com.hill.devlibs.impl.IAutoLoadImg


/**
 * Created by Hill on 2020/02/25
 */
fun IAutoLoadImg.loadUrl(url:String?, isFit:Boolean=false) =
        loadUrl(url,isFit, R.drawable.none_img)