package com.chzh.fitter;

import net.simonvt.numberpicker.NumberPicker;
import android.view.View;

import com.chzh.fitter.framework.BaseActivity;
import com.chzh.fitter.view.RoundImageView;
import com.jw.progress.ProgressHUD;

public class TestActivity extends BaseActivity{

	NumberPicker mNumberPicker;
	
	@Override
	protected void setupViews() {
		setContentView(R.layout.test);
		mNumberPicker = (NumberPicker) findView(R.id.picker);
		mNumberPicker.setMinValue(20);
		mNumberPicker.setMaxValue(30);
	}
	
}
