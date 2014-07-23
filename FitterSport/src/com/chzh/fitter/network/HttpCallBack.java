package com.chzh.fitter.network;

import org.json.JSONObject;

public interface HttpCallBack {
	void onCallBack(JSONObject obj);
	void onError(JSONObject obj, int errorCode);
}
