package com.chzh.fitter.fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.chzh.fitter.CourseResultActivity;
import com.chzh.fitter.CourseScheduleActivity;
import com.chzh.fitter.R;
import com.chzh.fitter.adapter.SportLogAdapter;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.BaseFragment;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.XUser;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;
import com.chzh.fitter.view.RoundImageView;

public class ScheduleFragment extends BaseFragment implements GlobalConstant{
	
	
	private ListView mListView;
	private SportLogAdapter mAdapter;
	private XUser mUserinfo;
	
	@Override
	protected View getContentView() {
		return inflateViewFrom(R.layout.schedule);
	}

	@Override
	protected void setupViews() {
		mUserinfo = new UICore(getActivity()).getUserFromPreference();
		findView(R.id.portrait, RoundImageView.class).ajaxImage(GlobalConstant.HOST_IP + mUserinfo.getPortrait());
		findView(R.id.nick, TextView.class).setText(mUserinfo.getNickName());
		findView(R.id.friends, TextView.class).setText(""+mUserinfo.getFriendCount());
		findView(R.id.status, TextView.class).setText(mUserinfo.getActiveStatus());
		
		mListView = findView(R.id.schedule_list, ListView.class);
		mAdapter = new SportLogAdapter(mContext);
		mListView.setAdapter(mAdapter);
		queryList();
	}

	private void queryList() {
		JHttpManager httpManager = new JHttpManager(mContext);
		String cookie = new UICore(mContext).getToken();
		httpManager.get(SPORT_LOG, new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				JSONArray array = JSONUtil.getJsonArrays(obj, "elem");
				mAdapter.setData(array);
			}
		}, cookie);
	}

	
	@Override
	public void onResume() {
		findView(R.id.portrait, RoundImageView.class).ajaxImage(GlobalConstant.HOST_IP + mUserinfo.getPortrait());
		super.onResume();
	}
}
