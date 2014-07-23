package com.chzh.fitter.adapter;

import org.json.JSONObject;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseDataAdapter;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.struct.ActionHistoryData;
import com.chzh.fitter.struct.CourseActionData;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;
import com.jarrah.json.XSON;


public class CourseScheduleAdataper extends BaseDataAdapter {

	public CourseScheduleAdataper(Context context) {
		super(context);
	}

	@Override
	public void configureItem(BaseDataItemView itemView) {

	}

	@Override
	public BaseDataItemView instanceItemView() {
		return new CourseActionItemView(mContext);
	}

	public class CourseActionItemView extends BaseDataItemView {

		public CourseActionItemView(Context context) {
			super(context);
		}

		@Override
		public void onDispatchData(JSONObject data) {
			L.red("课程详细内容单个动作数据: " + data);
			
			ActionHistoryData courseActionData = new ActionHistoryData();
			courseActionData = new XSON().fromJSON(courseActionData, data);
//
			ajaxImage(findView(R.id.action_img, ImageView.class), GlobalConstant.HOST_IP + courseActionData.getActionIconUrl());
			findView(R.id.action_name, TextView.class).setText(courseActionData.getTitle());
			
			String ending = courseActionData.getActionType() == 1 ? "resp" : "s";
			findView(R.id.action_count, TextView.class).setText(courseActionData.getActionAmount() + ending);
			
			boolean isChecked = courseActionData.getFinish() == 1 ? true : false;
			findView(R.id.isFinishCheckBox, CheckBox.class).setChecked(isChecked);
			findView(R.id.isFinishCheckBox, CheckBox.class).setEnabled(false);
		}

		@Override
		public void setupViews() {
			setContentView(R.layout.view_item_course_action);
		}

	}

}
