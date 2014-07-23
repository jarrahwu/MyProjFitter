package com.chzh.fitter.adapter;

import org.json.JSONObject;

import android.content.Context;

import com.chzh.fitter.InfoDetailActivity;
import com.chzh.fitter.framework.BaseDataAdapter;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.struct.MedalData;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.view.InfoDetailLevelItem;
import com.chzh.fitter.view.InfoDetailMealItem;
import com.chzh.fitter.view.InfoDetailScoreItem;
import com.chzh.fitter.view.InfoDetailStatusItem;
import com.jarrah.json.XSON;

public class InfoAdapter extends BaseDataAdapter {

	private int mType;

	public InfoAdapter(Context context) {
		super(context);
	}

	public InfoAdapter(Context context, int type) {
		this(context);
		mType = type;
	}

	@Override
	public void configureItem(BaseDataItemView itemView) {

	}

	@Override
	public BaseDataItemView instanceItemView() {
		return getItemViewByType();
	}

	private BaseDataItemView getItemViewByType() {
		BaseDataItemView item = null;
		switch (mType) {
		case InfoDetailActivity.TYPE_LEVEL:
			item = new InfoDetailLevelItem(mContext);
			break;

		case InfoDetailActivity.TYPE_MEDAL:
			item = new InfoDetailMealItem(mContext);
			break;

		case InfoDetailActivity.TYPE_SCORE:
			item = new InfoDetailScoreItem(mContext);
			break;

		case InfoDetailActivity.TYPE_STATUS:
			item = new InfoDetailStatusItem(mContext);
			break;

		default:
			break;
		}
		return item;
	}
	
	/**
	 * 获取勋章Url
	 * @return
	 */
	public String getMedalUrl() {
		String url = "";
		if (mType == InfoDetailActivity.TYPE_MEDAL) {
			for (int i = 0; i < mDataSource.length(); i++) {
				JSONObject data = JSONUtil.getJsonObjByIndex(mDataSource, i);
				MedalData medalData = new MedalData();
				XSON xson = new XSON();
				medalData = xson.fromJSON(medalData, data);
				
				if(medalData.getFinishStatus() == 1) {
					url = medalData.getImg();
					break;
				}
				
			}
		}
		return url;
	}

}
