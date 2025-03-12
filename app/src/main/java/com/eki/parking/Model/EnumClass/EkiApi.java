package com.eki.parking.Model.EnumClass;


import com.eki.parking.AppConfig;
import com.eki.parking.Model.response.ActionResponse;
import com.eki.parking.Model.response.AddCreditTestResponse;
import com.eki.parking.Model.response.AddLocationResponse;
import com.eki.parking.Model.response.ArgueResponse;
import com.eki.parking.Model.response.BankResponse;
import com.eki.parking.Model.response.BeManagerResponse;
import com.eki.parking.Model.response.CancelOpenSetResponse;
import com.eki.parking.Model.response.CheckChargeResponse;
import com.eki.parking.Model.response.CreditAgreeResponse;
import com.eki.parking.Model.response.DiscountResponse;
import com.eki.parking.Model.response.EditLocationResponse;
import com.eki.parking.Model.response.EditOpenSetResponse;
import com.eki.parking.Model.response.LoadLocationResonse;
import com.eki.parking.Model.response.LoadOrderResponse;
import com.eki.parking.Model.response.LoadReservaResponse;
import com.eki.parking.Model.response.LocationOrderResponse;
import com.eki.parking.Model.response.LoginResponse;
import com.eki.parking.Model.response.ManagerLocIncomeResponse;
import com.eki.parking.Model.response.ManagerLocMulctOrderResponse;
import com.eki.parking.Model.response.ManagerLocationResponse;
import com.eki.parking.Model.response.ManagerLvInfoResponse;
import com.eki.parking.Model.response.ManagerOrderCancelResponse;
import com.eki.parking.Model.response.MemberEditResponse;
import com.eki.parking.Model.response.NotifyResponse;
import com.eki.parking.Model.response.OrderAddResponse;
import com.eki.parking.Model.response.OrderCancelImgResponse;
import com.eki.parking.Model.response.OrderCancelResponse;
import com.eki.parking.Model.response.OrderCheckoutResponse;
import com.eki.parking.Model.response.OrderExtendTimeResponse;
import com.eki.parking.Model.response.OrderLinePayResponse;
import com.eki.parking.Model.response.PostImgResponse;
import com.eki.parking.Model.response.RatingResponse;
import com.eki.parking.Model.response.ReferrerResponse;
import com.eki.parking.Model.response.RegisterResponse;
import com.eki.parking.Model.response.SetLocationImgResponse;
import com.eki.parking.Model.response.SmsConfirmResponse;
import com.eki.parking.Model.response.StartChargeResponse;
import com.eki.parking.Model.response.VehicleListResponse;
import com.eki.parking.Model.response.VehicleResponse;
import com.hill.devlibs.EnumClass.HttpBodyType;
import com.hill.devlibs.EnumClass.HttpProtocol;
import com.hill.devlibs.annotation.ApiConfigSet;
import com.hill.devlibs.annotation.ServerSet;

/**
 * Created by Hill on 2017/11/21.
 */
@ServerSet(url = AppConfig.Url.api)
public enum EkiApi {
    @ApiConfigSet(path = "/member/register",protocal = HttpProtocol.POST,response = RegisterResponse.class)
    Register,
    @ApiConfigSet(path = "/member/login",protocal = HttpProtocol.POST,response = LoginResponse.class)
    Login,
    @ApiConfigSet(path = "/member/PostImg",protocal = HttpProtocol.POST,response = PostImgResponse.class,isAuth = true,bodyType = HttpBodyType.FORMDATA)
    PostImg,
    @ApiConfigSet(path = "/member/AddVehicle",protocal = HttpProtocol.POST,response = VehicleResponse.class,isAuth = true,bodyType = HttpBodyType.FORMDATA)
    AddVehicle,
    @ApiConfigSet(path = "/member/UpdateVehicle",protocal = HttpProtocol.POST,response = VehicleResponse.class,isAuth = true,bodyType = HttpBodyType.FORMDATA)
    UpdateVehicle,
    @ApiConfigSet(path = "/member/DeleteVehicle",protocal = HttpProtocol.POST,response = VehicleListResponse.class,isAuth = true)
    DeleteVehicle,
    @ApiConfigSet(path = "/member/BeManager",protocal = HttpProtocol.POST,response = BeManagerResponse.class,isAuth = true)
    BeManager,
    @ApiConfigSet(path = "/member/Edit",protocal = HttpProtocol.POST,response = MemberEditResponse.class,isAuth = true)
    MemberEdit,
    @ApiConfigSet(path = "/member/EditPwd",protocal = HttpProtocol.POST,response = MemberEditResponse.class,isAuth = true)
    MemberEditPwd,
    @ApiConfigSet(path = "/member/Discount",protocal = HttpProtocol.POST,response = DiscountResponse.class,isAuth = true)
    MemberDiscount,
    @ApiConfigSet(path = "/member/ForgetPwd",protocal = HttpProtocol.POST,response = MemberEditResponse.class)
    MemberForgetPwd,

    @ApiConfigSet(path = "/SMS/Confirm",protocal = HttpProtocol.POST,response = SmsConfirmResponse.class)
    SmsConfirm,

    @ApiConfigSet(path = "/v2/load/location",protocal = HttpProtocol.POST,response = LoadLocationResonse.class)
    LoadLocation,
    @ApiConfigSet(path = "/load/ReservaStatus",protocal = HttpProtocol.POST,response = LoadReservaResponse.class)
    LoadReservaStatus,
    @ApiConfigSet(path = "/load/order",protocal = HttpProtocol.POST,response = LoadOrderResponse.class,isAuth = true)
    LoadOrder,
    @ApiConfigSet(path = "/load/Notify",protocal = HttpProtocol.POST,response = NotifyResponse.class)
    LoadNotify,
    @ApiConfigSet(path = "/load/Action",protocal = HttpProtocol.POST,response = ActionResponse.class,isAuth = true)
    LoadAction,

