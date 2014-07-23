package com.chzh.fitter.util;

import android.util.Log;

public class L {
	
	public static void red(Object obj) {
		Log.e("XLog", obj.toString());
	}
	
	public static void yellow(Object obj) {
		Log.w("XLog", obj.toString());
	}
	
	public static void red(Class<?> cls, Object obj) {
		Log.e(cls.getName(), obj.toString());
	}
	
	public static void tagNull(Class<?> cls, Object... obj) {
		for (int i = 0; i < obj.length; i++) {
			String varName = obj[i] == null ? "null" : obj[i].getClass().getName();
			Log.e(cls.getName() + "var : " + varName +" is null ? " , obj[i] == null ? "YES" : "NO");
		} 
	}
	
}
