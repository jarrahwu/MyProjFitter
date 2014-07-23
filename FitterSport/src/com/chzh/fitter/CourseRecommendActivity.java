package com.chzh.fitter;

import android.view.View;

import com.chzh.fitter.framework.SimpleTitleSActivity;

public class CourseRecommendActivity extends SimpleTitleSActivity {



	@Override
	protected void setupGUI() {
		bindClickEvent(findViewById(R.id.goto_main), "gotoMain");
	}

	public void gotoMain(View v) {
		skipTo(MainActivity.class);
		finish();
	}

	@Override
	protected String getTitleName() {
		return "方案推荐";
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_course_recommend;
	}

}
