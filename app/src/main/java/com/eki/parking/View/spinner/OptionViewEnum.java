package com.eki.parking.View.spinner;

import com.eki.parking.R;

/**
 * Created by Hill on 2018/6/8.
 */
enum OptionViewEnum {
    Default(R.layout.item_textview);

    public int viewRes;
    OptionViewEnum(int res) {
        viewRes=res;
    }
}
