package com.chzh.fitter.network;


import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.FitterApplication;
import com.chzh.fitter.util.JSONUtil;
import com.jw.progress.ProgressHUD;

/**
 * 普通的Tapi回调，已经处理好失败的情况
 */
public abstract class CodeCallBack implements APICallBack {

	protected Context mContext;
	private ProgressHUD mProgress;


	public CodeCallBack() {
		mContext = FitterApplication.getInstance().getApplicationContext();	
	}
	
	public CodeCallBack(Context context, boolean isProgress) {
		mContext = context;
		if(isProgress)
			mProgress = ProgressHUD.show(mContext, "Loading...", false, true, null);
	}

	public void callBack(JSONObject obj) {
		befeoreHandlCallback();
		if(obj.isNull("code")) {
			System.out.println(obj);
			throw new RuntimeException("call back code is null! server error.");
		}

		int code = JSONUtil.getInt(obj, "code");

		switch (code) {

		case 0://正常情况
			handleCallBack(obj);
			break;
			
		case 1://正常处理数据,额外要处理邀请注册
			break;

		default: //返回错误代码
			handleSpecialCode(code);
			String msg = JSONUtil.getString(obj, "msg");
			showMsg(mContext, msg);
			break;
		}

	}


	public void onError(JSONObject obj,int httpCode) {
		if (httpCode == 403) {
			showMsg(mContext, mContext.getString(R.string.Auth_fail));
		} else if (httpCode == -101) {
			//如果多次-101请检查AndroidManifest有没有网络权限
			showMsg(mContext, mContext.getString(R.string.connect_fail));
		}else if(httpCode >= 500 && httpCode < 600){
			showMsg(mContext, mContext.getString(R.string.exception_try));
		}else {
			showMsg(mContext, "unknow http error. code : " + httpCode);
		}
		Log.e("error http code : ", ""+httpCode);
	}

	private void showMsg(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	protected void befeoreHandlCallback() {
		if (mProgress != null) {
			mProgress.dismiss();
		}
	}
	
	protected void handleSpecialCode(int code){
		
	}

	/**
	 * 处理回调
	 * @param obj
	 */
	public abstract void handleCallBack(JSONObject obj);



}
