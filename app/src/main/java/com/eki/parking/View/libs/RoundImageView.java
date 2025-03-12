package com.eki.parking.View.libs;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.util.AttributeSet;

import android.util.TypedValue;
import android.widget.ImageView;

import com.eki.parking.R;
import com.hill.devlibs.impl.IAutoLoadImg;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Edit by Hill on 2018/05/16
 */

public class RoundImageView extends AppCompatImageView
					        implements IAutoLoadImg {

	/** 圖片的類型，圓形or圓角 */
	private int type;
	private static final int TYPE_CIRCLE = 0;
	private static final int TYPE_RECTANGLE = 1,TYPE_SQUARE=2;

	/** 圓角大小的默認值 */
	private static final int BODER_RADIUS_DEFAULT = 100;
	/** 圓角的大小 */
	private int mBorderRadius;

	/** 繪圖的Paint */
	private Paint mBitmapPaint;
	/** 圓角的半徑 */
	private int mRadius;
	/** 3x3 矩陣，主要用於縮小放大 */
	private Matrix mMatrix;
	/** 渲染圖像，使用圖像為繪制圖形著色 */
	private BitmapShader mBitmapShader;
	/** view的寬度 */
	private int mWidth,mHeight;
	private RectF mRoundRect;

	@NotNull
	@Override
	public ImageView loadImg() {
		return this;
	}

	public enum ImgType{
		Circle(0),
		Rectangle(1),
		Square(2)
		;

		public int type;
		public int radius=0;
		ImgType(int i) {
			type=i;
		}
		public ImgType setBorderRadius(int radius){
			this.radius=radius;
			return this;
		}
	}

	public RoundImageView(Context context) {
		this(context,null);
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mMatrix = new Matrix();
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundImageView,0,0);

		mBorderRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_borderRadius, (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BODER_RADIUS_DEFAULT, getResources().getDisplayMetrics()));// 默認為10dp
//		HHLog.d("mBorderRadius->"+mBorderRadius);
		type = a.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);// 默認為Circle
//		HHLog.d("icon Type->"+type);
		a.recycle();
	}

	public RoundImageView setImgType(ImgType imgTypeEnum){
		type=imgTypeEnum.type;
		mBorderRadius=imgTypeEnum.radius;

		return this;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		HHLog.e("TAG", "onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        HHLog.d("onMeasure: widthMeasureSpec->"+widthMeasureSpec+" heightMeasureSpec->"+heightMeasureSpec);
		/**
		 * 如果類型是圓形，則強制改變view的寬高一致，以小值為準
		 */

//		HHLog.d("getMeasuredWidth()->"+getMeasuredWidth()+" getMeasuredHeight()->"+getMeasuredHeight());

		switch (type){
			case TYPE_CIRCLE:
			case TYPE_SQUARE:
				mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
				if (type == TYPE_CIRCLE) {
					mRadius = (int) (mWidth / 2.5);
				}else if (type==TYPE_SQUARE){
					mRoundRect=new RectF(0,0,mWidth,mWidth);//設定外框大小
				}
				setMeasuredDimension(mWidth, mWidth);//重新設定載入圖檔的顯示寬高
				break;
			case TYPE_RECTANGLE:
				mWidth=getMeasuredWidth();
				mHeight=getMeasuredHeight();
				mRoundRect=new RectF(0,0,mWidth,mHeight);//設定外框大小
				setMeasuredDimension(mWidth,mHeight);
				break;
		}


	}

	protected void setUpShader(Drawable drawable) {

		Bitmap bmp = drawableToBitamp(drawable);
		// 將bmp作為著色器，就是在指定區域內繪制bmp
		mBitmapShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
		float scale = 1.0f;
		switch (type){
			case TYPE_CIRCLE:
			case TYPE_SQUARE:
				if (type == TYPE_CIRCLE) {
					// 拿到bitmap寬或高的小值
					int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
					scale = mWidth * 1.0f / bSize;

				} else if (type == TYPE_SQUARE) {
					// 如果圖片的寬或者高與view的寬高不匹配，計算出需要縮放的比例；縮放後的圖片的寬高，一定要大於我們view的寬高；所以我們這裏取大值；
					scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight() * 1.0f / bmp.getHeight());
				}
				// shader的變換矩陣，我們這裏主要用於放大或者縮小
				mMatrix.setScale(scale, scale);
				break;
			case TYPE_RECTANGLE:
				float scaleX=getWidth()*1.0f/(float) bmp.getWidth();
				float scaleY=getHeight()*1.0f/(float)bmp.getHeight();
//				HHLog.d("preview scaleX->"+scaleX+" scaleY->"+scaleY);
				mMatrix.setScale(scaleX,scaleY);
				break;
		}

		// 設置變換矩陣
		mBitmapShader.setLocalMatrix(mMatrix);
		// 設置shader
		mBitmapPaint.setShader(mBitmapShader);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}
		setUpShader(getDrawable());
		setUpCanvas(canvas);


		invalidate();

//		super.onDraw(canvas);
	}

	protected void setUpCanvas(Canvas canvas) {
		if (canvas==null) return;

		if (type == TYPE_RECTANGLE || type==TYPE_SQUARE) {
			canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius, mBitmapPaint);
		} else {
			canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
		}
	}

	/**
	 * drawable轉bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	private Bitmap drawableToBitamp(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			return bd.getBitmap();
		}
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}
}
