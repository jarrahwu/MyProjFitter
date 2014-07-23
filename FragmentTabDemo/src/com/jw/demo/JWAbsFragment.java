package com.jw.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public abstract class JWAbsFragment extends Fragment{
	
	protected View mContentView;
	
	@Override //call only once , because of setRetainInstance()
	public void onCreate(Bundle savedInstanceState) {
		log("onCreate");
		setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log("onCreateView");
		mContentView = mContentView == null ? makeView(inflater, container, savedInstanceState) : mContentView;
		return mContentView;
	}

	public abstract View makeView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState);

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		log("onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		setupViews(savedInstanceState);
	}

	/**
	 * @param savedInstanceState
	 * setup and instantiate your views 
	 */
	protected void setupViews(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			log("view from savedInstanceState");
		}
	}
	
	@Override  //will not call
	public void onDestroy() {
		log("onDestroy");
		super.onDestroy();
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		log("onAttach");
		super.onAttach(activity);
	}
	
	@Override
	public void onDetach() {
		log("onDetach");
		super.onDetach();
	}
	
	public View inflateViewByLayoutId(LayoutInflater inflater, int layoutId) {
		return inflater.inflate(layoutId, null, false);
	}
	
	public void log(String text) {
		android.util.Log.e(getFragmentName(), text);
	}

	public abstract String getFragmentName();
}
