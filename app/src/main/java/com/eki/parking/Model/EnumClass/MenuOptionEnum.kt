package com.eki.parking.Model.EnumClass

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eki.parking.R

/**
 * Created by Hill on 2019/5/3
 */

enum class MenuOptionEnum(@DrawableRes var iconRes:Int,@StringRes var optionRes: Int) {
    BasicSetting(0,R.string.Basic_setting),
    AppointmentCalendar(0,R.string.Your_appointment_calendar),
    BillingOverview(0,R.string.Billing_overview),
    ConsumptionHistory(R.drawable.icon_order_overview,R.string.Consumption_history),
    ParkingSituation(0,R.string.Parking_situation),
    PaymentSetting(0,R.string.Payment_setting),
    Offer(0,R.string.Offer),
    ProblemResponse(R.drawable.icon_problem_response,R.string.Problem_response),
    MemberProblemResponse(R.drawable.icon_problem_response,R.string.Problem_response),
    ManagerProblemResponse(R.drawable.icon_problem_response,R.string.Problem_response),
    CommonProblem(0,R.string.Common_problem),
    ParkingRules(R.drawable.icon_rule,R.string.Parking_rules),
    CollectionBankSetting(0,R.string.Collection_bank_setting),
    LatestEventMessage(0,R.string.Latest_event_message),
    LoginOrRegister(R.drawable.icon_logout,R.string.Login_or_register),
    Logout(R.drawable.icon_logout,R.string.Logout),

    ParkingSiteSetting(R.drawable.icon_parking_set,R.string.Parking_Site_Setting),
    ReservationStatus(R.drawable.icon_site_status,R.string.Reservation_status),
    SiteOpenTime(R.drawable.icon_site_open_time,R.string.Opening_hours),

    BillingOverView(R.drawable.icon_order_overview,R.string.Billing_overview),
    LandOwnerRule(R.drawable.icon_rule,R.string.Land_Owner_Rule),

    CarSetting(R.drawable.icon_car_setting,R.string.Car_Setting),
    Calendar(R.drawable.icon_calendar,R.string.Calendar_option)
}