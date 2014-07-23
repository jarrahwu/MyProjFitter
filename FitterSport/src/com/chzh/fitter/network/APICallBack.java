package com.chzh.fitter.network;



import org.json.JSONObject;

/**
 *	api回调
 */
public interface APICallBack {

	/**
	 * 成功回调
	 * @param obj
	 */
	public void callBack(JSONObject obj);

	/**
	 * 出错
	 * @param httpCode
	 */
	public void onError(JSONObject obj, int httpCode);
}
