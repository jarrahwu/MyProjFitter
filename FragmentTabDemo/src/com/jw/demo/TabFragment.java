package com.jw.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

@SuppressLint("ValidFragment")
public class TabFragment extends JWAbsFragment {

	private String mTitle;
	private int mImageRes;

	public TabFragment() {

	}

	public TabFragment(String title, int imageRes) {
		mTitle = title;
		mImageRes = imageRes;
	}

	@Override
	public View makeView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log("make view  :" + mContentView);
		return inflateViewByLayoutId(inflater, R.layout.fragment_test);
	}

	@Override
	protected void setupViews(Bundle savedInstanceState) {
		ImageView img = (ImageView) getActivity().findViewById(R.id.image);
		img.setImageResource(mImageRes);
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), MainActivity.class));
			}
		});
	}

	public String getTitle() {
		return mTitle;
	}

	@Override
	public String getFragmentName() {
		return mTitle;
	}

}
