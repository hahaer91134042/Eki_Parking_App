package com.eki.parking.extension

import com.eki.parking.Model.EnumClass.EkiUri

/**
 * Created by Hill on 2020/05/27
 */

fun EkiUri.isAppUri():Boolean=this.host==EkiUri.Host.EkiWeb&&this.scheme==EkiUri.Scheme.Eki
fun EkiUri.isCheckOut():Boolean=this.isAppUri()&&this.path==EkiUri.Path.Checkout