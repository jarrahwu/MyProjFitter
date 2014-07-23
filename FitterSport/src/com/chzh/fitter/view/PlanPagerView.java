package com.chzh.fitter.view;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.chzh.fitter.GymPlansActivity;
import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.struct.CourseSummaryData;
import com.chzh.fitter.util.JSONUtil;
import com.jarrah.json.XSON;

public class PlanPagerView extends BaseView implements OnPageChangeListener {

	private ViewPager mViewPager;

	private PagerIndicator mIndicator;

	private PlanPagerAapter mAdapter;

	public PlanPagerView(Context context) {
		super(context);
	}

	public PlanPagerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_plan_pager);
		mViewPager = findView(R.id.view_pager, ViewPager.class);
		mIndicator = findView(R.id.indicator, PagerIndicator.class);

		mAdapter = new PlanPagerAapter();

		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);

	}



	private class PlanPagerAapter extends PagerAdapter{

		private ArrayList<View> list;

		private JSONArray mJsonArray;

		public PlanPagerAapter() {
			list = new ArrayList<View>();
		}

		@Override
		public int getCount() {
			return mJsonArray == null ? 0 : mJsonArray.length();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(this.list.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(this.list.get(position));
			return list.get(position);
		}

		public void setData(JSONArray jsonArray) {
			mJsonArray = jsonArray;
			for (int i = 0; i < jsonArray.length(); i++) {
				//解析数据
				CourseSummaryData gymPlanData = new CourseSummaryData();
				JSONObject curObj = JSONUtil.getJsonObjByIndex(jsonArray, i);
				XSON xson = new XSON();
				gymPlanData = xson.fromJSON(gymPlanData, curObj);
				
				String imageUrl = GlobalConstant.HOST_IP + gymPlanData.getPic1080();
				list.add(genImageView(imageUrl));
			}
			mIndicator.initIndicator(jsonArray.length(), R.drawable.point_normal, R.drawable.point_selected);
			notifyDataSetChanged();
		}

		private View genImageView(String url) {
			ImageView v = new ImageView(getContext());
			v.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
			v.setScaleType(ScaleType.FIT_XY);
			ajaxImage(v, url);
			bindClickEvent(v, "gotoGymPlans");
			return v;
		}
	}

	public void gotoGymPlans(View v) {
		skipTo(GymPlansActivity.class);
	}



	//pager 滑动的监听方法

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		mIndicator.selectItem(arg0);
	}

	public void setData(JSONArray jsonArray) {
		mAdapter.setData(jsonArray);
	}

	private int currIndex = 0;

	public void autoScrollAtTime(final long time, final boolean flag) {
		if (flag) {
			postDelayed(new Runnable() {
				@Override
				public void run() {
					if (currIndex >= mAdapter.getCount())
						currIndex = 0;
					mViewPager.setCurrentItem(currIndex, true);
					currIndex += 1;
					autoScrollAtTime(time, flag);
				}
			}, time);
		}
	}
}
