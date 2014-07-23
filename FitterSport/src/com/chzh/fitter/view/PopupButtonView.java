package com.chzh.fitter.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseView;
import com.chzh.fitter.util.DensityUtil;
import com.chzh.fitter.util.L;

/**
 * 类似微信的功能菜单的从底部popup上来的view
 */
public class PopupButtonView extends BaseView{

	private LinearLayout mContainer;

	private OnPopupItemClickListener mOnPopupItemClickListener;

	private static final int DEFAULT_BTN_HEIGHT_DIP = 60;

	public PopupButtonView(Context context) {
		super(context);
	}

	public PopupButtonView(Context context, String... btnTexts) {
		this(context);

		for (int i = 0; i < btnTexts.length; i++) {
			String text = btnTexts[i];
			addButton(text, i);
		}
		refreshDrawableState();
	}

	/**
	 * add button and set tag with position for click event
	 * @param text
	 * @param position
	 */
	private void addButton(String text, int position) {
		Button btn = new Button(mContext);
		int height = DensityUtil.dip2px(DEFAULT_BTN_HEIGHT_DIP);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, height );
		btn.setLayoutParams(lp);
		btn.setText(text);
		btn.setTag(position);
		btn.setBackgroundResource(R.drawable.popup_menu_btn_selector);
		bindClickEvent(btn, "btnClicked");
		mContainer.addView(btn);
	}

	public void btnClicked(View v) {
		if (mOnPopupItemClickListener != null) {
			int position = (Integer) v.getTag();
			mOnPopupItemClickListener.onItemClick(v, position);
			L.red(position);
		}
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.popup_button_view);
		mContainer = findView(R.id.popup_menu_container, LinearLayout.class);
	}

	/**
	 * popup menu btn的点击事件监听器 postion 就是点击的位置, 从 0 开始
	 *
	 */
	public interface OnPopupItemClickListener {
		void onItemClick(View v, int position);
	}

	public void setOnPopupItemClickListener(OnPopupItemClickListener l) {
		mOnPopupItemClickListener = l;
	}

}
