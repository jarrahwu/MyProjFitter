package com.chzh.fitter.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.chzh.fitter.R;
import com.chzh.fitter.util.L;

/**
 * fragment 基类
 */
public abstract class BaseFragment extends Fragment {

	protected AQuery mAq;
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected View mContentView;

	private void init() {
		mContext = getActivity();
		mAq = new AQuery(mContext);
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = getContentView();
		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupViews();
	}

	/**
	 * 重写这个方法并返回需要显示的View
	 * 
	 * @return
	 */
	protected abstract View getContentView();

	/**
	 * 从资源实例化View
	 * 
	 * @param resId
	 * @return
	 */
	protected View inflateViewFrom(int resId) {
		return mInflater.inflate(resId, null, false);
	}

	/**
	 * 绑定点击事件
	 * 
	 * @param v
	 *            需要绑定的View
	 * @param methodName
	 *            绑定的方法名 目前只是支持void类型,参数为View的, 如: private void abc(View v) { }
	 */
	protected void bindClickEvent(final View v, final String methodName) {
		v.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					Method method = BaseFragment.this.getClass().getMethod(
							methodName, new java.lang.Class[] { View.class });
					method.invoke(BaseFragment.this, v);
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
	
	
	protected void bindClickEvent(int id, final String methodName) { 
		View v = findView(id);
		if (v != null) {
			bindClickEvent(v, methodName);
		}else {
			L.red("v not found");
		}
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

	protected View findView(int id) {
		if (mContentView != null) {
			View v = mContentView.findViewById(id);
			return v;
		} else {
			return getActivity().findViewById(id);
		}
	}

	protected void setContextView(View parent) {
		mContentView = parent;
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T findView(int view_id, Class<T> clz) {
		return (T) findView(view_id);
	}

	protected abstract void setupViews();

	/**
	 * Activity跳转
	 * 
	 * @param cls
	 */
	public void skipTo(Class<?> cls) {
		Intent i = new Intent();
		i.setClass(getActivity(), cls);
		startActivity(i);
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
}
