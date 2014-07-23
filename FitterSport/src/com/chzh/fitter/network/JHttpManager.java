package com.chzh.fitter.network;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.chzh.fitter.util.JSONUtil;

/**
 * AQ HTTP 的各种请求调用
 */
public class JHttpManager {

	private AQuery mAq;

	AQAjaxCallBack mAQAjaxCallBack;

	public JHttpManager(Context context) {
		mAq = new AQuery(context);
	}

	public static JHttpManager mInstance;

	/**
	 * 初始化
	 * @param context
	 * @return
	 */
	public synchronized static JHttpManager initWithContext(Context context) {
		if (mInstance == null) {
			mInstance = new JHttpManager(context);
		}
		return mInstance;
	}

	/**
	 * 调用AQ的post方法
	 * @param data
	 * @param url
	 * @param cb
	 */
	public void post(HashMap<String, Object> data, String url, final APICallBack cb, String cookie) {

		JSONObject object = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		StringEntity entity;

		try {
			object = genParams(object, data);
			entity = new StringEntity(object.toString(), "utf-8");
			params.put(AQuery.POST_ENTITY, entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		AQAjaxCallBack ajaxCallBack = instantiateCallBackWith(cb);

		if (cookie != null) ajaxCallBack.cookie("accesstoken", cookie);

		mAq.ajax(url, params, JSONObject.class, ajaxCallBack);
	}

	/**
	 * multipart 传文件
	 * @param url
	 * @param data map key为传的字段名, value 是文件路径
	 * @param cookie
	 * @param cb
	 */
	public void multiPart(String url, HashMap<String, String> data, String cookie, APICallBack cb) {
		Map<String, Object> params = new HashMap<String, Object>();
		Iterator<Entry<String, String>> iter = data.entrySet().iterator();

		//put file
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
			params.put(entry.getKey(), new File(entry.getValue()));
		}

		AQAjaxCallBack ajaxCallBack = instantiateCallBackWith(cb);

		if (cookie != null) ajaxCallBack.cookie("accesstoken", cookie);

		mAq.ajax(url, params, JSONObject.class, ajaxCallBack);

	}

	/**
	 * 调用AQ的get方法
	 * @param url
	 * @param cb
	 */
	public void get(String url, final APICallBack cb, String cookie) {
		AQAjaxCallBack ajaxCallBack = instantiateCallBackWith(cb);

		if(cookie != null) ajaxCallBack.cookie("accesstoken", cookie);

		mAq.ajax(url, JSONObject.class, ajaxCallBack);
	}

	/**
	 * 调用AQ的get方法
	 * @param url
	 * @param cb
	 */
	public void getFile(String url, AjaxCallback<File> cb) {
			mAq.ajax(url, File.class, cb);
	}

	public void put(HashMap<String, Object> data, String url, final APICallBack cb, String cookie) {

		JSONObject object = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		StringEntity entity = null;
		try {
			object = genParams(object, data);
			entity = new StringEntity(object.toString(), "utf-8");
			params.put(AQuery.POST_ENTITY, entity);
		} catch (UnsupportedEncodingException e) {
}
		if(cookie != null)
			mAq.put(url, "application/json", entity, JSONObject.class, new AQAjaxCallBack(cb).header("cookie", "token=" + cookie));
		else
			mAq.put(url, "application/json", entity, JSONObject.class, new AQAjaxCallBack(cb));
	}

	public void delete(String url, final APICallBack cb, String token) {

		HttpDelete request = new HttpDelete(url);
		HttpClient client = new DefaultHttpClient();

		request.addHeader("Cookie", "accesstoken=" + token);
		try {
			HttpResponse response = client.execute(request);
			int code = response.getStatusLine().getStatusCode();
			if (code >= 200 && code < 300) {
				cb.callBack(new JSONObject());
			} else {
				cb.onError(new JSONObject(), code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 把参数放进jsonobject里面并作为http参数调用网络借口
	 * @param obj 需要放参数的jsonobject
	 * @param data 参数
	 * @return 放好参数的json object
	 */
	private JSONObject genParams(JSONObject obj,HashMap<String, Object> data){
		//遍历map 并 设置参数
		Iterator<Entry<String, Object>> iter = data.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();
			try {
				obj.put(entry.getKey(), entry.getValue());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}


	/**
	 * @author jarrahwu
	 * 将AQ的callback进一步封装,不用在处理code的各种情况
	 */
	private class AQAjaxCallBack extends AjaxCallback<JSONObject> {

		private APICallBack cb;

		public AQAjaxCallBack(APICallBack cb) {
			this.cb = cb;
		}

		public AQAjaxCallBack() {

		}

		public AQAjaxCallBack setCallBack(APICallBack cb) {
			this.cb = cb;
			return this;
		}

		@Override
		public void callback(String url, JSONObject object, AjaxStatus status) {
			if (status.getCode() >= 200 && status.getCode() < 300) {
				cb.callBack(object);
			} else {
				cb.onError(JSONUtil.createJSONObject(status.getError() != null ? status.getError() : "{}"),status.getCode());
			}
			super.callback(url, object, status);
		}
	}

	/**
	 * 实例化一个回调
	 * @param cb
	 * @return
	 */
	private AQAjaxCallBack instantiateCallBackWith(APICallBack cb) {
		if (mAQAjaxCallBack == null) {
			mAQAjaxCallBack = new AQAjaxCallBack();
		}
		return mAQAjaxCallBack.setCallBack(cb);
	}
}
