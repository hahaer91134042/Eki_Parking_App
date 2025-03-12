package com.hill.devlibs.EnumClass;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ClickableSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;

/**
 * Created by Hill on 2018/5/23.
 */
public enum SpanHelper{
    TYPE_NORMAL(Typeface.NORMAL),
    TYPE_ITALIC(Typeface.ITALIC),
    TYPE_BOLD(Typeface.BOLD),
    TYPE_ITALIC_BOLD(Typeface.BOLD_ITALIC)
    ;

    public int typeFace;
    public float relativeSize=1.0f;
    public int textColor= Color.BLACK;
    public boolean hasStrike=false,hasUrl=false,hasClick=false,hasSuperscript=false;
    public String url="";
    public ClickableSpan click;
    SpanHelper(int type) {
        typeFace=type;
    }
    public SpanHelper setRelativeSize(@FloatRange(from = 0.0f,to = 2.0f) float size){
        relativeSize=size;
        return this;
    }
    public SpanHelper setTextColor(@ColorInt int color){
        textColor=color;
        return this;
    }
    public SpanHelper setUrlString(String url){
        this.url=url;
        hasUrl=true;
        return this;
    }
    public SpanHelper setClick(ClickableSpan click){
        this.click=click;
        hasClick=true;
        return this;
    }
    public SpanHelper hasSuperscript(){
        hasSuperscript=true;
        return this;
    }

    public SpanHelper hasStrikeSpan(boolean has){
        hasStrike=has;
        return this;
    }

    // TODO:Edit by Hill 2018/5/23 因為是static 所以用完要歸0
    public void clean(){
        relativeSize=1.0f;
        textColor=Color.BLACK;
        hasStrike=false;
        url="";
        hasUrl=false;
        click=null;
        hasClick=false;
    }
}
