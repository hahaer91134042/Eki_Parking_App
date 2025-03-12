package com.eki.parking.Model.EnumClass

import com.eki.parking.R

enum class MarkerIcon constructor(var res: Int){

    NoneCar(R.drawable.icon_marker_gray_car),
    NoneLocomotive(R.drawable.icon_marker_gray_motor),
    AcCar(R.drawable.icon_marker_ac_car),
    AcLocomotive(R.drawable.icon_marker_ac_motor),
    DcCar(R.drawable.icon_marker_dc_car),
    DcLocomotive(R.drawable.icon_marker_dc_motor);

}