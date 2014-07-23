package com.chzh.fitter.view;

import android.content.Context;
import android.view.View;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseView;

public class SingedPopupView extends BaseView {

	public SingedPopupView(Context context) {
		super(context);
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_popup_signed);
	}

	public View getCloseView() {
		return findView(R.id.close);
	}

}
