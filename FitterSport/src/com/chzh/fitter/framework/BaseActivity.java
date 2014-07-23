package com.chzh.fitter.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.chzh.fitter.R;
import com.chzh.fitter.util.L;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Activity 基类
 */
public abstract class BaseActivity extends Activity {

	protected AQuery mAq;

	/**
	 * 当调用setContentView(title , contentView) 的时候, 这个就是这两个子View 的父容器
	 */
	protected LinearLayout mViewContainer;

	protected LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		FitterApplication.getInstance().addExistActivity(getClass().getName().toString(), this);
		super.onCreate(savedInstanceState);
		mAq = new AQuery(this);
		
		if(savedInstanceState == null)
			setupViews();
		else
			restoreViews(savedInstanceState);
	}

	protected void restoreViews(Bundle savedInstanceState) {
		L.red("restore VIEW");
	}

	protected abstract void setupViews();

	protected void requestNoTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 
	 * @param v
	 * @param methodName
	 */
	protected void bindClickEvent(final View v, final String methodName) {
		v.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					Method method = BaseActivity.this.getClass().getMethod(
							methodName, new java.lang.Class[] { View.class });
					method.invoke(BaseActivity.this, v);
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

	protected void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	protected void showToast(int layoutRes) {
		Toast toast = new Toast(this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		initInflater();
		View v = getLayoutInflater().inflate(layoutRes, null, false);
		toast.setView(v);
		toast.show();
	}

	@Override
	protected void onDestroy() {
		mAq.ajaxCancel();
		FitterApplication.getInstance().removeAcitity(getClass().getName().toString());
		super.onDestroy();
	}

	protected Bitmap getImageFromAssetsFile(String fileName) {
		Bitmap image = null;
		AssetManager am = getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}

	protected FitterApplication getFitterApplication() {
		return (FitterApplication) getApplication();
	}

	public void setContentView(View title, View contentView) {
		removeOriginalTitle();

		initViewContainer();

		mViewContainer.addView(title);
		mViewContainer.addView(contentView);
		setContentView(mViewContainer);
	}

	public void setContentView(int titleId, int contentId) {
		removeOriginalTitle();

		initViewContainer();
		initInflater();

		View title = mInflater.inflate(titleId, mViewContainer, false);
		View content = mInflater.inflate(contentId, mViewContainer, false);

		mViewContainer.addView(title);
		mViewContainer.addView(content);

		setContentView(mViewContainer);
	}

	public void setContentView(View title, int contentId) {
		removeOriginalTitle();

		initInflater();
		initViewContainer();

		View content = mInflater.inflate(contentId, mViewContainer, false);

		mViewContainer.addView(title);
		mViewContainer.addView(content);

		setContentView(mViewContainer);
	}

	private void initViewContainer() {
		// if (mViewContainer == null) {
		// mViewContainer = new LinearLayout(this);
		// LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		// mViewContainer.setLayoutParams(params );
		// mViewContainer.setOrientation(LinearLayout.VERTICAL);
		// }
		if (mViewContainer == null) {
			mViewContainer = (LinearLayout) mInflater.inflate(
					R.layout.base_container, null, false);
		}
	}

	protected void initInflater() {
		if (mInflater == null)
			mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	}

	public LayoutInflater getLayoutInflater() {
		if (mInflater == null) {
			throw new RuntimeException(
					"LayoutInflater is null, have you ever use initInflater() ?");
		}
		return mInflater;
	}

	protected void removeOriginalTitle() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 查找fragment 并返回对应的类型
	 * 
	 * @param id
	 * @param claz
	 * @return
	 */
	public <T extends Fragment> T findByFragment(int id, Class<T> claz) {
		@SuppressWarnings("unchecked")
		T t = (T) getFragmentManager().findFragmentById(id);
		return t;
	}

	/**
	 * Activity跳转
	 * 
	 * @param cls
	 */
	public void skipTo(Class<?> cls) {
		Intent i = new Intent();
		i.setClass(this, cls);
		startActivity(i);
	}

	public void skipTo(String extraKey, Serializable data, Class<?> cls) {
		Intent i = new Intent();
		i.putExtra(extraKey, data);
		i.setClass(this, cls);
		startActivity(i);
	}

	protected View findView(int id) {
		View v = null;
		v = findViewById(id);
		if (v == null) {
			Log.e("can not find view id", "~~~~~~~");
		}
		return v;
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T findView(int view_id, Class<T> clz) {
		return (T) findView(view_id);
	}

	protected void setDefaultAppBackground() {
		mViewContainer.setBackgroundResource(R.drawable.bg);
	}

	protected void setAppBackground(int res) {
		mViewContainer.setBackgroundResource(res);
	}

	protected void setAppBackground(Bitmap bmp) {
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
		mViewContainer.setBackgroundDrawable(bitmapDrawable);
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
	 * 拉取图片方法.
	 * 
	 * @param imageview
	 * @param url
	 */
	public void ajaxImage(ImageView imageview, String url) {
		mAq.id(imageview).image(url, false, true, 600, R.drawable.ic_launcher,
				new BitmapAjaxCallback() {
					@Override
					protected void callback(String url, ImageView iv,
							Bitmap bm, AjaxStatus status) {
						iv.setImageBitmap(bm);
						super.callback(url, iv, bm, status);
					}
				});
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
}
