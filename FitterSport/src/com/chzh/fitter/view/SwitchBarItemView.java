package com.chzh.fitter.view;

import com.chzh.fitter.framework.BaseView;
import com.chzh.fitter.util.DensityUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SwitchBarItemView extends BaseView{

	//显示点击状态或者普通状态的TAG
	public static final int STATE_NORMAL = 1;
	public static final int STATE_LIGHTED = 2;

	//记录baritem在bar的哪个位置
	private int mPosition;

	private LinearLayout mItemContainer;

	//element
	private ImageView mIcon;
	private TextView mText;

	//icon res
	private int mLigtedIcon;
	private int mNormalIcon;

	//text res
	private int mTextId;
	private int mLigtedTextColor;
	private int mNormalTextColor;

	public SwitchBarItemView(Context context) {
		super(context);
	}

	public SwitchBarItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setupViews() {
		//init container
		mItemContainer = new LinearLayout(mContext);
		mItemContainer.setOrientation(LinearLayout.VERTICAL);
		mItemContainer.setLayoutParams(getCustomLayoutParams(-1, -1, 0));

		//init icon
		mIcon = new ImageView(mContext);
		mIcon.setScaleType(ScaleType.FIT_XY);
		//80 width 80 height
		mIcon.setLayoutParams(getPencentageParams(80, 80 , 0.0f));

		//init text
		mText = new TextView(mContext);

		int size = DensityUtil.dip2px(4); //4dip size
		mText.setTextSize(size);
		mText.setLayoutParams(getPencentageParams(-1, -2, 0.0f));
		mText.setGravity(Gravity.CENTER);

		//add subchildview
		mItemContainer.addView(mIcon);
		mItemContainer.addView(mText);
		mItemContainer.setGravity(Gravity.CENTER);
//		mItemContainer.setBackgroundColor(Color.BLUE);

		setContentView(mItemContainer);
	}

	private LinearLayout.LayoutParams getPencentageParams(int width, int height, float f) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height, f);
		return lp ;
	}

	public void setIconAndText(int normalIcon, int lightedIcon, int textId, int normalTextColor, int lightedTextColor) {
		mNormalIcon = normalIcon;
		mLigtedIcon= lightedIcon;
		mTextId = textId;
		mNormalTextColor = normalTextColor;
		mLigtedTextColor = lightedTextColor;

		mText.setText(mTextId);
	}

	public void setState(int state) {

		switch (state) {
		case STATE_NORMAL:
			showNormal();
			break;

		case STATE_LIGHTED:
			showLighted();
			break;

		default:
			Log.e(this.getClass().toString(), "setState error! only normal and lighted!");
			break;
		}
	}

	public void showLighted() {
		setResource(mLigtedIcon, getResources().getColor(mLigtedTextColor));
	}

	public void showNormal() {
		setResource(mNormalIcon, getResources().getColor(mNormalTextColor));
	}

	private void setResource(int iconId, int textColorId) {
		mIcon.setImageResource(iconId);
		mText.setTextColor(textColorId);
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int position) {
		this.mPosition = position;
	}
}
