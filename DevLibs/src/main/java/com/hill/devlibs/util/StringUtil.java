package com.hill.devlibs.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.widget.TextView;


import com.hill.devlibs.EnumClass.SpanHelper;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

public class StringUtil {

    public static final int LEFT=0,RIGHT=1,TOP=2,BOTTOM=3;
//    public static final String timeFormat="yyyy-MM-dd hh:mm:ss";

    public static boolean isEmptyString(String src) {
        return src == null || src.trim().length() == 0;
    }

    public static boolean hasAnyEmptyString(String... params) {
        for (String s : params) {
            if (isEmptyString(s)) {
                return true;
            }
        }

        return false;
    }

    public static String getCleanPhoneNum(String input) {
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(input);
        return m.replaceAll("").trim();
    }

    public static String cleanChar(String input){
        return removeEscapeChars(removeWrapChar(removeSpaceChars(input)));
    }

    public static String removeSpaceChars(String input){
        return input.replaceAll(" ","").trim();
    }

    public static String removeEscapeChars(String input) {
        return input.replaceAll("\\\\", "").trim();
    }
    public static String removeWrapChar(String input){
        String regEx = "\\\\n|\\\\s|\\\\t";
        return input.replaceAll(regEx,"").trim();
    }
    public static String removeSpecialChar(String input){
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\\\n|\\\\r|\\\\t|-|\"";
        return input.replaceAll(regEx,"").trim();
    }
    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\\\n|\\\\r|\\\\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static String getFormateMessage(String templete,Object... args) {
        return MessageFormat.format(templete,args);
    }
    public static String getFormateString(String templete,Object... args){
        return String.format(templete,args);
    }
    public static String getNumberFormat(String form,long total) {
        DecimalFormat df=new DecimalFormat(form);
        return df.format(total);
    }



    public static String fillZero3(int number) {
        return String.format("%03d", number);
    }

    public static <V> String linkWithSymbol(String sym,ArrayList<V> list){
        StringBuilder builder=new StringBuilder();
        for (V code:
                list) {
            builder.append(code+sym);
        }
        builder.replace(builder.length()-1,builder.length(),"");
        return builder.toString();
    }


