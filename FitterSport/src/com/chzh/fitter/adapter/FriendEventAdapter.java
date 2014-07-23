package com.chzh.fitter.adapter;

import org.json.JSONObject;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseDataAdapter;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.struct.DynamicData;
import com.chzh.fitter.util.JSONUtil;
import com.jarrah.json.XSON;

public class FriendEventAdapter extends BaseDataAdapter {

	public FriendEventAdapter(Context context) {
		super(context);
	}

	@Override
	public void configureItem(BaseDataItemView itemView) {

	}

	@Override
	public BaseDataItemView instanceItemView() {
		return new FriendEventItemView(mContext);
	}

	private class FriendEventItemView extends BaseDataItemView {

		public FriendEventItemView(Context context) {
			super(context);
		}

		@Override
		public void onDispatchData(JSONObject data) {
			DynamicData dynamicData = new DynamicData();
			dynamicData = new XSON().fromJSON(dynamicData, data);
			
			String title = dynamicData.getContent();
			String time = dynamicData.getPostTime();
			findView(R.id.event_title, TextView.class).setText(title);
			findView(R.id.event_time, TextView.class).setText(time);
			
			setCommentCount(dynamicData.getCommentCount());
			setFavorCount(dynamicData.getFavorCount());
		}

		@Override
		public void setupViews() {
			setContentView(R.layout.view_item_friend_event);
		}
		
		public void setCommentCount(int i) {
			String text = "评论 ";
			if (i > 0) {
				text = text + "(" + i + ")";
			}
			findView(R.id.comment, Button.class).setText(text);
		}
		
		public void setFavorCount(int i) {
			String text = "赞 ";
			if (i > 0) {
				text = text + "(" + i + ")";
			}
			findView(R.id.favor, Button.class).setText(text);
		}

	}

}
