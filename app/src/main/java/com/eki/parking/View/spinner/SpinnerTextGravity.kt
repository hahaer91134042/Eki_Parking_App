package com.eki.parking.View.spinner

import android.view.Gravity

/**
 * Created by Hill on 2020/05/15
 */
enum class SpinnerTextGravity(val gravity: Int) {
    Center(Gravity.CENTER),
    Center_Horizontal(Gravity.CENTER_HORIZONTAL),
    Center_Vertical(Gravity.CENTER_VERTICAL),
    Left(Gravity.LEFT or Gravity.CENTER_VERTICAL);

}