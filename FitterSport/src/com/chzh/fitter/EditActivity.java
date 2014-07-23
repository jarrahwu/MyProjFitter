
package com.chzh.fitter;

import android.content.Intent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.chzh.fitter.framework.BaseActivity;
import com.chzh.fitter.util.DensityUtil;
import com.chzh.fitter.view.EditView;
import com.chzh.fitter.view.IEditMode;
import com.chzh.fitter.view.SimpleTitleBar;

public class EditActivity extends BaseActivity {

	private SimpleTitleBar mSimpleTitleBar;

	public static final String BACK_KEY = "data";

	private IEditMode mContentView;

	private TextView mTitleText;

	@Override
	protected void setupViews() {
		// 标题设置
		mSimpleTitleBar = new SimpleTitleBar(this);
		mTitleText = new TextView(this);
		mTitleText.setText("编辑");
		mTitleText.setTextAppearance(this, R.style.white_text_style);
		mTitleText.setTextSize(DensityUtil.dip2px(7));
		mSimpleTitleBar.addMiddleView(mTitleText);
		// 标题按钮
		Button btn = new Button(this);
		btn.setText("提交");
		btn.setBackgroundResource(android.R.color.transparent);
		btn.setTextAppearance(this, R.style.white_text_style);

		mSimpleTitleBar.addRightSideView(btn);
		mSimpleTitleBar.enableOnBackFinish(this);
		mSimpleTitleBar.getBackView().setCompoundDrawablesWithIntrinsicBounds(R.drawable.back_arrow, 0, 0, 0);

		bindClickEvent(btn, "rightClick");
		switchEditMode();
		setDefaultAppBackground();
	}

	/**
	 * 根据点击过来的mode切换不同的编辑界面
	 */
	private void switchEditMode() {
		int mode = getIntent().getIntExtra("edit_mode", 0);
		String lastValue = getIntent().getStringExtra("last_value");
		switch (mode) {
		case UserActivity.EDIT_AGE:
			changeContentView("年龄", lastValue, 18, 80);
			enablePickerType(true);
			break;

		case UserActivity.EDIT_HEIGHT:
			changeContentView("身高", lastValue, 145, 230);
			enablePickerType(true);
			break;

		case UserActivity.EDIT_NICK:
			changeContentView("昵称", lastValue, 0, 0);
			enablePickerType(false);
			break;

		case UserActivity.EDIT_WEIGHT:
			changeContentView("体重", lastValue, 40, 150);
			enablePickerType(true);
			break;

		default:
			TextView tv = new TextView(this);
			tv.setText("NO EDIT MODE");
			initInflater();
			setContentView(mSimpleTitleBar, tv);
			break;
		}
	}

	private void changeContentView(String title, String lastValue, int min, int max) {
		mContentView = new EditView(this, title, lastValue, min, max);
		initInflater();
		setContentView(mSimpleTitleBar, (View)mContentView);
	}
	
	private void enablePickerType(boolean enable) {
		EditView editView = (EditView) mContentView;
		if (enable) {
			visible(editView.getPicker());
			editView.setInitPickerValue();
			gone(editView.getEditText());
		}
	}

	public void rightClick(View v) {
		Intent i = new Intent();
		dynamicExtraValue(i, mContentView.getEditValue());
		setResult(RESULT_OK, i);
		finish();
	}

	/**
	 * 返回不同的数据类型
	 * @param i
	 * @param value
	 */
	private void dynamicExtraValue(Intent i, String value) {
		int mode = getIntent().getIntExtra("edit_mode", 0);
		switch (mode) {
		case UserActivity.EDIT_HEIGHT:
		case UserActivity.EDIT_WEIGHT:
			i.putExtra(BACK_KEY, Float.valueOf(value));
			break;

		case UserActivity.EDIT_AGE:
			i.putExtra(BACK_KEY, Integer.valueOf(value));
			break;

		default:
			i.putExtra(BACK_KEY,value);
			break;
		}
	}



}
