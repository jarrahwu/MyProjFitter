package com.chzh.fitter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chzh.fitter.framework.BaseView;
import com.chzh.fitter.util.DensityUtil;

public class PagerIndicator extends BaseView {

	/**
	 * 小点点的总容器
	 */
	private LinearLayout mLinearLayout;

	private ImageView mPreviousItem;

	private int mResNormal;

	private int mResSelected;

	public PagerIndicator(Context context) {
		super(context);
	}

	public PagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setupViews() {
		mLinearLayout = new LinearLayout(mContext);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT,Gravity.CENTER);
		mLinearLayout.setLayoutParams(params);
		mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		setContentView(mLinearLayout);
	}

	public void initIndicator(int num, int resNormal, int resSelected) {
		mResNormal = resNormal;
		mResSelected = resSelected;
		for (int i = 0; i < num; i++) {
			if (i == 0) {
				mPreviousItem = getItem(mResSelected);
				mLinearLayout.addView(mPreviousItem);
			}else {
				mLinearLayout.addView(getItem(mResNormal));
			}
		}
	}

	public ImageView getItem(int res) {
		ImageView iv = new ImageView(mContext);
		iv.setImageResource(res);
		int padding = DensityUtil.dip2px(4, mContext);
		iv.setPadding(padding, 0, padding, 0);
		return iv;
	}

	public void selectItem(int index) {
		if (mPreviousItem == getImageChild(index)) {
			return;
		}else {
			mPreviousItem.setImageResource(mResNormal);
			ImageView select = getImageChild(index);
			select.setImageResource(mResSelected);
			mPreviousItem = select;
		}
	}

	public ImageView getImageChild(int index) {
		return (ImageView) mLinearLayout.getChildAt(index);
	}

}
