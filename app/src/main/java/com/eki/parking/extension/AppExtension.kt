@file:JvmName("AppUtils")

package com.eki.parking.extension

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.*
import com.eki.parking.App
import com.eki.parking.AppConfig
import com.eki.parking.Controller.asynctask.task.server.RequestTask
import com.eki.parking.Controller.dialog.EkiProgressDialog
import com.eki.parking.Controller.dialog.MsgDialog
import com.eki.parking.Controller.impl.IAppContext
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.util.ScreenUtils
import com.eki.parking.Model.DTO.AddressInfo
import com.eki.parking.Model.EnumClass.CurrencyUnit
import com.eki.parking.Model.EnumClass.impl.IEnumValue
import com.eki.parking.Model.ResponseVO
import com.eki.parking.Model.impl.ICurrencyCost
import com.eki.parking.Model.impl.IEkiSecretHeader
import com.eki.parking.Model.impl.IRequestApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.sql.*
import com.eki.parking.R
import com.google.android.gms.maps.model.LatLng
import com.hill.devlibs.EnumClass.ProgressMode
import com.hill.devlibs.annotation.parse.ApiParser
import com.hill.devlibs.extension.halfUp
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.ICryptoSet
import com.hill.devlibs.listener.OnMsgDialogBtnListener
import com.hill.devlibs.tools.MobileInfo
import java.io.InputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Hill on 2019/6/21
 */
inline fun LatLng.equal(other: LatLng): Boolean =
    latitude == other.latitude && longitude == other.longitude

inline fun CountryCode.fullNameByLan(): String = when (MobileInfo.language) {
    "zh" -> fullCn
    else -> fullEn
}

inline fun AddressInfo.mapCityDetail(): String =
    "$City$Detail"

inline fun ICurrencyCost.currencyCost(): Double = costValue.toCurrency(currencyUnit)

inline fun Context.showProgress(mode: ProgressMode = ProgressMode.PROCESSING_MODE): EkiProgressDialog =
    EkiProgressDialog(this, mode).apply { show() }

inline fun Double.toCurrency(unit: Int): Double =
    when (parseEnum<CurrencyUnit>(unit)) {
        CurrencyUnit.USD -> this
        CurrencyUnit.TWD,
        CurrencyUnit.RMB,
        CurrencyUnit.JPY
        -> this.halfUp(0)//四捨五入
    }
inline fun <reified T : Enum<T>> String.toEnum():T=java.lang.Enum.valueOf(T::class.java,this)
inline fun <reified T : Enum<T>> Int.toEnum(): T = parseEnum(this)
inline fun <reified T : Enum<T>> parseEnum(value: Int): T {
    val enumList = enumValues<T>()
    val defaultEnumList = enumList.filterIsInstance(IEnumValue::class.java).filter { it.default }
    if (defaultEnumList.size != 1)
        throw IllegalArgumentException("Enum default option must be only one!")

    var default: T? = null
    enumList.forEach {
        if (it is IEnumValue) {
            if (it.default)
                default = it

            when (it.value) {
                value -> return it
            }
        } else {
            throw IllegalArgumentException("Enum must implement ${IEnumValue::class.java.simpleName}")
        }
    }
    default.notNull { return it }
    throw IllegalArgumentException("Enum must has default option")
}

inline fun screenWidth(): Int = ScreenUtils.getScreenWidth()
inline fun screenHeight(): Int = ScreenUtils.getScreenHeight()
inline fun showMsgDialog(context: Context, msg: String, listener: OnMsgDialogBtnListener? = null) {
    MsgDialog(context)
        .setShowMsg(msg)
        .setPositiveBtn(context.getString(R.string.Determine))
        .setBtnClickListener(listener)
        .show()
}

