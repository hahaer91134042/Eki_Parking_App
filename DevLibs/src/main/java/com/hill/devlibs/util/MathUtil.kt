package com.hill.devlibs.util

import java.util.*

/**
 * Created by Hill on 2018/11/19.
 */
object MathUtil {
    @JvmStatic fun ranInt(min:Int,max:Int):Int=Random().run { nextInt((max - min) + 1)+min }
}