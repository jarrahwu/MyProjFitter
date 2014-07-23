package com.chzh.fitter;

import com.chzh.fitter.download.ActionDownView;
import com.chzh.fitter.framework.BaseActivity;
import com.chzh.fitter.struct.CourseActionData;
import com.chzh.fitter.struct.PlayListData;

public class ActionDownloadActivity extends BaseActivity{
	
	private  ActionDownView mContentView;
	
	@Override
	protected void setupViews() {
		removeOriginalTitle();
		CourseActionData data = (CourseActionData) getIntent().getSerializableExtra("down_data");
		if (data != null) {
			mContentView = new ActionDownView(this, data);
		}
		
		PlayListData playListData = (PlayListData) getIntent().getSerializableExtra("play_list");
		if (playListData != null) {
			mContentView = new ActionDownView(this, playListData.getList());
		}
		
		if (mContentView == null) {
			showToast("缺少参数下载的数据");
			finish();
			return;
		}
		
		setContentView(mContentView);
	}
}
