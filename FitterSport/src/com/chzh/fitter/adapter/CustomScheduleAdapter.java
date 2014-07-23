package com.chzh.fitter.adapter;

import org.json.JSONObject;

import android.content.Context;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseDataAdapter;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.util.JSONUtil;

public class CustomScheduleAdapter extends BaseDataAdapter {

	public CustomScheduleAdapter(Context context) {
		super(context);
	}

	@Override
	public void configureItem(BaseDataItemView itemView) {
	}

	@Override
	public BaseDataItemView instanceItemView() {
		return new CustomScheduleItemView(mContext);
	}

	private class CustomScheduleItemView extends BaseDataItemView {

		public CustomScheduleItemView(Context context) {
			super(context);
		}

		@Override
		public void onDispatchData(JSONObject data) {
			String title = JSONUtil.getString(data, "question_title");
			findView(R.id.title, TextView.class).setText("" + (mPosition + 1) + "丶 " + title);
		}

		@Override
		public void setupViews() {
			setContentView(R.layout.view_item_custom_schedule);
		}

	}

}
