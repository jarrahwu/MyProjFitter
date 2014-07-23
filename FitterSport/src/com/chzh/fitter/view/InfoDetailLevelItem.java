package com.chzh.fitter.view;

import org.json.JSONObject;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.struct.LevelData;
import com.jarrah.json.XSON;

public class InfoDetailLevelItem extends BaseDataItemView {

	public InfoDetailLevelItem(Context context) {
		super(context);
	}

	@Override
	public void onDispatchData(JSONObject data) {
		LevelData levelData = new LevelData();
		levelData = new XSON().fromJSON(levelData, data);
		
		findView(R.id.level_title, TextView.class).setText(levelData.getTitle());
		findView(R.id.level_description, TextView.class).setText(levelData.getIntroduce());
		ajaxImage(findView(R.id.level_img, ImageView.class), GlobalConstant.HOST_IP + levelData.getImg());
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_item_info_detail_level);
	}

}
