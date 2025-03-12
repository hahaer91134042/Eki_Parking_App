package com.eki.parking.View.libs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.Size;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;

import com.eki.parking.R;



/**
 * @author deadline
 * @time 2016-11-07
 * Edit by hill on 2017-12-08
 */

public class StateButton extends AppCompatButton{

    //text color
    private int mNormalTextColor = 0;
    private int mPressedTextColor = 0;
    private int mUnableTextColor = 0;
    ColorStateList mTextColorStateList;

    //animation duration
    private int mDuration = 0;

    //radius
    private float mRadius = 0;
    private boolean mRound;

    //stroke
    private float mStrokeDashWidth = 0;
    private float mStrokeDashGap = 0;
    private int mNormalStrokeWidth = 0;
    private int mPressedStrokeWidth = 0;
    private int mUnableStrokeWidth = 0;
    private int mNormalStrokeColor = 0;
    private int mPressedStrokeColor = 0;
    private int mUnableStrokeColor = 0;

    //background color
    private int mNormalBackgroundColor = 0;
    private int mPressedBackgroundColor = 0;
    private int mUnableBackgroundColor = 0;

    private GradientDrawable mNormalBackground;
    private GradientDrawable mPressedBackground;
    private GradientDrawable mUnableBackground;

    private Drawable mNormalIcon;
    private Drawable mPressedIcon;
    private Drawable mUnableIcon;

    private int mNormalIconRes;
    private int mPressedIconRes;
    private int mUnableIconRes;

    private int[][] states;

    StateListDrawable mStateBackground;

    public StateButton(Context context) {
        this(context, null);
    }

    public StateButton(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.buttonStyle);
    }

    public StateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }

    private void setup(AttributeSet attrs) {

        states = new int[4][];

        Drawable drawable = getBackground();
        if(drawable != null && drawable instanceof StateListDrawable){
            mStateBackground = (StateListDrawable) drawable;
        }else{
            mStateBackground = new StateListDrawable();
        }

        mNormalBackground = new GradientDrawable();
        mPressedBackground = new GradientDrawable();
        mUnableBackground = new GradientDrawable();

        //pressed, focused, normal, unable
        states[0] = new int[] { android.R.attr.state_enabled, android.R.attr.state_pressed };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[3] = new int[] { -android.R.attr.state_enabled};
        states[2] = new int[] { android.R.attr.state_enabled };

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StateButton);

        //get original text color as default
        //set text color
        mTextColorStateList = getTextColors();
        int mDefaultNormalTextColor = mTextColorStateList.getColorForState(states[2], getCurrentTextColor());
        int mDefaultPressedTextColor = mTextColorStateList.getColorForState(states[0], getCurrentTextColor());
        int mDefaultUnableTextColor = mTextColorStateList.getColorForState(states[3], getCurrentTextColor());
        mNormalTextColor = a.getColor(R.styleable.StateButton_normalTextColor, mDefaultNormalTextColor);
        mPressedTextColor = a.getColor(R.styleable.StateButton_pressedTextColor, mDefaultPressedTextColor);
        mUnableTextColor = a.getColor(R.styleable.StateButton_unableTextColor, mDefaultUnableTextColor);
        setTextColor();

        //set animation duration
        mDuration = a.getInteger(R.styleable.StateButton_animationDuration, mDuration);
        mStateBackground.setEnterFadeDuration(mDuration);
        mStateBackground.setExitFadeDuration(mDuration);

        //set background color
        mNormalBackgroundColor = a.getColor(R.styleable.StateButton_normalBackgroundColor, 0);
        mPressedBackgroundColor = a.getColor(R.styleable.StateButton_pressedBackgroundColor, 0);
        mUnableBackgroundColor = a.getColor(R.styleable.StateButton_unableBackgroundColor, 0);
        mNormalBackground.setColor(mNormalBackgroundColor);
        mPressedBackground.setColor(mPressedBackgroundColor);
        mUnableBackground.setColor(mUnableBackgroundColor);

        //set radius
        mRadius = a.getDimensionPixelSize(R.styleable.StateButton_btn_radius, 0);
        mRound = a.getBoolean(R.styleable.StateButton_round, false);
        mNormalBackground.setCornerRadius(mRadius);
        mPressedBackground.setCornerRadius(mRadius);
        mUnableBackground.setCornerRadius(mRadius);

        //set stroke
        mStrokeDashWidth = a.getDimensionPixelSize(R.styleable.StateButton_strokeDashWidth, 0);
        mStrokeDashGap = a.getDimensionPixelSize(R.styleable.StateButton_strokeDashWidth, 0);
        mNormalStrokeWidth = a.getDimensionPixelSize(R.styleable.StateButton_normalStrokeWidth, 0);
        mPressedStrokeWidth = a.getDimensionPixelSize(R.styleable.StateButton_pressedStrokeWidth, 0);
        mUnableStrokeWidth = a.getDimensionPixelSize(R.styleable.StateButton_unableStrokeWidth, 0);
        mNormalStrokeColor = a.getColor(R.styleable.StateButton_normalStrokeColor, 0);
        mPressedStrokeColor = a.getColor(R.styleable.StateButton_pressedStrokeColor, 0);
        mUnableStrokeColor = a.getColor(R.styleable.StateButton_unableStrokeColor, 0);
        setStroke();

        mNormalIcon=a.getDrawable(R.styleable.StateButton_normalIcon);
        mPressedIcon=a.getDrawable(R.styleable.StateButton_pressedIcon);
        mUnableIcon=a.getDrawable(R.styleable.StateButton_unableIcon);

        switch (a.getInteger(R.styleable.StateButton_iconPosition,0)){
            case 0:
                position=IconPosition.Center;
                break;
            case 1:
                position=IconPosition.Left;
                break;
            case 2:
                position=IconPosition.Top;
                break;
            case 3:
                position=IconPosition.Right;
                break;
            case 4:
                position=IconPosition.Bottom;
                break;
        }
        imgSize=(int) a.getDimension(R.styleable.StateButton_iconSize,0);
        gap=a.getDimension(R.styleable.StateButton_iconGap,0);

