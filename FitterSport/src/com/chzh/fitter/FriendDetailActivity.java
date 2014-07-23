package com.chzh.fitter;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.widget.TextView;

import com.chzh.fitter.adapter.FriendEventAdapter;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.FriendPostData;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.view.BounceListView;
import com.chzh.fitter.view.RoundImageView;

public class FriendDetailActivity extends SimpleTitleSActivity{

	private BounceListView mEventList;
	private FriendEventAdapter mFriendEventAdapter;

	@Override
	protected void setupGUI() {
		mEventList = findView(R.id.friend_event, BounceListView.class);
		mFriendEventAdapter = new FriendEventAdapter(this);
		mEventList.setAdapter(mFriendEventAdapter);
		setData();
	}

	private void setData() {
		FriendPostData fData = (FriendPostData) getIntent().getSerializableExtra("data");
		
		findView(R.id.friends, TextView.class).setText("" + fData.getFriendCount());
		findView(R.id.status, TextView.class).setText(fData.getActTitle());
		findView(R.id.nick, TextView.class).setText(fData.getNickname());
		
		findView(R.id.portrait, RoundImageView.class).ajaxImage(GlobalConstant.HOST_IP + fData.getPic());
		
		setEventListData(fData.getUid());
	}

	private void setEventListData(String uid) {
		String id = uid;
		String cookie = new UICore(this).getToken();
		JHttpManager httpManager = new JHttpManager(this);
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("uid", id);
		
		httpManager.post(data , GlobalConstant.USER_PLAN_LOG, new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
					JSONArray array = JSONUtil.getJsonArrays(obj, "elem");
					mFriendEventAdapter.setData(array);
			}
		}, cookie);
	}

//	private JSONArray getData() {
//		JSONArray array = new JSONArray();
//		array.put(genObj("做了平板支撑20分钟, 俯卧撑20个", "2014/5/23  07:25"));
//		array.put(genObj("做了空气跳绳10分钟, 俯地登山步12分钟", "2014/5/25  12:25"));
//		array.put(genObj("做了原地深蹲50个, 俯卧撑20个", "2014/5/23  11:25"));
//		array.put(genObj("做了跳跃侧拍手10分钟, 原地后踢跑10分钟", "2014/5/23  09:45"));
//		return array;
//	}

//	private JSONObject genObj(String title, String time){
//		JSONObject obj0 = new JSONObject();
//		try {
//			obj0.put("event_title", title);
//			obj0.put("event_time", time);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return obj0;
//	}

	@Override
	protected String getTitleName() {
		return getFriendNmae();
	}

	private String getFriendNmae() {
		return getIntent().getStringExtra("friend_name");
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_friend_detail;
	}

}
