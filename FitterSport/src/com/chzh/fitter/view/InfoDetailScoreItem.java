package com.chzh.fitter.view;

import org.json.JSONObject;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.struct.UserScore;
import com.chzh.fitter.util.DensityUtil;
import com.jarrah.json.XSON;

public class InfoDetailScoreItem extends BaseDataItemView {
	
	public InfoDetailScoreItem(Context context) {
		super(context);
	}

	@Override
	public void onDispatchData(JSONObject data) {
		UserScore uScore = new UserScore();
		uScore = new XSON().fromJSON(uScore, data);
		
		ajaxImage(findView(R.id.score_icon, ImageView.class), GlobalConstant.HOST_IP + uScore.getPic());
		
		TextView title = findView(R.id.score_title, TextView.class);
		TextView score = findView(R.id.score, TextView.class);
		
		
		if (uScore.getTitle().equals("总积分")) {
			title.setTextSize(DensityUtil.convertDpToPixel(6));
			score.setTextSize(DensityUtil.convertDpToPixel(6));
			
			title.setTextColor(getResources().getColor(R.color.light_yellow));
			score.setTextColor(getResources().getColor(R.color.light_yellow));
		}
		else {
			title.setTextSize(DensityUtil.convertDpToPixel(5));
			score.setTextSize(DensityUtil.convertDpToPixel(5));
			
			title.setTextColor(getResources().getColor(R.color.WHITE));
			score.setTextColor(getResources().getColor(R.color.WHITE));
		}
		
		title.setText(uScore.getTitle());
		score.setText("" + uScore.getTotal());
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_item_info_detail_score);
	}

}
