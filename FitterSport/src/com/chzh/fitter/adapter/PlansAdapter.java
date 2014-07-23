package com.chzh.fitter.adapter;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.chzh.fitter.CourseSummaryActivity;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.BaseDataAdapter;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.CourseSummaryData;
import com.chzh.fitter.util.L;
import com.jarrah.json.XSON;

public class PlansAdapter extends BaseDataAdapter{

	public PlansAdapter(Context context) {
		super(context);
	}

	@Override
	public void configureItem(BaseDataItemView itemView) {

	}

	@Override
	public BaseDataItemView instanceItemView() {
		return new PlanItem(mContext);
	}


	public class PlanItem extends BaseDataItemView {

		private ImageView mPlanImage;

		public PlanItem(Context context) {
			super(context);
		}

		@Override
		public void onDispatchData(JSONObject data) {
			L.red("course data : " + data);
			CourseSummaryData gymPlanData = new CourseSummaryData();
			gymPlanData = new XSON().fromJSON(gymPlanData, data);
			
			ajaxImage(mPlanImage, GlobalConstant.HOST_IP + gymPlanData.getPic1080());
			
			setTag(gymPlanData);
			
			this.bindClickEvent(this, "gotoCourseSummary");
		}

		public void gotoCourseSummary(View v) {
			CourseSummaryData gymPlanData = (CourseSummaryData) v.getTag();
			Intent i = new Intent();
			i.setClass(mContext, CourseSummaryActivity.class);
			i.putExtra("course_data", gymPlanData);
			mContext.startActivity(i);
			postCourseId(gymPlanData.getCourseId());
		}

		@Override
		public void setupViews() {
			mPlanImage = new ImageView(mContext);
			mPlanImage.setLayoutParams(getCustomLayoutParams(-2, -2, Gravity.CENTER));
			mPlanImage.setScaleType(ScaleType.FIT_XY);
			setContentView(mPlanImage);
		}
	}


	public void postCourseId(int i) {
		String cookie = new UICore(mContext).getToken();
		JHttpManager httpManager = new JHttpManager(mContext);
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("fid", i);
		httpManager.post(data , GlobalConstant.ADD_PLANT, new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				L.red("已添加计划.");
			}
			
			
		}, cookie);
	}


}
