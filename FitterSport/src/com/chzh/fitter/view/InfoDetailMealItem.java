package com.chzh.fitter.view;

import org.json.JSONObject;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.struct.MedalData;
import com.chzh.fitter.util.L;
import com.jarrah.json.XSON;

public class InfoDetailMealItem extends BaseDataItemView {

	public InfoDetailMealItem(Context context) {
		super(context);
	}

	@Override
	public void onDispatchData(JSONObject data) {
		
		L.red("medal" + data);
		
		MedalData medalData = new MedalData();
		XSON xson = new XSON();
		medalData = xson.fromJSON(medalData, data);
		
		findView(R.id.medal_name, TextView.class).setText(medalData.getTitle());
		findView(R.id.medal_description, TextView.class).setText(medalData.getIntroduce());
		
		String imgUrl = GlobalConstant.HOST_IP + medalData.getImg();
		ajaxImage(findView(R.id.medal_icon, ImageView.class), imgUrl);
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_item_info_detail_medal);
	}

}
