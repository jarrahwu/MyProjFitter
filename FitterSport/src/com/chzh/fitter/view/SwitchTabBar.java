package com.chzh.fitter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseView;
import com.chzh.fitter.util.DensityUtil;

/**
 * the bottom tab bar
 */
public class SwitchTabBar extends BaseView{

	private LinearLayout mBarContainer;

	private SwitchBarItemView mPreviousBarItemView;

	private SwitchBarItemClickListener mSwitchBarItemClickListener;

	private static final int[] NORMAL_ICONS = new int[] {R.drawable.home,R.drawable.schedule,R.drawable.friends,R.drawable.me};
	private static final int[] LIGHTED_ICONS = new int[] {R.drawable.home_selected,R.drawable.schedule_selected,R.drawable.friends_selected,R.drawable.me_selected};
	private static final int[] TEXTS = new int[] {R.string.home,R.string.schedule,R.string.friends,R.string.me};
	private static final int[] LIGHTED_TEXT_COLORS = new int[] {R.color.light_yellow, R.color.light_yellow, R.color.light_yellow, R.color.light_yellow};
	private static final int[] NORMAL_TEXT_COLORS = new int[] {R.color.grey_black, R.color.grey_black, R.color.grey_black, R.color.grey_black};

	public SwitchTabBar(Context context) {
		super(context);
	}

	public SwitchTabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setupViews() {

		mBarContainer = new LinearLayout(mContext);
		mBarContainer.setOrientation(LinearLayout.HORIZONTAL);
		mBarContainer.setLayoutParams(getCustomLayoutParams(-1, -1, 0));

		setBarItemResource(NORMAL_ICONS, LIGHTED_ICONS, TEXTS, NORMAL_TEXT_COLORS, LIGHTED_TEXT_COLORS);
		setContentView(mBarContainer);
	}


	private void setBarItemResource(int[] normalIcons, int[] lightedIcons, int[] texts, int[] normalTextColors, int[] lightedTextColors) {
		for (int i = 0; i < TEXTS.length; i++) {
			SwitchBarItemView barItemView = new SwitchBarItemView(mContext);
			barItemView.setIconAndText(normalIcons[i], lightedIcons[i], texts[i], normalTextColors[i], lightedTextColors[i]);
			barItemView.showNormal();
			barItemView.setPosition(i);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -1, 1.0f);
			barItemView.setLayoutParams(lp);
			int top = DensityUtil.dip2px(5);
			int bottom = top;
			barItemView.setPadding(0, top, 0, bottom);

			mBarContainer.addView(barItemView);

			bindClickEvent(barItemView, "onBarItemClicked");
		}
	}

	public void onBarItemClicked(View v) {

		if (v == mPreviousBarItemView) {
			return;
		}

		SwitchBarItemView barItemView = (SwitchBarItemView) v;
		barItemView.showLighted();

		if (mPreviousBarItemView != null) {
			mPreviousBarItemView.showNormal();
		}

		mPreviousBarItemView = barItemView;

		if (mSwitchBarItemClickListener != null) {
			mSwitchBarItemClickListener.onItemClicked(barItemView.getPosition(), barItemView);
		}
	}

	public interface SwitchBarItemClickListener {
		void onItemClicked(int position, SwitchBarItemView itemView);
	}

	public void setSwitchBarItemClickListener(SwitchBarItemClickListener l) {
		mSwitchBarItemClickListener = l;
	}

	public void highligtItemAt(int position) {
		SwitchBarItemView barItemView = (SwitchBarItemView) mBarContainer.getChildAt(position);
		barItemView.showLighted();
		mPreviousBarItemView = barItemView;
	}

}
