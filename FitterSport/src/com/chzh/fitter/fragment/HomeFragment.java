package com.chzh.fitter.fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chzh.fitter.CourseSummaryActivity;
import com.chzh.fitter.R;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.BaseFragment;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.CourseSummaryData;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.view.PlanPagerView;
import com.jarrah.json.XSON;

public class HomeFragment extends BaseFragment implements GlobalConstant{

	private PlanPagerView mPagerView;

	@Override
	protected View getContentView() {
		return inflateViewFrom(R.layout.home);
	}

	@Override
	protected void setupViews() {
		mPagerView = findView(R.id.plan_pager, PlanPagerView.class);
		mPagerView.autoScrollAtTime(3000L, true);
		
		bindClickEvent(R.id.img2, "gotoCourseSummary");
		
		queryHome();
	}
	
	public void gotoCourseSummary(View v) {
		CourseSummaryData gymPlanData = (CourseSummaryData) v.getTag();
		Intent i = new Intent();
		i.setClass(mContext, CourseSummaryActivity.class);
		i.putExtra("course_data", gymPlanData);
		mContext.startActivity(i);
	}

	private void queryHome() {
		JHttpManager httpManager = new JHttpManager(getActivity());
		String cookie = new UICore(mContext).getToken();
		httpManager.get(HOME, new CodeCallBack() {

			@Override
			public void handleCallBack(JSONObject obj) {
				JSONArray pagerData = JSONUtil.getJsonArrays(obj, "elem");
				JSONObject recommendData = JSONUtil.getJsonObject(obj, "recommend");
				
				mPagerView.setData(pagerData);
				setRecommendData(recommendData);
				
			}}, cookie);
	}

	private void setRecommendData(JSONObject recommendData) {
		CourseSummaryData planData = new CourseSummaryData();
		planData = new XSON().fromJSON(planData, recommendData);
		findView(R.id.img2, ImageView.class).setTag(planData);
		ajaxImage(findView(R.id.img2, ImageView.class), GlobalConstant.HOST_IP + planData.getPic1080());
	}


}
