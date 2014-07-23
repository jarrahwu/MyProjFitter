package com.chzh.fitter.view;

import android.content.Context;
import android.util.AttributeSet;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseView;

public class HeaderButton extends BaseView{

	public HeaderButton(Context context) {
		super(context);
	}

	public HeaderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_header_button);
	}

}
