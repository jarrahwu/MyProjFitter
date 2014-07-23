package com.chzh.fitter;

import java.util.HashMap;

import org.json.JSONObject;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;

import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.BaseActivity;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.XUser;
import com.chzh.fitter.util.DataMatcher;
import com.chzh.fitter.util.L;
import com.chzh.fitter.view.SimpleTextTitleBar;
import com.jarrah.json.XSON;
import com.jw.progress.ProgressHUD;

public class LoginActivity extends BaseActivity implements GlobalConstant{

	private SimpleTextTitleBar mTextTitleBar;

	private EditText mAccountEditText;
	private EditText mPasswordEditText;

	@Override
	protected void setupViews() {
		// init a title
		mTextTitleBar = new SimpleTextTitleBar(this);
		mTextTitleBar.setTitleText(getString(R.string.login), Color.WHITE, 18);
		mTextTitleBar.enableOnBackFinish(this);

		initInflater();
		setContentView(mTextTitleBar, R.layout.login_activity);

		mAccountEditText = (EditText) findViewById(R.id.account);
		mPasswordEditText = (EditText) findViewById(R.id.password);

		bindClickEvent(findViewById(R.id.btn_login), "onLoginClicked");
		bindClickEvent(findViewById(R.id.btn_register), "onRegisterClicked");
		bindClickEvent(findViewById(R.id.help), "onHelpClicked");

		setDefaultAppBackground();
	}

	public void onLoginClicked(View v) {
		String account = mAccountEditText.getText().toString().trim();
		String password = mPasswordEditText.getText().toString().trim();

		if (!DataMatcher.getInstance().isPhoneNumberFormat(account)) {
			showToast(getString(R.string.tips_phone));
			return;
		}

		if (!DataMatcher.getInstance().isDataLengthInRange(password, 6, 20)) {
			showToast(getString(R.string.tips_password_length_error));
			return;
		}

		queryLogin(account, password);
	}

	private void queryLogin(String account, String password) {
		
		final ProgressHUD toast = ProgressHUD.show(this, "正在登陆...", true, true, null);
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("mobile", account);
		data.put("password", password);

		JHttpManager.initWithContext(this).post(data , LOGIN_URL, new CodeCallBack() {

			@Override
			public void handleCallBack(JSONObject obj) {
					showToast("登陆成功!");
	        		saveUserInfo(obj);
	        		skipTo(MainActivity.class);
	        		finish();
	        		getFitterApplication().getExistActivity(UserIdActivity.class.getName()).finish();
			}
			
			@Override
			protected void befeoreHandlCallback() {
				toast.dismiss();
			}
			
			@Override
			public void onError(JSONObject obj, int httpCode) {
				super.onError(obj, httpCode);
				System.out.println(obj);
			}
		}, null);
	}

	protected void saveUserInfo(JSONObject obj) {
		UICore core = new UICore(this);
		XUser jUser = new XUser();
		XSON xson = new XSON();
		xson.fromJSON(jUser, obj);
		core.saveAsPreferenceData(jUser, USER_PREFERENCE);
		L.red(core.getToken());
	}

	public void onRegisterClicked(View v) {
		skipTo(RegisterActivity.class);
	}

	public void onHelpClicked(View v) {
	}

}
