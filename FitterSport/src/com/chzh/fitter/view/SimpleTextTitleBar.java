package com.chzh.fitter.view;

import com.chzh.fitter.R;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleTextTitleBar extends SimpleTitleBar{

	private TextView mTitleTextView;

	public SimpleTextTitleBar(Context context) {
		super(context);
	}

	public SimpleTextTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setupViews() {
		super.setupViews();
		mTitleTextView = new TextView(mContext);
		getBackView().setCompoundDrawablesWithIntrinsicBounds(R.drawable.back_arrow, 0, 0, 0);
	}

	public void setTitleText(String title) {
		mTitleTextView.setText(title);
		//字体加粗
		TextPaint tp = mTitleTextView.getPaint();
		tp.setFakeBoldText(true);
		
		addMiddleView(mTitleTextView);
	}

	public void setTitleText(String title, int color, int size) {
		setTitleText(title);
		mTitleTextView.setTextColor(color);
		mTitleTextView.setTextSize(size);
	}
}