inline fun showMsgDialog(
    context: Context,
    set: MsgDialog.MsgDialogSet,
    listener: OnMsgDialogBtnListener? = null
) {
    MsgDialog(context)
        .setShowTitle(set.title)
        .setShowMsg(set.msg)
        .setNegativeBtn(set.nBtnTex)
        .setPositiveBtn(set.pBtnTex)
        .setBtnClickListener(listener)
        .show()
}

inline fun stringArray(@ArrayRes res: Int): Array<String> = App.getInstance().resources.getStringArray(res)
inline fun color(@ColorRes res: Int): Int = App.getInstance().resources.getColor(res)
inline fun colorStateList(@ColorRes res: Int): ColorStateList =
    App.getInstance().resources.getColorStateList(res, null)

inline fun dpToPx(dp: Float): Int = ScreenUtils.dpToPx(dp)
inline val Float.toPx: Int get() = ScreenUtils.dpToPx(this)
inline val Int.toDp: Float get() = ScreenUtils.pxToDp(this)
inline fun pxToDp(px: Int): Float = ScreenUtils.pxToDp(px)

inline fun dimen(@DimenRes res: Int): Float = App.getInstance().resources.getDimension(res)
inline fun drawable(@DrawableRes res: Int): Drawable? = App.getInstance().getDrawable(res)
inline fun string(@StringRes res: Int): String = App.getInstance().getString(res)
inline fun raw(@RawRes res: Int): InputStream = App.getInstance().resources.openRawResource(res)

inline fun ArrayList<SearchInfo>.addInfo(info: SearchInfo): Boolean {
    if (this.none { it.Content == info.Content }) {
        if (this.size >= AppConfig.maxSearchInfoRecord) {
            val min = this.minByOrNull { it.craetTime.toStamp() }
            this.remove(min)
        }
        this.add(info)
        return true
    }
    return false
}

inline fun <reified R : ResponseVO> EkiRequest<*>.sendRequest(
    context: Context?,
    showProgress: Boolean = false,
    listener: OnResponseListener<R>? = null,
    showErrorDialog: Boolean = true
) {

    if (body is IRequestApi) {
        val config = ApiParser.parse((body as IRequestApi).requestApi())
        if (config.isAuth)
            if (!sqlHasData<EkiMember>()) {
                //沒有登入先擋下
                //之後要做登入跳窗

                return
            }
    } else {
        if (apiConfig.isAuth)
            if (!sqlHasData<EkiMember>()) {
                //沒有登入先擋下
                //之後要做登入跳窗

                return
            }
    }

    if (body is IEkiSecretHeader)
        secretHeader()

    context.notNull {
        RequestTask<R>(it, this, showProgress, showErrorDialog)
            .setExecuteListener(listener)
            .start()
    }
}

inline fun <reified T> ICryptoSet.decodeToObj(): T? {
    return encoder().decode(T::class.java, cipher(), key())
}

inline val IAppContext.app: App get() = App.getInstance()

inline fun List<EkiOrder>.hasCheckoutOrder():Boolean{
    return !none { it.isOverCheckOut() }
}

fun compareBiggerOrSameDate(strSmall: String, strBig: String, pattern: String): Boolean {

    val sdf = SimpleDateFormat(pattern,Locale.getDefault())
    val smallDate: Date = sdf.parse(strSmall) ?: Calendar.getInstance().time
    val bigDate: Date = sdf.parse(strBig) ?: Calendar.getInstance().time

    return bigDate.after(smallDate) || (bigDate == smallDate)
}

inline fun addComma(str: String): String? { // 每三位數加入逗號
    val decimalFormat = DecimalFormat(",###")
    return if (str.toDouble() > 100.0) {
        decimalFormat.format(str.toDouble())
    } else {
        str
    }

}

inline fun resetFilter() {
    val sqlLocConfig = sqlDataList<LoadLocationConfig>().first()

    sqlSave(LoadLocationConfig().apply {
        Range=AppConfig.MaxSearchRange.range
        Address=sqlLocConfig.Address
    })
}



