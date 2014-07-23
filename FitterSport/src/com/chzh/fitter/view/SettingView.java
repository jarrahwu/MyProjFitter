package com.chzh.fitter.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

import com.chzh.fitter.AppSettingActivity;
import com.chzh.fitter.InfoDetailActivity;
import com.chzh.fitter.MainActivity;
import com.chzh.fitter.PasswordActivity;
import com.chzh.fitter.R;
import com.chzh.fitter.SignActivity;
import com.chzh.fitter.UserActivity;
import com.chzh.fitter.framework.BaseView;

/**
 * 首页左上角点击的popup
 *
 */
public class SettingView extends BaseView{

	public SettingView(Context context) {
		super(context);
	}

	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void gotoSign(View v) {
		skipTo(SignActivity.class);
		closePopupWindow();
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_settting);
		bindClickEvent(findView(R.id.sign), "gotoSign");
		bindClickEvent(findView(R.id.scores), "gotoScores");
		bindClickEvent(findView(R.id.setting), "gotoAppSetting");
		bindClickEvent(findView(R.id.password), "gotoPassword");
		bindClickEvent(findView(R.id.user_info), "gotoUserInfo");
	}
	
	public void gotoScores(View v) {
		Intent i = new Intent();
		i.setClass(mContext, InfoDetailActivity.class);
		i.putExtra("info_type", InfoDetailActivity.TYPE_SCORE);
		i.putExtra("title", "积分查询");
		mContext.startActivity(i);
		closePopupWindow();
	}

	private void closePopupWindow() {
		// dismiss popup
		PopupWindow window = (PopupWindow) getTag();
		if (window != null) {
			window.dismiss();
		}
	}
	
	public void gotoAppSetting(View v) {
		Intent i = new Intent();
		i.setClass(mContext, AppSettingActivity.class);
		Activity main = (Activity) mContext;
		main.startActivityForResult(i, MainActivity.LOGOUT);
		closePopupWindow();
	}
	
	public void gotoPassword(View v) {
		skipTo(PasswordActivity.class);
		closePopupWindow();
	}
	
	public void gotoUserInfo(View v) {
		skipTo(UserActivity.class);
		closePopupWindow();
	}

}
