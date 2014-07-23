package com.chzh.fitter.view;

import com.chzh.fitter.R;
import com.chzh.fitter.util.DensityUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class MainTitleBar extends SimpleTitleBar {

	private TextView mMidTitle;

	public MainTitleBar(Context context) {
		super(context);
	}

	public MainTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MainTitleBar(Context context, String titleText) {
		this(context);
		if (mMidTitle == null) {
			mMidTitle = new TextView(context);
			mMidTitle.setText(titleText);
			mMidTitle.setTextColor(Color.WHITE);
			mMidTitle.setTextSize(DensityUtil.dip2px(6));
			mMidTitle.setLayoutParams(getCustomLayoutParams(-2, -2, Gravity.CENTER));
			
			//字体加粗
			TextPaint tp = mMidTitle.getPaint();
			tp.setFakeBoldText(true);
			
			this.addMiddleView(mMidTitle);

			TextView tv = (TextView) getBackView();
			tv.setText("更多");
			tv.setTextAppearance(mContext, R.style.white_text_style);
			refreshDrawableState();
		}
	}

	public void setTittle(String text) {
		mMidTitle.setText(text);
 	}

}
