package com.chzh.fitter.framework;

import java.util.HashMap;

import android.app.Activity;
import android.app.Application;

/**
 *	main application
 */
public class FitterApplication extends Application{

	private static FitterApplication mAppInstance;

	@Override
	public void onCreate() {

		//init application
		if(mAppInstance == null)
			mAppInstance = this;

		if(existActivityMap == null)
			existActivityMap = new HashMap<String, Activity>();
		
		super.onCreate();
	}

	public static synchronized FitterApplication getInstance() {
		return mAppInstance;
	}
	
	private HashMap<String, Activity> existActivityMap;
	
	public void addExistActivity(String actName, Activity activity) {
		if (!existActivityMap.containsKey(actName)) {
			existActivityMap.put(actName, activity);
		}
	}
	
	public void removeAcitity(String actName) {
		if (existActivityMap.containsKey(actName)) {
			existActivityMap.remove(actName);
		}
	}
	
	public Activity getExistActivity(String actName) {
		return existActivityMap.get(actName);
	}
}
