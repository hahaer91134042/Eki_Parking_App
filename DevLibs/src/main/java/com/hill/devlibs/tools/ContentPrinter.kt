package com.hill.devlibs.tools

/**
 * Created by Hill on 16,10,2019
 */
class ContentPrinter {
    var keys= ArrayList<String>()
    var values= ArrayList<Any?>()



    fun addKey(key:String):ContentPrinter{
        keys.add(key)
        return this
    }
    fun addValue(value:Any):ContentPrinter{
        values.add(value)
        return this
    }
    fun setKeys(vararg keys: String): ContentPrinter {
        this.keys.addAll(keys.toList())
        return this
    }

    fun setValues(vararg values: Any?): ContentPrinter {
        this.values.addAll(values.toList())
        return this
    }
}