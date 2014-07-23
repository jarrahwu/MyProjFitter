package com.chzh.fitter.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseAttrView;

public class InfoTextView extends BaseAttrView{

	//自定义的资源
	private String mLeftText, mRightText;
	private float mLeftTextSize, mRightTextSize;
	private int mLeftTextColor, mRightTextColor;
	private Drawable mLeftTextDrawable, mRightTextDrawable;

	private TextView mRightTextView;
	private TextView mLeftTextView;

	public InfoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setupViews() {
		super.setupViews();
		setContentView(R.layout.info_text_view);

		//setup custom attrs
		mLeftTextColor = getColor(R.styleable.infoText_left_text_color);
		mRightTextColor = getColor(R.styleable.infoText_right_text_color);

		mLeftTextSize = getDimention(R.styleable.infoText_left_text_size);
		mRightTextSize = getDimention(R.styleable.infoText_right_text_size);

		mLeftText = getString(R.styleable.infoText_left_text);
		mRightText = getString(R.styleable.infoText_right_text);

		mLeftTextDrawable = getDrawable(R.styleable.infoText_drawable_left);
		mRightTextDrawable = getDrawable(R.styleable.infoText_drawable_right);

		mRightTextView = findView(R.id.right_tv, TextView.class);
		mLeftTextView = findView(R.id.left_tv, TextView.class);

		//set res leftside
		mLeftTextView.setTextColor(mLeftTextColor);
		mLeftTextView.setTextSize(mLeftTextSize);
		mLeftTextView.setText(mLeftText);

		//set res rightside
		mRightTextView.setTextColor(mRightTextColor);
		mRightTextView.setTextSize(mRightTextSize);
		mRightTextView.setText(mRightText);

		if (mLeftTextDrawable != null) {
			mLeftTextView.setCompoundDrawablesWithIntrinsicBounds(mLeftTextDrawable, null, null, null);
		}

		if (mRightTextDrawable != null) {
			mRightTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, mRightTextDrawable, null);
		}

		mTypedArray.recycle();

	}

	@Override
	protected TypedArray getTypedArrayById(AttributeSet attrs) {
		return mContext.obtainStyledAttributes(attrs, R.styleable.infoText);
	}

	public void setRightText(String text) {
		mRightTextView.setText(text);
	}
	
	public TextView setRightTextSpannable(SpannableString span, int size) {
		mRightTextView.setText(span);
		mRightTextView.setTextSize(size);
		return mRightTextView;
	}
	
	public void appendRightText(String text) {
		mRightTextView.append(text);
	}
		
	public String getRightText() {
		return mRightTextView.getText().toString();
	}
}
