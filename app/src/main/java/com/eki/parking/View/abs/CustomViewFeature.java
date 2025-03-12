package com.eki.parking.View.abs;

import android.content.res.TypedArray;

import com.hill.devlibs.impl.InflatViewImpl;
import com.hill.devlibs.impl.LayoutParamsImpl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;

/**
 * Created by Hill on 2017/11/20.
 */

public interface CustomViewFeature extends InflatViewImpl, LayoutParamsImpl {
    // TODO:Edit by Hill 2018/11/21 在kotlin裡面會有問題 不要使用
    // TODO: 2019/6/26 kotlin 裡面要再去呼叫一次 parseAttrSet(attrs)
    @Nullable
    @StyleableRes int[] setStyleableRes();
    void parseTypedArray(@NonNull TypedArray typedArray);
}
