package com.chzh.fitter.view;

import org.json.JSONObject;

import android.content.Context;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.struct.StatusData;
import com.jarrah.json.XSON;

public class InfoDetailStatusItem extends BaseDataItemView {
	
	public static final int statusRes = R.drawable.ic_s1;
	
	public InfoDetailStatusItem(Context context) {
		super(context);
	}

	@Override
	public void onDispatchData(JSONObject data) {
		StatusData statusData = new StatusData();
		statusData = new XSON().fromJSON(statusData, data);
		
		findView(R.id.status_bg).setBackgroundResource(statusRes + mPosition);
		findView(R.id.status_title, TextView.class).setText(statusData.getTitle());
		findView(R.id.status_info, TextView.class).setText(statusData.getIntroduce());
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_item_info_detail_status);
	}

}
