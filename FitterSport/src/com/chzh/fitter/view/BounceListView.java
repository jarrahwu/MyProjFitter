package com.chzh.fitter.view;


import com.chzh.fitter.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class BounceListView extends ListView {
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 10;

	private Context mContext;
	private int mMaxYOverscrollDistance;

	private OnOverScrollListener mOnOverScrollListener;

	public BounceListView(Context context) {
		super(context);
		mContext = context;
		initBounceListView();
	}

	public BounceListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initBounceListView();
	}

	public BounceListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initBounceListView();
	}

	private void initBounceListView() {
		// get the density of the screen and do some maths with it on the max
		// overscroll distance
		// variable so that you get similar behaviors no matter what the screen
		// size

		final DisplayMetrics metrics = mContext.getResources()
				.getDisplayMetrics();
		final float density = metrics.density;

		mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX,
				mMaxYOverscrollDistance, isTouchEvent);
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
//		L.red("over y " + scrollY);
		if (mOnOverScrollListener != null) {
			if (scrollY <= 0) {
				mOnOverScrollListener.onOverScrollUp(scrollY);
			}else {
				mOnOverScrollListener.onOverScrollDown(scrollY);
			}
		}
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}
	
	public interface OnOverScrollListener {
		void onOverScrollUp(int overY);
		void onOverScrollDown(int overY);
	}
	
	public void setOnOverScrollListener(OnOverScrollListener l) {
		mOnOverScrollListener = l;
	}
	
	/**
	 * 设置一个外部的emptyview
	 * @param emptyView
	 */
	public void easyEmptyView(View emptyView) {
		ViewGroup parentView = (ViewGroup) this.getParent();  
		parentView.addView(emptyView, 2);
		setEmptyView(emptyView);
	}
	
	
	/**
	 * 设置一个空的提示语句
	 * @param text
	 */
	public void easyEmptyText(String text) {
		ViewGroup parentView = (ViewGroup) this.getParent();
		TextView tv = new TextView(mContext);
		tv.setTextAppearance(mContext, R.style.white_text_style);
		tv.setText(text);
		parentView.addView(tv, 2);
		setEmptyView(tv);
	}

}