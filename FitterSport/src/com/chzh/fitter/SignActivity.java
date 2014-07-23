package com.chzh.fitter;

import org.json.JSONObject;

import android.text.SpannableString;
import android.view.View;

import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.BasePopupWindow;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.ShakeListener;
import com.chzh.fitter.util.ShakeListener.OnShakeListener;
import com.chzh.fitter.view.InfoTextView;
import com.chzh.fitter.view.SingedPopupView;
import com.jw.spannabletext.SpannableStringUtil;

public class SignActivity extends SimpleTitleSActivity implements GlobalConstant, OnShakeListener{

	private BasePopupWindow mSignPopupWindow;
	private SingedPopupView mSignPopupView;
	private ShakeListener shakeListener;

	@Override
	protected void setupGUI() {
		mSignPopupView = new SingedPopupView(this);
		mSignPopupWindow = new BasePopupWindow(mViewContainer,mSignPopupView);
		setData();
		listenShake();
		
	}

	private void listenShake() {
		shakeListener = new ShakeListener(this);
		shakeListener.setOnShakeListener(this);
	}

	/**
	 * 拉取数据
	 */
	private void setData() {
		JHttpManager httpManager = new JHttpManager(this);
		String cookie = new UICore(this).getToken();
		httpManager.get(USER_SIGN, new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				String signTotal = JSONUtil.getString(obj, "total");
				String todayScore = JSONUtil.getString(obj, "today");
				String signContinue = JSONUtil.getString(obj, "continue");
				
				SpannableStringUtil util = new SpannableStringUtil();
				SpannableString boldSignTotal = util.getBoldSpannableString(signTotal);
				SpannableString boldTodayScore = util.getBoldSpannableString(todayScore);
				SpannableString boldSignContinue = util.getBoldSpannableString(signContinue);
				
				SpannableString scoreUnit = util.getSizeSpannableString(" 分", 55);
				SpannableString dayUnit = util.getSizeSpannableString(" 天", 55);
				
				findView(R.id.signed, InfoTextView.class).setRightTextSpannable(boldSignTotal, 25).append(dayUnit);
				findView(R.id.curent_socre, InfoTextView.class).setRightTextSpannable(boldTodayScore, 25).append(scoreUnit);
				findView(R.id.continuous_signed, InfoTextView.class).setRightTextSpannable(boldSignContinue, 25).append(dayUnit);
			}
		}, cookie);
	}

	public void dismissPopup(View v) {
		if (mSignPopupWindow != null) {
			mSignPopupWindow.dismiss();
		}
	}


	@Override
	protected String getTitleName() {
		return "签到";
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_sign;
	}

	@Override
	public void onShake() {
		querySign();
	}

	private synchronized void querySign() {
		JHttpManager httpManager = new JHttpManager(this);
		String url = GlobalConstant.USER_SIGN_ADD;
		String cookie = new UICore(this).getToken();
		httpManager.get(url, new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				//弹出popup
				mSignPopupWindow.showAtParentCenter();
				bindClickEvent(mSignPopupView.getCloseView(), "dismissPopup");
				
//				shakeListener.start();
				
			}
			
//			@Override
//			protected void befeoreHandlCallback() {
//				shakeListener.stop();
//			}
		
			@Override
			protected void handleSpecialCode(int code) {
				if (code == 10010) {
					showToast(R.layout.toast_signed);
				}
			}
			
		}, cookie);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		shakeListener.stop();
	}

}
