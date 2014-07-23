package com.chzh.fitter.fragment;

import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.chzh.fitter.FitterFriendsActivity;
import com.chzh.fitter.InfoDetailActivity;
import com.chzh.fitter.R;
import com.chzh.fitter.UserActivity;
import com.chzh.fitter.adapter.ScheduleEventAdapter;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.BaseFragment;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.XUser;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;
import com.chzh.fitter.view.BounceListView;
import com.chzh.fitter.view.RoundImageView;

public class MeFragment extends BaseFragment implements GlobalConstant{

	private BounceListView mEventList;
	private ScheduleEventAdapter mAdapter;
	
	private XUser mUserInfo;
	private UICore mUICore;

	@Override
	protected View getContentView() {
		return inflateViewFrom(R.layout.me);
	}

	@Override
	protected void setupViews() {
		mEventList = findView(R.id.event_list, BounceListView.class);
		mAdapter = new ScheduleEventAdapter(mContext);
		mEventList.setAdapter(mAdapter);
		mUICore = new UICore(mContext);
		setLocalData();
		queryEventData();

		bindClickEvent(findView(R.id.portrait), "skipUserInfo");
		bindClickEvent(findView(R.id.status_parent), "skipStatusDetail");
		bindClickEvent(findView(R.id.level), "skipLevelDetail");
		bindClickEvent(findView(R.id.medal_parent), "skipMedalDetail");
		bindClickEvent(findView(R.id.friends_parent), "skipToFittetFriends");

		mEventList.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
	}

	private void setLocalData() {
		mUserInfo = mUICore.getUserFromPreference();
		findView(R.id.portrait, RoundImageView.class).ajaxAutoOrientationImage(HOST_IP + mUserInfo.getPortrait());
		findView(R.id.nick, TextView.class).setText(mUserInfo.getNickName());
		findView(R.id.status, TextView.class).setText(mUserInfo.getActiveStatus());
		findView(R.id.friends, TextView.class).setText(""+mUserInfo.getFriendCount());
	}

	private void queryEventData() {
		JHttpManager httpManager = new JHttpManager(mContext);
		String cookie = new UICore(mContext).getToken();
		httpManager.get(CURRENT_TODO, new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				mAdapter.setData(JSONUtil.getJsonArrays(obj, "elem"));
			}
		}, cookie);
	}

	public void skipUserInfo(View v) {
		skipTo(UserActivity.class);
	}
	
	@Override
	public void onResume() {
		L.red("MeFragment onResume");
		setLocalData();
		super.onResume();
	}
	
	public void skipStatusDetail(View v) {
		Intent i = new Intent();
		i.setClass(getActivity(), InfoDetailActivity.class);
		i.putExtra("info_type", InfoDetailActivity.TYPE_STATUS);
		i.putExtra("title", "状态查询");
		startActivity(i);
	}
	
	public void skipLevelDetail(View v) {
		Intent i = new Intent();
		i.setClass(getActivity(), InfoDetailActivity.class);
		i.putExtra("info_type", InfoDetailActivity.TYPE_LEVEL);
		i.putExtra("title", "等级查询");
		startActivity(i);
	}
	
	public void skipMedalDetail(View v) {
		Intent i = new Intent();
		i.setClass(getActivity(), InfoDetailActivity.class);
		i.putExtra("info_type", InfoDetailActivity.TYPE_MEDAL);
		i.putExtra("title", "勋章查询");
		startActivity(i);
	}
	
	public void skipToFittetFriends(View v) {
		skipTo(FitterFriendsActivity.class);
	}



}
