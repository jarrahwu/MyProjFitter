package com.chzh.fitter.view;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.chzh.fitter.R;
import com.chzh.fitter.util.DensityUtil;
import com.chzh.fitter.util.FileUtil;
import com.chzh.fitter.util.ImageUtil;
import com.chzh.fitter.util.L;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 圆角的ImageView 可以设置圆角图片
 */
public class RoundImageView extends ImageView {

	Path clipPath;
	private RectF rect;
	private static final float DEFALUT_RADIUS = DensityUtil.dip2px(10.0F);
	private float radius =  DEFALUT_RADIUS;// 默认是5px
	private AQuery mAq;

	public RoundImageView(Context context) {
		super(context);
		init();
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ConnerImage);
		this.radius = array.getFloat(R.styleable.ConnerImage_radius, DEFALUT_RADIUS);
		array.recycle();
	}

	public RoundImageView(Context context, float radius) {
		this(context);
		this.radius = radius;
	}

	private void init() {
		clipPath = new Path();
		mAq = new AQuery(getContext());
	}

	// we can get the size here, this is the first time view's appearance
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		rect = new RectF(0, 0, this.getWidth(), this.getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
		canvas.clipPath(clipPath);
		super.onDraw(canvas);

	}

	public void setCornerRadiusSize(int px) {
		radius = px;
		invalidate();
	}

	/**
	 * 设置边框
	 *
	 * @param borderSize
	 * @param res
	 */
	public void setPaddingBorder(int borderSize, int res) {
		setPadding(borderSize, borderSize, borderSize, borderSize);
		setAdjustViewBounds(true);
		setBackgroundResource(res);
		invalidate();
	}

	/**
	 * 设置背景边框
	 * @param borderSize 背景边框大小
	 * @param res 背景资源
	 * @param pixels 需要圆角的大小, 如果 doCorner = true
	 * @param doCorner 是否需要切成圆角
	 */
	public void setBitmapBorder(int borderSize, Bitmap res, int pixels, boolean doCorner) {
		setPadding(borderSize, borderSize, borderSize, borderSize);
		setAdjustViewBounds(true);


		if(doCorner)
			res = cornerBitmap(res, pixels);

		BitmapDrawable drawable = new BitmapDrawable(res);
		if (drawable != null) {
			setBackgroundDrawable(drawable);
			invalidate();
		}
	}

	/**
	 * 切成圆角的图片
	 *
	 * @param bitmap
	 *            数据源
	 * @param pixels
	 *            圆角的像素
	 * @return
	 */
	public Bitmap cornerBitmap(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff000000;
		// final int color = 0x000000;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 拉取图片方法.
	 * @param imageview
	 * @param url
	 */
	public void ajaxImage(String url){
		L.red("图片url " + url);
		mAq.id(this).image(url, false, true, 600, R.drawable.h_2, new BitmapAjaxCallback(){
			@Override
			protected void callback(String url, ImageView iv, Bitmap bm,
					AjaxStatus status) {
				iv.setImageBitmap(bm);
			}
		});
	}
	
	public void ajaxAutoOrientationImage(String url) {
		L.red("图片url " + url);
		mAq.id(this).image(url, false, true, 600, R.drawable.h_2, new BitmapAjaxCallback(){
			@Override
			protected void callback(String url, ImageView iv, Bitmap bm,
					AjaxStatus status) {
				String imgPath = FileUtil.saveFile(bm, FileUtil.IMAGE_DIR + "/", "tempHeader.jpg", true);
				Bitmap img = ImageUtil.autoFixOrientation(bm, null, null, imgPath);
				setImageBitmap(img);
			}
		});
	}


}
