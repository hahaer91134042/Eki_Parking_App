package com.eki.parking.Model.EnumClass

import com.eki.parking.R
import com.hill.devlibs.annotation.ErrorCodeSet

/**
 * Created by Hill on 2018/10/25.
 */
enum class EkiErrorCode {

    @ErrorCodeSet(msgRes = R.string.E000)
    E000,//正常
    @ErrorCodeSet(msgRes = R.string.E001)
    E001,//輸入錯誤
    @ErrorCodeSet(msgRes = R.string.E002)
    E002,//超出時間
    @ErrorCodeSet(msgRes = R.string.E003)
    E003,//無資料
    @ErrorCodeSet(msgRes = R.string.E004)
    E004,//未知錯誤
    @ErrorCodeSet(msgRes = R.string.E005)
    E005,//沒有這個帳號
    @ErrorCodeSet(msgRes = R.string.E006)
    E006,//資料格式錯誤
    @ErrorCodeSet(msgRes = R.string.E007)
    E007,//沒有輸入地址
    @ErrorCodeSet(msgRes = R.string.E008)
    E008,//信箱已存在
    @ErrorCodeSet(msgRes = R.string.E009)
    E009,//手機號碼已存在
    @ErrorCodeSet(msgRes = R.string.E010)
    E010,//Token已過期
    @ErrorCodeSet(msgRes = R.string.E011)
    E011,//Token驗證錯誤
    @ErrorCodeSet(msgRes = R.string.E012)
    E012,//帳號或密碼錯誤
    @ErrorCodeSet(msgRes = R.string.E013)
    E013,//檔案格式錯誤
    @ErrorCodeSet(msgRes = R.string.E014)
    E014,//無檔案
    @ErrorCodeSet(msgRes = R.string.E015)
    E015,//手機號碼錯誤
    @ErrorCodeSet(msgRes = R.string.E016)
    E016,//"超出數量"
    @ErrorCodeSet(msgRes = R.string.E017)
    E017,//"權限不足"
    @ErrorCodeSet(msgRes = R.string.E018)
    E018,//"預約失敗"
    @ErrorCodeSet(msgRes = R.string.E019)
    E019,//"次數不足"
    @ErrorCodeSet(msgRes = R.string.E020)
    E020,//"帳單未付款"
    @ErrorCodeSet(msgRes = R.string.E021)
    E021,//時間重疊
    @ErrorCodeSet(msgRes = R.string.E022)
    E022,//權限錯誤
    @ErrorCodeSet(msgRes = R.string.E023)
    E023,//加入錯誤
    @ErrorCodeSet(msgRes = R.string.E024)
    E024//帳號停權
}