//        PrintLogKt.w("State btn->  mNormalIcon:"+mNormalIcon+" position:"+position+" iconSize:"+imgSize);
//        PrintLogKt.i("State btn size width->"+getMeasuredWidth()+" height->"+getMeasuredHeight());


        if (mPressedIcon!=null){
            mStateBackground.addState(states[0],mPressedIcon);
            mStateBackground.addState(states[1],mPressedIcon);
        }
        if (mNormalIcon!=null)
            mStateBackground.addState(states[2],mNormalIcon);
        if (mUnableIcon!=null)
            mStateBackground.addState(states[3],mUnableIcon);


        //set background
        mStateBackground.addState(states[0], mPressedBackground);
        mStateBackground.addState(states[1], mPressedBackground);
        mStateBackground.addState(states[2], mNormalBackground);
        mStateBackground.addState(states[3], mUnableBackground);

        setBackgroundDrawable(mStateBackground);
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setRound(mRound);
    }

    /****************** stroke color *********************/

    public void setNormalStrokeColor(@ColorInt int normalStrokeColor) {
        this.mNormalStrokeColor = normalStrokeColor;
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth);
    }

    public void setPressedStrokeColor(@ColorInt int pressedStrokeColor) {
        this.mPressedStrokeColor = pressedStrokeColor;
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth);
    }

    public void setUnableStrokeColor(@ColorInt int unableStrokeColor) {
        this.mUnableStrokeColor = unableStrokeColor;
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth);
    }

    public void setStateStrokeColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable){
        mNormalStrokeColor = normal;
        mPressedStrokeColor = pressed;
        mUnableStrokeColor = unable;
        setStroke();
    }

    /****************** stroke width *********************/

    public void setNormalStrokeWidth(int normalStrokeWidth) {
        this.mNormalStrokeWidth = normalStrokeWidth;
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth);
    }

    public void setPressedStrokeWidth(int pressedStrokeWidth) {
        this.mPressedStrokeWidth = pressedStrokeWidth;
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth);
    }

    public void setUnableStrokeWidth(int unableStrokeWidth) {
        this.mUnableStrokeWidth = unableStrokeWidth;
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth);
    }

    public void setStateStrokeWidth(int normal, int pressed, int unable){
        mNormalStrokeWidth = normal;
        mPressedStrokeWidth = pressed;
        mUnableStrokeWidth= unable;
        setStroke();
    }

    public void setStrokeDash(float strokeDashWidth, float strokeDashGap) {
        this.mStrokeDashWidth = strokeDashWidth;
        this.mStrokeDashGap = strokeDashWidth;
        setStroke();
    }

    private void setStroke(){
        setStroke(mNormalBackground, mNormalStrokeColor, mNormalStrokeWidth);
        setStroke(mPressedBackground, mPressedStrokeColor, mPressedStrokeWidth);
        setStroke(mUnableBackground, mUnableStrokeColor, mUnableStrokeWidth);
    }

    private void setStroke(GradientDrawable mBackground, int mStrokeColor, int mStrokeWidth) {
        mBackground.setStroke(mStrokeWidth, mStrokeColor, mStrokeDashWidth, mStrokeDashGap);
    }

    /********************   radius  *******************************/

    public void setRadius(@FloatRange(from = 0) float radius) {
        this.mRadius = radius;
        mNormalBackground.setCornerRadius(mRadius);
        mPressedBackground.setCornerRadius(mRadius);
        mUnableBackground.setCornerRadius(mRadius);
    }

    public void setRound(boolean round){
        this.mRound = round;
        int height = getMeasuredHeight();
        if(mRound){
            setRadius(height / 2f);
        }
    }

    public void setRadius(float[] radii){
        mNormalBackground.setCornerRadii(radii);
        mPressedBackground.setCornerRadii(radii);
        mUnableBackground.setCornerRadii(radii);
    }

    /********************  background color  **********************/

    public void setStateBackgroundColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable){
        mNormalBackgroundColor = normal;
        mPressedBackgroundColor = pressed;
        mUnableBackgroundColor = unable;
        mNormalBackground.setColor(mNormalBackgroundColor);
        mPressedBackground.setColor(mPressedBackgroundColor);
        mUnableBackground.setColor(mUnableBackgroundColor);
    }

    public void setNormalBackgroundColor(@ColorInt int normalBackgroundColor) {
        this.mNormalBackgroundColor = normalBackgroundColor;
        mNormalBackground.setColor(mNormalBackgroundColor);
    }

    public void setPressedBackgroundColor(@ColorInt int pressedBackgroundColor) {
        this.mPressedBackgroundColor = pressedBackgroundColor;
        mPressedBackground.setColor(mPressedBackgroundColor);
    }

    public void setUnableBackgroundColor(@ColorInt int unableBackgroundColor) {
        this.mUnableBackgroundColor = unableBackgroundColor;
        mUnableBackground.setColor(mUnableBackgroundColor);
    }

    /*******************alpha animation duration********************/
    public void setAnimationDuration(@IntRange(from = 0)int duration){
        this.mDuration = duration;
        mStateBackground.setEnterFadeDuration(mDuration);
    }

    /***************  text color   ***********************/

    private void setTextColor() {
        int[] colors = new int[] {mPressedTextColor, mPressedTextColor, mNormalTextColor, mUnableTextColor};
        mTextColorStateList = new ColorStateList(states, colors);
        setTextColor(mTextColorStateList);
    }

    public void setStateTextColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable){
        this.mNormalTextColor = normal;
        this.mPressedTextColor = pressed;
        this.mUnableTextColor = unable;
        setTextColor();
    }

    public void setNormalTextColor(@ColorInt int normalTextColor) {
        this.mNormalTextColor = normalTextColor;
        setTextColor();

    }

    public void setPressedTextColor(@ColorInt int pressedTextColor) {
        this.mPressedTextColor = pressedTextColor;
        setTextColor();
    }

    public void setUnableTextColor(@ColorInt int unableTextColor) {
        this.mUnableTextColor = unableTextColor;
        setTextColor();
    }

    private Drawable iconDrawable;
    private float imgScale=1.0f;
    private IconPosition position=IconPosition.Center;
    private float gap=0f;
    private int imgSize=0;
    private String flag="";
    public enum IconPosition{
        Center,
        Top,
        Bottom,
        Left,
        Right
    }
    public StateButton setFlag(String f){
        flag=f;
        return this;
    }
    public StateButton setBtnIcon(@DrawableRes int res){
        iconDrawable=getContext().getDrawable(res);
        return this;
    }

    public StateButton setIconSize(int size){
        imgSize=size;
        return this;
    }
    public StateButton setIconScale(@Size float s){
        imgScale=s;
        return this;
    }
    public StateButton setIconPosition(IconPosition p,float gap){
        position=p;
        this.gap=gap;
        return this;
    }

    //一樣會重複呼叫
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        PrintLogKt.e(flag+" State dispatchDraw iconDrawable->"+iconDrawable);
//        if (iconDrawable!=null ){
//
//            toDrawNewIcon(canvas);
//        } else {
//            if (mNormalIcon!=null||mPressedIcon!=null||mUnableIcon!=null){
////                iconDrawable=getBackground();
//                setIconFromRes(getBackground());
////                return;
//            }
//        }
//    }

    //要注意 系統會重複執行
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.save();
//        PrintLogKt.e(flag+" State onDraw iconDrawable->"+iconDrawable);
        if (iconDrawable!=null ){

            toDrawNewIcon(canvas);
        } else {
            if (mNormalIcon!=null||mPressedIcon!=null||mUnableIcon!=null){
//                iconDrawable=getBackground();
                setIconFromRes(getBackground());
//                return;
            }
        }
