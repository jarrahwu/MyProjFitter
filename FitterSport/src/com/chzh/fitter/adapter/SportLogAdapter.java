package com.chzh.fitter.adapter;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chzh.fitter.CourseResultActivity;
import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseDataAdapter;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.struct.SportLogData;
import com.chzh.fitter.util.L;
import com.jarrah.json.XSON;

public class SportLogAdapter extends BaseDataAdapter{

	public SportLogAdapter(Context context) {
		super(context);
	}

	@Override
	public void configureItem(BaseDataItemView itemView) {
		
	}

	@Override
	public BaseDataItemView instanceItemView() {
		return new SportLogItemView(mContext);
	}
	
	
	public class SportLogItemView extends BaseDataItemView {

		private SportLogData sportLogData;

		public SportLogItemView(Context context) {
			super(context);
		}

		@Override
		public void onDispatchData(JSONObject data) {
			L.red("运动记录: " + data);
			sportLogData = new SportLogData();
			sportLogData = new XSON().fromJSON(sportLogData, data);
			sportLogData.parseEffectArray(data, "effect");
			sportLogData.parseShareObJ(data, "share");
			
			findView(R.id.course_title, TextView.class).setText(sportLogData.getTitle());
			findView(R.id.complete_percentage, TextView.class).setText(sportLogData.getPercentage());
			
			bindClickEvent(this, "gotoCourseResult");
		}

		@Override
		public void setupViews() {
			setContentView(R.layout.view_item_sport_log);
		}	
		
		public void gotoCourseResult(View v) {
			skipTo("result_data", sportLogData, CourseResultActivity.class);
		}
	}
	
		
}
