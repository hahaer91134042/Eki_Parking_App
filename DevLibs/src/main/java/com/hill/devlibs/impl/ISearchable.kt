package com.hill.devlibs.impl

/**
 * Created by Hill on 18,10,2019
 */
interface ISearchable {
    fun onSearchStart(text:String)
    fun onSearch(text:String)
}