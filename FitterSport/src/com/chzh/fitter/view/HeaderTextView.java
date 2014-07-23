package com.chzh.fitter.view;

import android.content.Context;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseView;

public class HeaderTextView extends BaseView{

	public HeaderTextView(Context context) {
		super(context);
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_course_summary_list_header);
	}
	
}
