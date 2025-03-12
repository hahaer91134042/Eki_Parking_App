package com.hill.devlibs.tools

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.hill.devlibs.annotation.GsonSkip


/**
 * Created by Hill on 2019/12/25
 */
class GsonExclusion:ExclusionStrategy{
    override fun shouldSkipClass(clazz: Class<*>): Boolean =false
    override fun shouldSkipField(f: FieldAttributes): Boolean = f.getAnnotation(GsonSkip::class.java)!=null
}