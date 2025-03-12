package com.hill.devlibs.listener

/**
 * Created by Hill on 2019/4/26
 */
class ItemClickListener2_bak(listener:ClickListener):ClickListener by listener

interface ClickListener{
    fun onItemClick(which:Int)
}


//var onItemClick:(which:Int)->Unit={
//
//}
//interface Say{
//    fun say()
//}
//
//class KtSay:Say{
//    override fun say() {
//
//    }
//}
//class DelegateKtSay(ktSay:Say):Say by ktSay