package com.chzh.fitter.fragment;

import org.json.JSONObject;

import android.view.View;
import android.widget.ListView;

import com.chzh.fitter.AddFitterFriendActivity;
import com.chzh.fitter.InviteActivity;
import com.chzh.fitter.R;
import com.chzh.fitter.adapter.FriendPostAdapter;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.BaseFragment;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.util.JSONUtil;

public class FriendsFragment extends BaseFragment implements GlobalConstant{

	private ListView mListView;
	private FriendPostAdapter mAdapter;

	@Override
	protected View getContentView() {
		return inflateViewFrom(R.layout.friends);
	}

	@Override
	protected void setupViews() {
		mListView = findView(R.id.friend_post_list, ListView.class);
		mAdapter = new FriendPostAdapter(mContext);
		mListView.setAdapter(mAdapter);
		setFriendsData();
		bindClickEvent(findView(R.id.invite_friend), "inviteFriends");
		bindClickEvent(findView(R.id.add_friend), "addFriends");
	}

	private void setFriendsData() {
		String cookie = new UICore(mContext).getToken();
		JHttpManager httpManager = new JHttpManager(mContext);
		httpManager.get(GET_FRIENDS, new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				mAdapter.setData(JSONUtil.getJsonArrays(obj, "elem"));
			}
		}, cookie);
	}
	
	public void addFriends(View v) {
		skipTo(AddFitterFriendActivity.class);
	}

	public void inviteFriends(View v) {
		skipTo(InviteActivity.class);
	}
}
