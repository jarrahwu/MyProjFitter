package com.chzh.fitter.data;


import com.chzh.fitter.util.L;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

/**
 * SharedPreferences 帮助器
 *
 */
public class SharedPreferencesHelper implements SharedPreferencesDef{

	private Context mContext;

	private SharedPreferences mSharedPreferences;

	public SharedPreferencesHelper(Context context) {
		mContext = context;
	}

	public void openOrCreateSharedPreferences(String name) {
		openOrCreateSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public void openOrCreateSharedPreferences(String name, int mode) {
		mSharedPreferences = mContext.getSharedPreferences(name, mode);
	}

	/**
	 * 保存整个完整的数据
	 * @param data
	 */
	public void save(AbstractPreferencesData data) {
		if (mSharedPreferences == null) {
			throw new RuntimeException("you must create SharedPreferences first!");
		}
		Editor editor = mSharedPreferences.edit();

		String[] varNames = data.getVarNames();
		Class<?>[] varTypes = data.getVarTypes();
		Object[] varValues = data.getVarValues();

		for (int i = 0; i < varValues.length; i++) {

			String key = varNames[i];

			  if(varTypes[i] == Integer.TYPE) {   //int
				  editor.putInt(key, Integer.parseInt(varValues[i].toString()));

              }else if(varTypes[i] == String.class){   //String
            	  editor.putString(key, varValues[i].toString());

              }else if(varTypes[i] == Long.TYPE){   //long
            	  editor.putLong(key, Long.parseLong(varValues[i].toString()));

              }else if(varTypes[i] == Float.TYPE){   //float
            	  editor.putFloat(key, Float.parseFloat(varValues[i].toString()));

              }else if(varTypes[i] == Boolean.TYPE){   //boolean
            	  editor.putBoolean(key, Boolean.parseBoolean(varValues[i].toString()));

              }else {
				throwException();
			}
		}

		editor.commit();
	}

	/**
	 * 根据提供的变量获的类型获取数据，封装为一个bundle
	 * @param data 用来保存到sharedPreference里面的数据实例
	 * @return
	 */
	public Bundle get(AbstractPreferencesData data) {

		String[] varNames = data.getVarNames();
		Class<?>[] varTypes = data.getVarTypes();

		Bundle retData = new Bundle();

		for (int i = 0; i < varNames.length; i++) {

			String key = varNames[i];

			if (varTypes[i] == Integer.TYPE) { // int
				int value = mSharedPreferences.getInt(key, DEFALUT_VALUE);
				retData.putInt(key, value);

			} else if (varTypes[i] == String.class) { // String
				String value = mSharedPreferences.getString(key, DEFALUT_STRING);
				retData.putString(key, value);

			} else if (varTypes[i] == Long.TYPE) { // long
				long value = mSharedPreferences.getLong(key, DEFALUT_VALUE);
				retData.putLong(key, value);

			} else if (varTypes[i] == Float.TYPE) { // float
				float value = mSharedPreferences.getFloat(key, DEFALUT_VALUE);
				System.out.println(key + " : " + value);
				retData.putFloat(key, value);

			} else if (varTypes[i] == Boolean.TYPE) { // boolean
				boolean value = mSharedPreferences.getBoolean(key, DEFALUT_BOOL);
				retData.putBoolean(key, value);

			} else {
				throwException();
			}
		}
		return retData;
	}

	/**
	 * 移除指定的sharedPreferences文件
	 * @param preferenceName
	 */
	public void clear() {
		mSharedPreferences.edit().clear().commit();
	}

	/**
	 * 修改一个键的值
	 * @param key 键
	 * @param value 支持的类型，其他的不能识别
	 */
	public void modify(String key, Object value) {
		L.red("modified key : " + key + " and value is :" + value);
		if (value instanceof Integer) {
			putInt(key, (Integer) value);

		}else if (value instanceof Float) {
			putFloat(key, (Float) value);

		}else if (value instanceof Long) {
			putLong(key, (Long) value);

		}else if(value instanceof String) {
			putString(key, (String) value);

		}else if (value instanceof Boolean) {
			putBoolean(key, (Boolean) value);
		}else {
			throwException();
		}
	}

	private void putInt(String key, int value) {
		mSharedPreferences.edit().putInt(key, value).commit();
	}

	private void putFloat(String key, float value) {
		mSharedPreferences.edit().putFloat(key, value).commit();
	}

	private void putLong(String key, long value) {
		mSharedPreferences.edit().putLong(key, value).commit();
	}

	private void putString(String key, String value) {
		mSharedPreferences.edit().putString(key, value).commit();
	}

	private void putBoolean(String key, boolean value) {
		mSharedPreferences.edit().putBoolean(key, value).commit();
	}

	/**
	 * 抛出类型不支持
	 */
	private void throwException() {
		throw new RuntimeException(
				"DATA TYPE NOT ALLOW! int String long float boolean support only");
	}
}
