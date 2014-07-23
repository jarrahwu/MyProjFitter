package com.chzh.fitter.util;




import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


/**
 * JSON 工具
 */
public class JSONUtil {

	/**
	 * Jsonarry追加.
	 * @param toArrays
	 * @param fromArrays
	 * @return
	 */
	public static JSONArray append(JSONArray toArrays,JSONArray fromArrays){
		for(int i = 0; i < fromArrays.length(); i++){
			toArrays.put(getJsonObjByIndex(fromArrays,i));
		}
		return toArrays;
	}

	/**
	 * 加对象到top
	 * @param obj
	 * @param jsonArray
	 * @return
	 */
	public static JSONArray appendTop(JSONObject obj, JSONArray jsonArray) {
		JSONArray array = new JSONArray();
		try {
			array.put(0, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < jsonArray.length(); i++) {
			array.put(getJsonObjByIndex(jsonArray,i));
		}
		return array;
	}

	/**
	 * 获取JsonObjet公用方法.
	 * @param obj
	 * @param keyname
	 * @return null if JSONException
	 */
	public static JSONObject getJsonObject(JSONObject obj,String keyname){
		JSONObject jsonObj = null;
		try {
			jsonObj = obj.getJSONObject(keyname);
		} catch (JSONException e) {
			return null;
		}
		return jsonObj;
	}

	/**
	 * 获取JsonArray的公用方法.
	 * @param obj
	 * @param keyname
	 * @return
	 */
	public static JSONArray getJsonArrays(JSONObject obj,String keyname){
		JSONArray jsonArray = null;
		try {
			if(obj != null){
				jsonArray = obj.getJSONArray(keyname);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArray;
	}


	/**
	 * @param array
	 * @param index
	 * @return
	 */
	public static JSONObject getJsonObjByIndex(JSONArray array,int index){
		JSONObject jsonObj = null;
		try {
			jsonObj = array.getJSONObject(index);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

	/**
	 * 通过keyname获取int.
	 * @param obj
	 * @param keyname
	 * @return int.
	 */
	public static int getInt(JSONObject obj,String keyname){
		int res = 0;
		if(obj != null){
			try {
				res = obj.getInt(keyname);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	public static double getDouble(JSONObject obj,String keyname){
		double res = 0;
		if(obj != null){
			try {
				res = obj.getDouble(keyname);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * 通过keyname获取String.
	 * @param obj
	 * @param keyname
	 * @return String.
	 */
	public static String getString(JSONObject obj,String keyname){
		String res = null;
		if(obj != null){
			try {
				res = obj.getString(keyname);
			} catch (JSONException e) {
				Log.e("no json value", e.toString());
				res = "";
			}
		}
		return res;
	}

	/**
	 * 通过keyname获取Boolean.
	 * @param obj
	 * @param keyname
	 * @return String.
	 */
	public static boolean getBoolean(JSONObject obj,String keyname){
		boolean res = false;
		if(obj != null){
			try {
				res = obj.getBoolean(keyname);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	/**
	 * 通过word获取连接.
	 * @param arrays
	 * @param word
	 * @return String.
	 */
	public static String getHrefByRel(JSONArray arrays,String word){
		String href = null;
		if(arrays == null)
			return null;
		for(int i = 0; i < arrays.length(); i++){
			JSONObject link = JSONUtil.getJsonObjByIndex(arrays, i);
			String rel = JSONUtil.getString(link, "rel");
			//modified by jarrah
			int start = rel.lastIndexOf("/") + 1;
			String keyWord = rel.substring(start, rel.length());
			if (keyWord.equals(word)) {
				href = JSONUtil.getString(link, "href");
				break;
			}

		}
		return href;
	}




	/**
	 * 判断是否为空.如果valule = 'null'也是为空.
	 * @param value
	 * @return
	 */
	public static boolean isNull(String value){
		boolean b = false;
		if(value == null || value.equals("null") || value.length() <= 0){
			b = true;
		}
		return b;
	}

	public static boolean isNull(JSONObject value){
		boolean b = false;
		if(value == null || value.equals("null")){
			b = true;
		}
		return b;
	}

	public static JSONObject createJSONObject(String str) {
		JSONObject obj;
		try {
			obj = new JSONObject(str);
		} catch (JSONException e) {
			e.printStackTrace();
			obj = null;
		}
		return obj;
	}

	/**
	 * @param str
	 * @return
	 */
	public static JSONArray createJSONOArray(String str) {
		JSONArray array;
		try {
			array = new JSONArray(str);
		} catch (JSONException e) {
			e.printStackTrace();
			array = null;
		}
		return array;
	}

	/**
	 * JsonArray2String.
	 * @param array
	 * @return
	 */
	public static String JsonArray2String(JSONArray array){
		String res = null;
		if(array != null){
			res = array.toString();
		}
		return res;
	}

	public static JSONArray array2JsonArray(ArrayList<String> arrays) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < arrays.size(); i++) {
			try {
				array.put(i, arrays.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	public static JSONArray array2JsonArray(String[] arrays) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < arrays.length; i++) {
			try {
				array.put(i, arrays[i]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return array;
	}
}
