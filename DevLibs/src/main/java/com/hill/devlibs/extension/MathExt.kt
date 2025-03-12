package com.hill.devlibs.extension

import androidx.annotation.IntRange
import java.math.BigDecimal


/**
 * Created by Hill on 11,11,2019
 */
//無條件進位 scale 表示要到小數下第幾位
fun Double.ceil(@IntRange(from=0, to = 3) scale: Int=1):Double {
    return BigDecimal(this).setScale(scale,BigDecimal.ROUND_CEILING).toDouble()
}
//無條件捨棄 scale 表示要到小數下第幾位
fun Double.floor(@IntRange(from=0, to = 3) scale: Int=1):Double {
    return BigDecimal(this).setScale(scale,BigDecimal.ROUND_FLOOR).toDouble()
}
//四捨五入 scale 表示要到小數下第幾位
fun Double.halfUp(@IntRange(from=0, to = 3)scale:Int=1):Double{
    return BigDecimal(this).setScale(scale,BigDecimal.ROUND_HALF_UP).toDouble()
}
//避免java的double相乘會出現的精度誤差
fun Double.multiply(other:Double):Double{
    return BigDecimal(this.toString()).times(BigDecimal(other.toString())).toDouble()
}
//避免java的double相除會出現的精度誤差
fun Double.divide(other:Double):Double{
    return BigDecimal(this.toString()).div(BigDecimal(other.toString())).toDouble()
}