    @ApiConfigSet(path = "/order/add",protocal = HttpProtocol.POST,response = OrderAddResponse.class,isAuth = true)
    OrderAdd,
    @ApiConfigSet(path = "/order/CheckOut",protocal = HttpProtocol.POST,response = OrderCheckoutResponse.class,isAuth = true,bodyType = HttpBodyType.FORMDATA)
    OrderCheckOut,
    @ApiConfigSet(path = "/order/cancel",protocal = HttpProtocol.POST,response = OrderCancelResponse.class,isAuth = true)
    OrderCancel,
    @ApiConfigSet(path = "/order/CancelImg",protocal = HttpProtocol.POST,response = OrderCancelImgResponse.class,isAuth = true,bodyType = HttpBodyType.FORMDATA)
    OrderCancelImg,
    @ApiConfigSet(path = "/order/ExtendTime",protocal = HttpProtocol.POST,response = OrderExtendTimeResponse.class,isAuth = true)
    OrderExtendTime,
    @ApiConfigSet(path = "/order/LinePay", protocal = HttpProtocol.POST, response = OrderLinePayResponse.class, isAuth = true)
    OrderLinePay,
    @ApiConfigSet(path = "/order/CreditAgree", protocal = HttpProtocol.POST, response = CreditAgreeResponse.class, isAuth = true)
    CreditAgree,

    @ApiConfigSet(path = "/v2/manager/GetLocation",protocal = HttpProtocol.POST,response = ManagerLocationResponse.class,isAuth = true)
    ManagerGetLocation,
    @ApiConfigSet(path = "/manager/DeleteLocation",protocal = HttpProtocol.POST,response = EditLocationResponse.class,isAuth = true)
    ManagerDeleteLocation,
    @ApiConfigSet(path = "/v2/manager/EditLocation",protocal = HttpProtocol.POST,response = EditLocationResponse.class,isAuth = true)
    ManagerEditLocation,
    @ApiConfigSet(path = "/manager/SetLocationImg",protocal = HttpProtocol.POST,response = SetLocationImgResponse.class,isAuth = true,bodyType = HttpBodyType.FORMDATA)
    ManagerSetLocationImg,

    @ApiConfigSet(path = "/v2/manager/AddOpenSet",protocal = HttpProtocol.POST,response = EditOpenSetResponse.class,isAuth = true)
    ManagerAddOpenSet,
    @ApiConfigSet(path = "/v2/manager/CancelOpenSet",protocal = HttpProtocol.POST,response = CancelOpenSetResponse.class,isAuth = true)
    ManagerCancelOpenSet,
    @ApiConfigSet(path = "/v2/manager/AddLocation",protocal = HttpProtocol.POST,response = AddLocationResponse.class,isAuth = true)
    ManagerAddLocation,
    @ApiConfigSet(path = "/manager/LocationOrder",protocal = HttpProtocol.POST,response = LocationOrderResponse.class,isAuth = true) // 
    ManagerLocOrder,
    @ApiConfigSet(path = "/manager/GetBank",protocal = HttpProtocol.POST,response = BankResponse.class,isAuth = true)
    ManagerGetBank,
    @ApiConfigSet(path = "/manager/EditBank",protocal = HttpProtocol.POST,response = BankResponse.class,isAuth = true)
    ManagerEditBank,
    @ApiConfigSet(path = "/manager/AddReferrer",protocal = HttpProtocol.POST,response = ReferrerResponse.class,isAuth = true)
    ManagerAddReferrer,
    @ApiConfigSet(path = "/manager/OrderCancel",protocal = HttpProtocol.POST,response = ManagerOrderCancelResponse.class,isAuth = true)
    ManagerOrderCancel,
    @ApiConfigSet(path = "/manager/LocIncome",protocal = HttpProtocol.POST,response = ManagerLocIncomeResponse.class,isAuth = true)
    ManagerLocIncome,
    @ApiConfigSet(path = "/manager/LocMulctOrder",protocal = HttpProtocol.POST,response = ManagerLocMulctOrderResponse.class,isAuth = true)
    ManagerLocMulctOrder,
    @ApiConfigSet(path = "/manager/LvInfo",protocal = HttpProtocol.POST,response = ManagerLvInfoResponse.class,isAuth = true)
    ManagerLvInfo,


    @ApiConfigSet(path = "/rating/User",protocal = HttpProtocol.POST,response = RatingResponse.class,isAuth = true)
    RatingUser,
    @ApiConfigSet(path = "/rating/Location",protocal = HttpProtocol.POST,response = RatingResponse.class,isAuth = true)
    RatingLocation,

    @ApiConfigSet(path = "/argue/add",protocal = HttpProtocol.POST,response = ArgueResponse.class,isAuth = true,bodyType = HttpBodyType.FORMDATA)
    ArgueAdd,

    @ApiConfigSet(path = "/member/AddCreditTest",protocal = HttpProtocol.POST,response = AddCreditTestResponse.class,isAuth = true)
    AddCreditTest,

    @ApiConfigSet(path = "/order/CheckCharge",protocal = HttpProtocol.POST,response = CheckChargeResponse.class,isAuth = true)
    CheckCharge,
    @ApiConfigSet(path = "/order/StartCharge",protocal = HttpProtocol.POST,response = StartChargeResponse.class,isAuth = true)
    StartCharge

}
