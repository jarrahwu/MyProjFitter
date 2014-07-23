package com.chzh.fitter.framework;



import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.chzh.fitter.R;

/**
 * 自定义View 的基类 
 */
public abstract  class BaseView extends FrameLayout{

	protected AQuery mAq;

	private Activity mActivity;

	protected Context mContext;

	protected AttributeSet mAttrs;

	protected View mView;

	protected LayoutInflater mInflater;

	private boolean isDebug = true;

	public BaseView(Context context){
		super(context);
		if(isDebug) Log.d("VIEW", this.getClass().toString());
		init();
		setupViews();
	}

	public BaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isDebug) Log.d("VIEW", this.getClass().toString());
		mAttrs = attrs;//added by jarrah 14.4.28
		init();
		setupViews();
	}

	protected void init() {
		mContext = getContext();
		mAq = new AQuery(mContext);
		mInflater = LayoutInflater.from(mContext);
	}

	public void setContentView(int layoutId){
		mView = mInflater.inflate(layoutId, null);
		addView(mView);

	}

	public void setContentView(View v){
		addView(v);
	}

	/**
	 * 弹出toast
	 * @param text
	 */
	protected void showToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 拉取图片方法.
	 * @param imageview
	 * @param url
	 */
	public void ajaxImage(ImageView imageview,String url){
		mAq.id(imageview).image(url, false, true, 600, R.drawable.ic_launcher, new BitmapAjaxCallback(){
			@Override
			protected void callback(String url, ImageView iv, Bitmap bm,
					AjaxStatus status) {
				iv.setImageBitmap(bm);
				super.callback(url, iv, bm, status);
			}
		});
	}




	/**
	 * 拉取网络图片并切成圆角
	 * @param imageview
	 * @param url
	 * @param px 圆角的像素大小
	 */
	public void ajaxCornerImage(ImageView imageview,String url,final int px){
		mAq.id(imageview).image(url, false, true, 0, R.drawable.ic_launcher, new BitmapAjaxCallback(){
			@Override
			protected void callback(String url, ImageView iv, Bitmap bm,
					AjaxStatus status) {
				iv.setImageBitmap(cornerBitmap(bm,px));
			}
		});
	}

	protected Bitmap getImageFromAssetsFile(String fileName)
	  {
	      Bitmap image = null;
	      AssetManager am = getResources().getAssets();
	      try
	      {
	          InputStream is = am.open(fileName);
	          image = BitmapFactory.decodeStream(is);
	          is.close();
	      }
	      catch (IOException e)
	      {
	          e.printStackTrace();
	      }

	      return image;
	  }

	/**
	 * 切成圆角的图片
	 * @param bitmap 数据源
	 * @param pixels 圆角的像素
	 * @return
	 */
	public Bitmap cornerBitmap(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff000000;
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


	protected View findView(int id) {
		View v = null;
		v = findViewById(id);
		if (v == null) {
			Log.e("can not find view id", "~~~~~~~");
		}
		return v;
	}

	/**
	 * 绑定点击事件
	 * @param v 需要绑定的View
	 * @param methodName 绑定的方法名 目前只是支持void类型,参数为View的,
	 * 如: private void abc(View v) {
	 * 	   }
	 */
	protected void bindClickEvent(final View v, final String methodName) {
		v.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					Method method = BaseView.this.getClass().getMethod(methodName, new java.lang.Class[]{View.class});
					method.invoke(BaseView.this, v);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * view的显示方法,需要重写
	 */
	public abstract void setupViews();

	protected Activity getConvertActivity() {
		mActivity = (Activity)mContext;
		return mActivity;
	}

	/**
	 * 获取自定以的layoutParams
	 * @param width -1 : match_parent    -2 : wrap_content
	 * @param height -1 : match_parent    -2 : wrap_content
	 * @return
	 */
	protected FrameLayout.LayoutParams getCustomLayoutParams(int width, int height, int gravity) {
		FrameLayout.LayoutParams vlp = new FrameLayout.LayoutParams(width, height, gravity);
		return vlp;
	}


	public void gone(View v) {
		v.setVisibility(View.GONE);
	}

	public void visible(View v) {
		v.setVisibility(View.VISIBLE);
	}

	public void invisible(View v) {
		v.setVisibility(View.INVISIBLE);
	}

	public void gone(int id) {
		findView(id).setVisibility(View.GONE);
	}

	public void visible(int id) {
		findView(id).setVisibility(View.VISIBLE);
	}

	public void invisible(int id) {
		findView(id).setVisibility(View.INVISIBLE);
	}

	/**
	 * 判断是否为横屏
	 * @return
	 */
	protected boolean isLandscape() {
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return true;
		}
		return false;
	}

	public void skipTo(Class<?> cls) {
		Intent i = new Intent();
		i.setClass(mContext, cls);
		mContext.startActivity(i);
	}

	public void skipTo(String extraKey, Serializable data, Class<?> cls) {
		Intent i = new Intent();
		i.putExtra(extraKey, data);
		i.setClass(mContext, cls);
		mContext.startActivity(i);
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T findView(int view_id, Class<T> clz) {
		return (T) findView(view_id);
	}

}