//        int width=getWidth();
//        int height=getHeight();
//        int min=Math.min(width,height);
//
////        imgSize=(int)((float)Math.min(width,height)*imgScale)/2;
//        imgSize=(int)Math.min(min,(float)imgSize*imgScale);//怕圖片爆表
//
//        PrintLogKt.d("State imgSize->"+imgSize);
//        float centerX=(float) width/2.0f;
//        float centerY=(float) height/2.0f;
//
//        int radius=imgSize/2;//因為icon強制正方形
////        int halfHeight=imgSize/2;
//        //set(int left, int top, int right, int bottom)
//        int left=0,top=0,right=0,bottom=0;
//        switch (position){
//            case Center:
//                left=(int)centerX-radius;
//                top=(int)centerY-radius;
//                right=(int)centerX+radius;
//                bottom=(int)centerY+radius;
//
//                break;
//            case Top:
//                left=(int)centerX-radius;
//                top=(int)gap;
//                right=(int)centerX+radius;
//                bottom=(int)top+radius*2;
//
//                break;
//            case Bottom:
//                left=(int)centerX-radius;
//                right=(int)centerX+radius;
//                bottom=height-(int)gap;
//                top=bottom+radius*2;
//
//                break;
//            case Left:
//                left=(int)gap;
//                top=(int)centerY-radius;
//                right=left+radius*2;
//                bottom=(int)centerY+radius;
//
//                break;
//            case Right:
//                right=width-(int)gap;
//                left=right-radius*2;
//                top=(int)centerY-radius;
//                bottom=(int)centerY+radius;
//
//                break;
//        }
//
////        switch (position){
////            case Center:
////                canvas.translate(centerX,centerY);
////                break;
////            case Top:
////                float topY=((float)imgSize+gap)>((float) (height-imgSize))?
////                        (float) (height-imgSize):((float)imgSize+gap);
////                canvas.translate(centerX,topY);
////                break;
////            case Bottom:
////                float btmY=((float)height-gap-(float)imgSize)<0f?
////                        0f:((float)height-gap-(float)imgSize);
////                canvas.translate(centerX,btmY);
////                break;
////            case Left:
////                float leftY=((float) imgSize+gap)>((float)(width-imgSize))?
////                        ((float)(width-imgSize)):((float) imgSize+gap);
////                canvas.translate(leftY,centerY);
////                break;
////            case Right:
////                float rigetY=((float)width-gap-(float)imgSize)<0f?
////                        0f:((float)width-gap-(float)imgSize);
////                canvas.translate(rigetY,centerY);
////                break;
////        }
//
//
//
//
//        Rect imgRect=new Rect();
////        imgRect.set(-imgSize,-imgSize,imgSize,imgSize);
//        imgRect.set(left,top,right,bottom);
//        iconDrawable.setBounds(imgRect);
////        iconDrawable.draw(canvas);


    }

    private void toDrawNewIcon(Canvas canvas) {
        int width=getWidth();
        int height=getHeight();

        int size=imgSize;

        if (size==0)
            size=(int)((float)Math.min(width,height)*imgScale)/2;
        else
            size=(int)(size*imgScale)/2;

        float centerX=(float) width/2.0f;
        float centerY=(float) height/2.0f;

        switch (position){
            case Center:
                canvas.translate(centerX,centerY);
                break;
            case Top:
                float topY=((float)size+gap)>((float) (height-size))?
                        (float) (height-size):((float)size+gap);
                canvas.translate(centerX,topY);
                break;
            case Bottom:
                float btmY=((float)height-gap-(float)size)<0f?
                        0f:((float)height-gap-(float)size);
                canvas.translate(centerX,btmY);
                break;
            case Left:
                float leftY=((float) size+gap)>((float)(width-size))?
                        ((float)(width-size)):((float) size+gap);
                canvas.translate(leftY,centerY);
                break;
            case Right:
                float rigetY=((float)width-gap-(float)size)<0f?
                        0f:((float)width-gap-(float)size);
                canvas.translate(rigetY,centerY);
                break;
        }




        Rect imgRect=new Rect();
        imgRect.set(-size,-size,size,size);

        iconDrawable.setBounds(imgRect);
        iconDrawable.draw(canvas);
    }

    private void setIconFromRes(Drawable icon) {

        int width=getWidth();
        int height=getHeight();
        int min=Math.min(width,height);

//        imgSize=(int)((float)Math.min(width,height)*imgScale)/2;
        int size=imgSize;

        if (size==0)
            size=(int)Math.min(min,(float)size*imgScale);//怕圖片爆表
        else
            size=(int)(size*imgScale);

//        HHLog.d(flag+" State imgSize->"+imgSize);
        float centerX=(float) width/2.0f;
        float centerY=(float) height/2.0f;

        int radius=size/2;//因為icon強制正方形
//        int halfHeight=imgSize/2;
        //set(int left, int top, int right, int bottom)
        int left=0,top=0,right=0,bottom=0;
        switch (position){
            case Center:
                left=(int)centerX-radius;
                top=(int)centerY-radius;
                right=(int)centerX+radius;
                bottom=(int)centerY+radius;

                break;
            case Top:
                left=(int)centerX-radius;
                top=(int)gap;
                right=(int)centerX+radius;
                bottom=(int)top+radius*2;

                break;
            case Bottom:
                left=(int)centerX-radius;
                right=(int)centerX+radius;
                bottom=height-(int)gap;
                top=bottom+radius*2;

                break;
            case Left:
                left=(int)gap;
                top=(int)centerY-radius;
                right=left+radius*2;
                bottom=(int)centerY+radius;

                break;
            case Right:
                right=width-(int)gap;
                left=right-radius*2;
                top=(int)centerY-radius;
                bottom=(int)centerY+radius;

                break;
        }
        
        Rect imgRect=new Rect();

        imgRect.set(left,top,right,bottom);
        icon.setBounds(imgRect);
    }
}
