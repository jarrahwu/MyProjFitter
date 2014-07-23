package com.chzh.fitter;

import org.json.JSONArray;
import org.json.JSONObject;


import com.chzh.fitter.adapter.PlansAdapter;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.view.BounceListView;

public class GymPlansActivity extends SimpleTitleSActivity implements GlobalConstant{

	private BounceListView mListView;
	private PlansAdapter mAdapter;


	@Override
	protected void setupGUI() {
		mListView = findView(R.id.plan_list, BounceListView.class);
		mAdapter = new PlansAdapter(this);
		mListView.setAdapter(mAdapter);
		queryData();
	}

	private void queryData() {
		String cookie = new UICore(this).getToken();
		JHttpManager httpManager = new JHttpManager(this);
		httpManager.get(GYM_PLAN, new CodeCallBack(this, true) {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				JSONArray array = JSONUtil.getJsonArrays(obj, "elem");
				mAdapter.setData(array);
			}
		}, cookie);
	}


	@Override
	protected String getTitleName() {
		return "健身方案";
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_gym_plans;
	}

}
