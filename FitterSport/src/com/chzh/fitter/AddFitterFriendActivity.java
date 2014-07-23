package com.chzh.fitter;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.chzh.fitter.adapter.AddFitterFriendAdapter;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;

public class AddFitterFriendActivity extends SimpleTitleSActivity{
	
	private EditText mEditText;
	private Button mSearch;
	private ListView mListView;
	private AddFitterFriendAdapter mAdapter;
	private boolean allChecked = false;
	
	@Override
	protected void setupGUI() {
		mEditText = findView(R.id.et_search, EditText.class);
		mSearch = findView(R.id.btn_search, Button.class);
		mListView = findView(R.id.invite_list, ListView.class);
		mAdapter = new AddFitterFriendAdapter(this);
		mListView.setAdapter(mAdapter);
		bindClickEvent(mSearch, "onSearchClick");
		bindClickEvent(findView(R.id.send_invite), "sendInvite");
		bindClickEvent(findView(R.id.check_all), "checkAll");
		
		findView(R.id.send_invite, Button.class).setText("添加好友");
		
		getData();
	}
	
	private void getData() {
		String cookie = new UICore(this).getToken();
		JHttpManager jHttpManager = new JHttpManager(this);
		
		String url = GlobalConstant.GET_FITTER_FRIENDS;
		jHttpManager.get(url, new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				JSONArray array = JSONUtil.getJsonArrays(obj, "elem");
				
				mAdapter.setData(array);
				mAdapter.setAllChecked(false);
			}
		}, cookie);
	}

	public void onSearchClick(View v) {
		String text = mEditText.getText().toString();
		
		if (text.length() == 0) {
			return;
		}
		
		JSONArray array = mAdapter.getSearchData(text);
		mAdapter.setData(array);
	}
	
	/**
	 * 全选
	 * @param v
	 */
	public void checkAll(View v) {
		allChecked = !allChecked;
		mAdapter.setAllChecked(allChecked);
	}
	
	
	public void sendInvite(View v) {
		String phoneNums = mAdapter.getCheckedPhoneNum();
		JHttpManager httpManager = new JHttpManager(this);
		String cookie = new UICore(this).getToken();
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("mobile", phoneNums);
		httpManager.post(data, GlobalConstant.INVITE_FITTER, new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				L.red(obj);
				showToast(R.layout.toast_invite_success);
			}
		}, cookie);	
		
		finish();
	}

	@Override
	protected String getTitleName() {
		return "添加好友";
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_add_fitter_friends;
	}

}
