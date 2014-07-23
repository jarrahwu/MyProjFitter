package com.chzh.fitter.view;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzh.fitter.FriendDetailActivity;
import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.struct.FriendPostData;
import com.chzh.fitter.util.L;
import com.jarrah.json.XSON;

public class FriendPostItemView extends BaseDataItemView {

	private FriendPostData friendPostData;

	public FriendPostItemView(Context context) {
		super(context);
	}

	@Override
	public void onDispatchData(JSONObject data) {
		
		friendPostData = new FriendPostData();
		friendPostData = new XSON().fromJSON(friendPostData, data);
		
		findView(R.id.portrait, RoundImageView.class).ajaxImage(GlobalConstant.HOST_IP + friendPostData.getPic());
		findView(R.id.nick, TextView.class).setText(friendPostData.getNickname());
		findView(R.id.act_content, TextView.class).setText(friendPostData.getContent());
		findView(R.id.activeness_level, ImageView.class).setImageResource(R.drawable.act_start);
		findView(R.id.post_time, TextView.class).setText(friendPostData.getCreatetime());
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_item_friend_post);
		bindClickEvent(this, "gotoFriendDetail");
	}

	public void gotoFriendDetail(View v) {
		Intent i = new Intent();
		i.putExtra("friend_name", findView(R.id.nick, TextView.class).getText());
		i.putExtra("data", friendPostData);
		i.setClass(mContext, FriendDetailActivity.class);
		mContext.startActivity(i);
	}
}
