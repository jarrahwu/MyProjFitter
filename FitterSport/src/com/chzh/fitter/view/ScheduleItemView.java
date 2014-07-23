package com.chzh.fitter.view;

import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzh.fitter.CourseSummaryActivity;
import com.chzh.fitter.R;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.CourseSummaryData;
import com.chzh.fitter.struct.SelfPlanData;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;
import com.jarrah.json.XSON;

public class ScheduleItemView extends BaseDataItemView{

	public ScheduleItemView(Context context) {
		super(context);
	}

	public ScheduleItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_item_schedule);
	}

	@Override
	public void onDispatchData(JSONObject data) {
		L.red("今天要做的事: " + data);
		SelfPlanData selfPlanData = new SelfPlanData();
		selfPlanData = new XSON().fromJSON(selfPlanData, data);
		
		findView(R.id.content, TextView.class).setText(selfPlanData.getTitle());
		ajaxImage(findView(R.id.icon, ImageView.class), GlobalConstant.HOST_IP + selfPlanData.getImg());
		findView(R.id.time, TextView.class).setText(selfPlanData.getTime());
		
		this.setTag(selfPlanData);
		bindClickEvent(this, "gotoCourseSummary");
	}
	
	public void gotoCourseSummary(View v) {
		SelfPlanData selfPlanData = (SelfPlanData) v.getTag();
		//请求url 获取courseSummary的数据
		JHttpManager httpManager = new JHttpManager(mContext);
		String cookie = new UICore(mContext).getToken();
		httpManager.get(selfPlanData.getCourseSummaryUrl(), new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				L.red("今天要做的课程数据:" + obj);
				CourseSummaryData  courseSummaryData = new CourseSummaryData();
				courseSummaryData = new XSON().fromJSON(courseSummaryData, JSONUtil.getJsonObject(obj, "course_data"));
				skipTo("course_data", courseSummaryData, CourseSummaryActivity.class);
			}
		}, cookie);
	}


}
