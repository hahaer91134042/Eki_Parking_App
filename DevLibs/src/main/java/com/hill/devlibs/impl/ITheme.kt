package com.hill.devlibs.impl


/**
 * Created by Hill on 2020/10/29
 * 給一般的control使用 來標記該control要使用哪種主題
 */
interface ITheme<T:IThemeColor> {
    var theme:T
}