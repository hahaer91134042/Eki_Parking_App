package com.hill.devlibs.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

/**
 * Created by Hill on 2019/12/18
 */
fun File.toBitMap():Bitmap=BitmapFactory.decodeFile(path)