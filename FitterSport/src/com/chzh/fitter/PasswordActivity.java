package com.chzh.fitter;

import java.util.HashMap;

import org.json.JSONObject;

import android.view.View;
import android.widget.EditText;

import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.util.DataMatcher;
import com.jw.progress.ProgressHUD;

public class PasswordActivity extends SimpleTitleSActivity {

	private EditText mOldPasswd;
	private EditText mNewPasswd, mNewPasswdRepeat;

	@Override
	protected void setupGUI() {
		mOldPasswd = findView(R.id.old_password, EditText.class);
		mNewPasswd = findView(R.id.password, EditText.class);
		mNewPasswdRepeat = findView(R.id.password_repeat, EditText.class);
		bindClickEvent(findViewById(R.id.btn_save), "onSave");
	}

	@Override
	protected String getTitleName() {
		return "密码安全";
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_password;
	}

	public void onSave(View v) {
		if (validate()) {
			modifyPassword();
		}
	}

	private void modifyPassword() {
		final ProgressHUD progressHUD = ProgressHUD.show(this, "正在提交数据...", true, true, null);
		
		String newPassword = mNewPasswd.getText().toString().trim();
		String cookie = new UICore(this).getToken();
		JHttpManager httpManager = new JHttpManager(this);
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		data.put("password", newPassword);
		
		httpManager.post(data , GlobalConstant.USER_MODIFY, new CodeCallBack() {
			
			@Override
			protected void befeoreHandlCallback() {
				progressHUD.dismiss();
			}
			
			@Override
			public void handleCallBack(JSONObject obj) {
				showToast("修改密码成功");
			}
		}, cookie);
	}

	private boolean validate() {
		String newPassword = mNewPasswd.getText().toString().trim();
		String oldPassword = mOldPasswd.getText().toString().trim();
		
		// 检查旧密码的长度
		if (!DataMatcher.getInstance().isDataLengthInRange(oldPassword, 6, 20)) {
			showToast(getString(R.string.tips_password_length_error));
			return false;
		}
		
		// 检查密码的长度
		if (!DataMatcher.getInstance().isDataLengthInRange(
				newPassword, 6, 20)) {
			showToast(getString(R.string.tips_password_length_error));
			return false;
		}

		String passwordRepeat = mNewPasswdRepeat.getText().toString()
				.trim();
		// 检查重复密码的长度
		if (!DataMatcher.getInstance().isDataLengthInRange(passwordRepeat, 6,
				20)) {
			showToast(getString(R.string.tips_password_repeat_length_error));
			return false;
		}

		// 检查两次密码是否相同
		if (!passwordRepeat.equals(newPassword)) {
			showToast(getString(R.string.password_not_equals));
			return false;
		}
		return true;
	}

}
