package com.chzh.fitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;

import com.chzh.fitter.adapter.CustomScheduleAdapter;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.view.BounceListView;

public class QuestionActivity extends SimpleTitleSActivity{

	private BounceListView mBounceListView;
	private CustomScheduleAdapter mAdapter;

	@Override
	protected void setupGUI() {
		mBounceListView = findView(R.id.list, BounceListView.class);
		mAdapter = new CustomScheduleAdapter(this);
		mBounceListView.setAdapter(mAdapter);
		mAdapter.setData(getData());
		bindClickEvent(findView(R.id.ok), "onOkClick");
	}

	private JSONArray getData() {
		JSONArray array = new JSONArray();
		array.put(genObj(1, "您是否有眼袋 ?"));
		array.put(genObj(2, "您是否经常出现黑眼圈 ?"));
		array.put(genObj(3, "是否吃完饭就犯困 ?"));
		array.put(genObj(1, "是否腰围增大 ?"));
		array.put(genObj(1, "鼻子是否爱出汗 ?"));
		return array;
	}

	private JSONObject genObj(int id, String title){
		JSONObject obj0 = new JSONObject();
		try {
			obj0.put("question_id", id);
			obj0.put("question_title", title);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj0;
	}



	@Override
	protected String getTitleName() {
		return "私人定制";
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_custom_schedule;
	}

	public void onOkClick(View v) {
		skipTo(CourseRecommendActivity.class);
		finish();
	}

}