    public static SpannableString getPriceSpanString(String priceOld, String priceNew) {
        boolean hasOld = !StringUtil.isEmptyString(priceOld);
        String totalStr;
        String oldPrice = hasOld ? priceOld : "";
        String nowPrice = hasOld ? " " + priceNew : priceNew;

        if (hasOld) totalStr = oldPrice + nowPrice;
        else totalStr = nowPrice;

        SpannableString priceStr = new SpannableString(totalStr);
        if (hasOld) {
            priceStr.setSpan(new RelativeSizeSpan(0.7f),
                    0, oldPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//相對大小
            priceStr.setSpan(new StyleSpan(Typeface.ITALIC),
                    0, oldPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//字形
            priceStr.setSpan(new StrikethroughSpan(), 0, oldPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//刪除線
            priceStr.setSpan(new ForegroundColorSpan(Color.GRAY),
                    0, oldPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//顏色
        }

        priceStr.setSpan(new RelativeSizeSpan(1.0f),
                oldPrice.length(), totalStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//相對大小
        priceStr.setSpan(new StyleSpan(Typeface.BOLD),
                oldPrice.length(), totalStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//字形
//            priceStr.setSpan(new StrikethroughSpan(),0,oldPrice.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//刪除線
        priceStr.setSpan(new ForegroundColorSpan(Color.RED),
                oldPrice.length(), totalStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//顏色
        return priceStr;
    }


    public static SpanStringBuilder getSpanBuilder() {
        return new SpanStringBuilder();
    }

//    public static SpannableString getImgString(@DrawableRes int imgRes, String text) {
//        ImageSpan imgSpan=new ImageSpan(App.getInstance(),imgRes);
//        SpannableString spanString=new SpannableString(text);
//        spanString.setSpan(imgSpan,0,, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return spanString;
//    }
    public static ImgStringBuilder getImgStringBuilder(){
        return new ImgStringBuilder();
    }



    @NonNull
    public static String format02d(int i){
        return String.format("%02d",i);
    }

    @NonNull
    public static CharSequence convertBrToNewLine(@NonNull String input) {
        return input.replaceAll("<br>|</br>","\n");
    }
    @NonNull
    public static CharSequence removeHtmlBr(@NonNull String input) {
        return input.replaceAll("<br>|</br>","");
    }

    public static class ImgStringBuilder{
        private String text="",hintText="";
        private @DrawableRes int res=0;
        private Bitmap bitmap=null;
        private int position=LEFT;
        private float scale=1.0f;

        public ImgStringBuilder setHint(String text){
            hintText=text;
            return this;
        }
        public ImgStringBuilder setText(String text){
            this.text=text;
            return this;
        }
        public ImgStringBuilder setIcon(@DrawableRes int res){
            this.res=res;
            return this;
        }
        public ImgStringBuilder setIcon(@DrawableRes int res,int position){
            this.res=res;
            this.position=position;
            return this;
        }
        public ImgStringBuilder setIcon(Bitmap map){
            this.bitmap=map;
            return this;
        }
        public ImgStringBuilder setIcon(Bitmap map,int position){
            this.bitmap=map;
            this.position=position;
            return this;
        }
        public ImgStringBuilder setScale(@FloatRange(from = 0.0f) float scale){
            this.scale=scale;
            return this;
        }


        public<v extends TextView> void into(v view){
            Drawable icon=null;

            if (res!=0){
                icon=view.getContext().getDrawable(res);
            }else if (bitmap!=null){
                icon=new BitmapDrawable(view.getContext().getResources(),bitmap);
            }

            if(icon!=null){

                int width=(int)(icon.getIntrinsicWidth()*scale);
                int height=(int)(icon.getIntrinsicHeight()*scale);

                icon.setBounds(0,0,width,height);
                switch (position){
                    case LEFT:
                        view.setCompoundDrawables(icon,null,null,null);
                        break;
                    case RIGHT:
                        view.setCompoundDrawables(null,null,icon,null);
                        break;
                    case TOP:
                        view.setCompoundDrawables(null,icon,null,null);
                        break;
                    case BOTTOM:
                        view.setCompoundDrawables(null,null,null,icon);
                        break;
                }
            }
//            view.setCompoundDrawablesWithIntrinsicBounds(R.this.icon, 0, 0, 0);
            view.setText(text);
            view.setHint(hintText);
        }
    }

    public static class SpanStringBuilder {

        private ArrayList<SpanUnit> unitList = new ArrayList<>();
        private SpannableStringBuilder builder;
        private boolean hasUrl,hasClick=false;

        public SpanStringBuilder setSpanString(String input, SpanHelper helper) {
            SpanUnit unit = new SpanUnit(
                    input,
                    helper.typeFace,
                    helper.relativeSize,
                    helper.textColor,
                    helper.hasStrike,
                    helper.hasSuperscript
            );
            if (helper.hasUrl){
                unit.setUrl(helper.url);
                hasUrl=true;
            }else if (helper.hasClick){
                unit.setClick(helper.click);
                hasClick=true;
            }

            helper.clean();
            unitList.add(unit);
            return this;
        }

        public <V extends TextView> void into(V view) {
            builder = getSpanString();
            view.setText(builder);
            if (hasUrl||hasClick){
                view.setMovementMethod(LinkMovementMethod.getInstance());
            }
            unitList.clear();
        }

        private SpannableStringBuilder getSpanString() {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

            for (int i = 0; i < unitList.size(); i++) {
                int lastLength = stringBuilder.length();
                SpanUnit unit = unitList.get(i);
                stringBuilder.append(unit.inputStr);
                stringBuilder.setSpan(new RelativeSizeSpan(unit.size),
                        lastLength, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//相對大小

                stringBuilder.setSpan(new StyleSpan(unit.typeFace),
                        lastLength, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//字形

                stringBuilder.setSpan(new ForegroundColorSpan(unit.textColor),
                        lastLength, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//顏色

                if (unit.hasStrike)
                    stringBuilder.setSpan(new StrikethroughSpan(),
                            lastLength, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//刪除線

                if (unit.hasUrl)
                    stringBuilder.setSpan(new URLSpan(unit.url),
                            lastLength,stringBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (unit.hasClick)
                    stringBuilder.setSpan(unit.click,
                            lastLength,stringBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (unit.hasSpuerscript)
                    stringBuilder.setSpan(new SuperscriptSpan(),
                            lastLength, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//上標
            }

            return stringBuilder;
        }


        private class SpanUnit {

            public String inputStr;
            public int typeFace;
            public float size;
            public int textColor;
            public boolean hasStrike,hasUrl,hasClick,hasSpuerscript=false;
            public String url="";
            public ClickableSpan click;

            public SpanUnit(String input, int face, float relativeSize, int color, boolean strike,boolean superscript) {
                inputStr = input;
                typeFace = face;
                this.size = relativeSize;
                textColor = color;
                hasStrike = strike;
                hasSpuerscript=superscript;
            }
            public SpanUnit setUrl(String url){
                this.url=url;
                hasUrl=true;
                return this;
            }
            public SpanUnit setClick(ClickableSpan c){
                click=c;
                hasClick=true;
                return this;
            }
        }
    }


    public static String randomString(int len) {
        final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();
    }

}

//    public static String getPriceStringPerThousand(int total){
//        ArrayList<String> priceArr=new ArrayList<>();
//        while (true){
//            int remainder=total%1000;
//
//            if (total>1000)
//                priceArr.add(fillZero3(remainder));
//            else
//                priceArr.add(String.valueOf(remainder));
//
//            total=total/1000;
//            if (total==0)
//                break;
//        }
//
//        StringBuilder builder=new StringBuilder();
//
//        for (int i = priceArr.size()-1; i >=0 ; i--) {
//            builder.append(String.valueOf(priceArr.get(i))+",");
//        }
//        builder.replace(builder.length()-1,builder.length(),"");
//
////        Log.i("price Str->"+builder.toString());
//        return builder.toString();
//    }

//    public static String getPriceStringPerThousand(String unit, int total) {
//        ArrayList<Integer> priceArr = new ArrayList<>();
//        while (true) {
//            int remainder = total % 1000;
//            priceArr.add(remainder);
//            total = total / 1000;
//            if (total == 0)
//                break;
//        }
//
//        StringBuilder builder = new StringBuilder(unit + "$");
//
//        for (int i = priceArr.size() - 1; i >= 0; i--) {
//            builder.append(String.valueOf(priceArr.get(i)) + ",");
//        }
//        builder.replace(builder.length() - 1, builder.length(), "");
//
////        Log.i("price Str->"+builder.toString());
//        return builder.toString();
//    }