package com.chzh.fitter.framework;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class BasePopupWindow extends PopupWindow {

	private View parent;

	public BasePopupWindow(Context context) {
		super(context);
	}

	public BasePopupWindow(View parent, View contentView) {
		super(contentView.getContext());
		setContentView(contentView);
		this.parent = parent;
		setDefaultHeightAndWidth();
	}

	/**
	 * 如果有edit不能获取焦点,不能输入的时候,可以采用这个构造函数
	 *
	 * @param parent
	 * @param contentView
	 * @param focusable
	 */
	public BasePopupWindow(View parent, View contentView, boolean focusable) {
		super(contentView.getContext());
		setContentView(contentView);
		setFocusable(focusable);
		setDefaultHeightAndWidth();
	}

	public void showAtLocation(int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
	}

	/**
	 * 在父容器的中间显示
	 */
	public void showAtParentCenter() {
		showAtLocation(Gravity.CENTER, 0, 0);
	}

	public void enableTouchOutsideDismiss(boolean enable) {
		if(!enable) return;

		Drawable d = new ColorDrawable(Color.BLUE);
		setBackgroundDrawable(d);
		setOutsideTouchable(true);
	}

	/**
	 * 默认大小为,wrap_content
	 */
	public void setDefaultHeightAndWidth() {
		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 设置全屏
	 */
	public void setFullScreen() {
		setHeight(ViewGroup.LayoutParams.FILL_PARENT);
		setWidth(ViewGroup.LayoutParams.FILL_PARENT);
	}

	// <style name="animationStyle的Id">
	// <item
	// name="android:windowEnterAnimation">例如:@anim/share_up_animation</item>
	// <item
	// name="android:windowExitAnimation">例如:@anim/share_down_animation</item>
	// </style>
	/**
	 * @param animationStyle
	 *            看代码注释 上面是style的例子,动画要用xml配,效果要在show之前调用才有出现
	 */
	public void setAnimation(int animationStyle) {
		setAnimationStyle(animationStyle);
	}

	/**
	 * 设置点击能dismiss popupWindow的view
	 *
	 * @param v
	 */
	public void setDismissTarget(View... views) {
		for (int i = 0; i < views.length; i++) {
			View v = views[i];
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}

}
