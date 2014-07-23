package com.chzh.fitter;

import org.json.JSONObject;

import android.view.View;

import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.jw.progress.ProgressHUD;

public class AppSettingActivity extends SimpleTitleSActivity{
	
	@Override
	protected void setupGUI() {
		bindClickEvent(findView(R.id.logout), "onLogout");
	}

	@Override
	protected String getTitleName() {
		return "设置";
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_app_settting;
	}
	
	public void onLogout(View v) {
		final ProgressHUD progressHUD = ProgressHUD.show(this, "正在注销...", true, true, null);
		String cookie = new UICore(this).getToken();
		JHttpManager httpManager = new JHttpManager(this);
		httpManager.get(GlobalConstant.LOGOUT_URL, new CodeCallBack() {
			
			@Override
			protected void befeoreHandlCallback() {
				progressHUD.dismiss();
			}
			
			@Override
			public void handleCallBack(JSONObject obj) {
				showToast("已注销.");
				new UICore(mContext).clearUserInfo();
				setResult(RESULT_OK);
				finish();
				skipTo(MainActivity.class);
			}
		}, cookie);
	}
}
