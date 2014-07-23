package com.chzh.fitter;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.graphics.Color;
import android.text.SpannableString;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.BaseActivity;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.RegData;
import com.chzh.fitter.struct.XUser;
import com.chzh.fitter.util.DataMatcher;
import com.chzh.fitter.util.L;
import com.chzh.fitter.view.SimpleTextTitleBar;
import com.jw.progress.ProgressHUD;
import com.jw.spannabletext.SpannableStringUtil;

/**
 * 用户注册的Activity
 */
public class RegisterActivity extends BaseActivity implements GlobalConstant{

	private SimpleTextTitleBar mTextTitleBar;

	private EditText mPhoneNumEditText;
	private EditText mPasswordEditText;
	private EditText mPasswordRepeatEditText;
	private EditText mMsgAuthEditText;

	@Override
	protected void setupViews() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
		//init a title
		mTextTitleBar = new SimpleTextTitleBar(this);
		mTextTitleBar.setTitleText(getString(R.string.register), Color.WHITE, 18);
		mTextTitleBar.enableOnBackFinish(this);

		initInflater();
		setContentView(mTextTitleBar, R.layout.register_activity);
		setDefaultAppBackground();

		mPhoneNumEditText = (EditText) findViewById(R.id.phone_num);
		mPasswordEditText = (EditText) findViewById(R.id.password);
		mPasswordRepeatEditText = (EditText) findViewById(R.id.password_repeat);
		mMsgAuthEditText = (EditText) findViewById(R.id.msg_auth_code);

		bindClickEvent(findViewById(R.id.complete), "onCompleteClicked");
		bindClickEvent(findViewById(R.id.btn_send_auth), "onSendAuthClicked");
	}


	/**
	 * 获取注册信息
	 * @return
	 */
	public RegData getRegisterData() {
		String phoneNum = mPhoneNumEditText.getText().toString().trim();
		String password = mPasswordEditText.getText().toString().trim();
		String msgAuthNum = mMsgAuthEditText.getText().toString().trim();

		RegData regData = new RegData(phoneNum, password, msgAuthNum);
		return regData;
	}

	public void onSendAuthClicked(View v) {
		
		if(DataMatcher.getInstance().isStringEmpty(getRegisterData().getPhoneNum())) {
			showToast("请填写手机号码.");
			return;
		}

		if (!DataMatcher.getInstance().isPhoneNumberFormat(getRegisterData().getPhoneNum())) {
			showToast("请填写正确的手机号码.");
			return;
		}

		queryAuthCode();
	}

	private void queryAuthCode() {
		showButtonCodeDelay();
		//发送验证码请求
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("mobile", getRegisterData().getPhoneNum());
		JHttpManager.initWithContext(this).post(data , VCODE_URL, new CodeCallBack() {

			@Override
			public void handleCallBack(JSONObject obj) {
				showToast("已发送验证码!" + obj);
			}
		}, null);
	}
	
	
	static int _count = 60;
	final String _text = "发送验证码";
	private void showButtonCodeDelay() {
		final Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						_count --;
						SpannableStringUtil spannableStringUtil = new SpannableStringUtil();
						SpannableString text = spannableStringUtil.getSizeSpannableString("(" + _count +"s后重发验证码)", 30);
						findView(R.id.btn_send_auth, Button.class).setText(_text);
						findView(R.id.btn_send_auth, Button.class).append(text);
						findView(R.id.btn_send_auth, Button.class).setEnabled(false);
						if(_count == 0)
						{
							findView(R.id.btn_send_auth, Button.class).setText(_text);
							findView(R.id.btn_send_auth, Button.class).setEnabled(true);
							_count = 60;
							timer.cancel();
						}
					}
				});
			}
		}, 1000, 1000);
	}


	public void onCompleteClicked(View v) {
		RegData regData = getRegisterData();
		//检查账号格式
		if (!DataMatcher.getInstance().isPhoneNumberFormat(regData.getPhoneNum())) {
			showToast(getString(R.string.tips_phone_format_error));
			return;
		}
		//检查密码的长度
		if (!DataMatcher.getInstance().isDataLengthInRange(regData.getPassword(), 6, 20)) {
			showToast(getString(R.string.tips_password_length_error));
			return;
		}

		String passwordRepeat = mPasswordRepeatEditText.getText().toString().trim();
		//检查重复密码的长度
		if (!DataMatcher.getInstance().isDataLengthInRange(passwordRepeat , 6, 20)) {
			showToast(getString(R.string.tips_password_repeat_length_error));
			return;
		}

		//检查两次密码是否相同
		if (!passwordRepeat.equals(regData.getPassword())) {
			showToast(getString(R.string.password_not_equals));
			return;
		}

		//这里要加入短信验证码的验证
		if(DataMatcher.getInstance().isStringEmpty(regData.getMsgAuthNum())) {
			showToast(getString(R.string.tips_msg_auth_code));
			return;
		}

		//是否勾选用户协议
		if (!isAgreeAppInfo()) {
			showToast(getString(R.string.tips_agree_app_info_error));
			return;
		}

		queryRegister(regData);
	}


	private boolean isAgreeAppInfo() {
		CheckBox cb = (CheckBox) findViewById(R.id.agree);
		return cb.isChecked();
	}


	private void queryRegister(RegData regData) {
		
		final ProgressHUD toast = ProgressHUD.show(this, "正在提交注册内容...", true, true, null);
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("mobile", regData.getPhoneNum());
		data.put("password", regData.getPassword());
		data.put("code", regData.getMsgAuthNum());
		JHttpManager.initWithContext(this).post(data , REG_URL, new CodeCallBack() {
			
			@Override
			protected void befeoreHandlCallback() {
				toast.dismiss();
			}
			
			@Override	
			public void handleCallBack(JSONObject obj) {
				showToast("注册成功!");
				L.red(obj);
				saveUserInfo(obj);
				skipTo(MainActivity.class);
				finish();
			}
		}, null);
	}


	/**
	 * save userInfo with sharedPreference
	 * @param obj callback user json
	 */
	private void saveUserInfo(JSONObject obj) {
		UICore core = new UICore(this);
		XUser user = new XUser();
		core.initDataWithJSON(user, obj);
		core.saveAsPreferenceData(user, USER_PREFERENCE);
	}
}
