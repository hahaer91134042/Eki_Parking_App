package com.hill.devlibs.impl

import androidx.annotation.ColorRes
import androidx.annotation.StyleRes

/**
 * Created by Hill on 2020/10/30
 * 實作主題物件使用
 */
interface IThemeColor {
    @ColorRes fun colorRes():Int
    @StyleRes fun styleRes():Int
}