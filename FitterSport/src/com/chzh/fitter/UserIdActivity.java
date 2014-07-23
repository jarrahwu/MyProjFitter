package com.chzh.fitter;

import java.util.HashMap;

import org.json.JSONObject;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import com.chzh.fitter.core.UICore;
import com.chzh.fitter.data.SharedPreferencesDef;
import com.chzh.fitter.framework.BaseActivity;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.XUser;
import com.chzh.fitter.util.L;
import com.jarrah.json.XSON;
import com.jw.progress.ProgressHUD;

public class UserIdActivity extends BaseActivity implements GlobalConstant, OnPreparedListener, OnCompletionListener{

	private VideoView mVideoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		removeOriginalTitle();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void setupViews() {
		finishIfUserExisted();
		
		setContentView(R.layout.activity_user_id);
		bindClickEvent(findView(R.id.id_fitter), "useFitterLogin");
		bindClickEvent(findView(R.id.id_visitor), "useVisitorLogin");
		bindClickEvent(findView(R.id.register), "register");
		
		mVideoView = findView(R.id.bg_video, VideoView.class);
		Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.flash);
		mVideoView.setVideoURI(uri);
		mVideoView.setOnCompletionListener(this);
		mVideoView.setOnPreparedListener(this);
		
//		skipTo(CourseCommentActivity.class);
	}

	private void finishIfUserExisted() {
		XUser userInfo = new UICore(this).getUserFromPreference();
		if (!userInfo.getToken().equals(SharedPreferencesDef.DEFALUT_STRING)) {
			skipTo(MainActivity.class);
			finish();
		}
	}

	public void useFitterLogin(View v) {
		skipTo(LoginActivity.class);
	}

	public void useVisitorLogin(View v) {
		//dialog
		final ProgressHUD toast = ProgressHUD.show(this, "以游客身份登陆fitter.", true, true, null);
		
		JHttpManager httpManager = new JHttpManager(this);
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("mac", new UICore(this).getMacAddress());
		httpManager.post(data , VISITOR_LOGIN_URL, new CodeCallBack() {
			
			@Override
			protected void befeoreHandlCallback() {
				toast.dismiss();
			}
			
			@Override
			public void handleCallBack(JSONObject obj) {
				L.red(obj);
				showToast("登陆成功.");
				saveUser(obj);
				finish();
				skipTo(MainActivity.class);
			}
		}, null);

	}

	private void saveUser(JSONObject obj) {
		UICore core = new UICore(this);
		XUser jUser = new XUser();
		XSON xson = new XSON();
		xson.fromJSON(jUser, obj);
		core.saveAsPreferenceData(jUser, USER_PREFERENCE);
		L.red(core.getToken());
	}

	public void register(View v) {
		skipTo(RegisterActivity.class);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mVideoView.start();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mVideoView.start();
	}
}
