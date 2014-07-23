package com.chzh.fitter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.ListView;

import com.chzh.fitter.adapter.InviteAdapter;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.core.UICore.ContactInfo;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;

public class InviteActivity extends SimpleTitleSActivity implements GlobalConstant{

	private ListView mListView; // invite list
	private InviteAdapter mAdapter;
	private JSONArray mData;//数据源

	private boolean allChecked = false;

	@Override
	protected void setupGUI() {
		mListView = findView(R.id.invite_list, ListView.class);

		mAdapter = new InviteAdapter(this);
		mListView.setAdapter(mAdapter);

		mData = getContactData();
		mAdapter.setData(mData);

		bindClickEvent(findView(R.id.send_invite), "sendInvite");
		bindClickEvent(findView(R.id.check_all), "checkAll");
		
		findView(R.id.invite_parent).setBackgroundColor(getResources().getColor(R.color.white_20trnas));
	}

	public void sendInvite(View v) {
		String phoneNums = mAdapter.getCheckedPhoneNum();
		
		JHttpManager httpManager = new JHttpManager(this);
		String cookie = new UICore(this).getToken();
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("mobile", phoneNums);
		httpManager.post(data, INVITE_PHONE_CONTACTS, new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				L.red(obj);
//				showToast("已发送邀请.");
				showToast(R.layout.toast_invite_success);
			}
		}, cookie);	
		
		finish();
	}

	/**
	 * 全选
	 * @param v
	 */
	public void checkAll(View v) {
		allChecked = !allChecked;
		setCheckAllData(allChecked);
		mAdapter.notifyDataSetChanged();
	}

	public void setCheckAllData(boolean chcked) {
		for (int i = 0; i < mData.length(); i++) {
			JSONObject obj = JSONUtil.getJsonObjByIndex(mData, i);
			try {
				obj.put("is_checked", chcked);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected String getTitleName() {
		return "邀请好友";
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_invite;
	}


	/**
	 * 获取联系人数据
	 * @return
	 */
	private JSONArray getContactData() {
		JSONArray array = new JSONArray();

		UICore core = new UICore(this);
		ArrayList<ContactInfo> contacts = core.getPhoneContacts(this);

		for (int i = 0; i < contacts.size(); i++) {
			ContactInfo info = contacts.get(i);
			JSONObject obj = new JSONObject();

			try {
				obj.put("name", info.getName());
				obj.put("phone", info.getPhone());
				obj.put("is_checked", false);
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				array.put(obj);
			}
		}

		return array;
	}
}
