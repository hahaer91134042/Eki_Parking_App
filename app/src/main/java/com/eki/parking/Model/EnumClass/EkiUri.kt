package com.eki.parking.Model.EnumClass

import android.net.Uri

/**
 * Created by Hill on 2019/1/8.
 */
class EkiUri {
    private constructor()
    enum class Scheme(var value: String) {
        None(""),
        Http("http"),
        Https("https"),
        Eki("eki")
    }
    enum class Host(var value:String){
        None(""),
        NewebPay("core.newebpay.com"),
        NewebPayTest("ccore.newebpay.com"),
        EkiWeb("ppyp.app")
    }
    enum class Path(var value:String){
        None(""),
        Checkout("/checkout")
    }
    //目前只有EkiWeb會有
    enum class Action(var value:String){
        None(""),
        Finish("finish"),
        Back("back")
    }
    //目前只有NewebPay會有
    enum class Status(var value:String){
        Fail(""),
        Success("SUCCESS")
    }

    var scheme:Scheme=Scheme.None
    var host:Host=Host.None
    var path:Path=Path.None
    var action:Action=Action.None
    var status:Status=Status.Fail
    companion object{
        fun parse(uri:Uri):EkiUri=EkiUri().apply {
            scheme=when(uri.scheme){
                Scheme.Eki.value->Scheme.Eki
                Scheme.Http.value->Scheme.Http
                Scheme.Https.value->Scheme.Https
                else->Scheme.None
            }
            host=when(uri.host){
                Host.NewebPay.value->Host.NewebPay
                Host.NewebPayTest.value->Host.NewebPayTest
                Host.EkiWeb.value->Host.EkiWeb
                else->Host.None
            }
            path=when(uri.path){
                Path.Checkout.value->Path.Checkout
                else->Path.None
            }
            action=when(uri.getQueryParameter("action")){
                Action.Finish.value->Action.Finish
                Action.Back.value->Action.Back
                else->Action.None
            }
            status=when(uri.getQueryParameter("Status")){
                Status.Success.value->Status.Success
                else->Status.Fail
            }
        }
    }
}