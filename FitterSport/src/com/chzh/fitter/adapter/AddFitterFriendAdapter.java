package com.chzh.fitter.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseDataAdapter;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.struct.AddFitterFriendData;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;
import com.chzh.fitter.view.RoundImageView;
import com.jarrah.json.XSON;

public class AddFitterFriendAdapter extends BaseDataAdapter{

	public AddFitterFriendAdapter(Context context) {
		super(context);
	}

	@Override
	public void configureItem(BaseDataItemView itemView) {
		final AddFitterFriendItem item = (AddFitterFriendItem) itemView;
		CheckBox cb = item.getCheckBox();
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				try {
					item.getData().put("is_checked", isChecked);
					mDataSource.put(item.getPosition(), item.getData());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	@Override
	public BaseDataItemView instanceItemView() {
		return new AddFitterFriendItem(mContext);
	}
	
	public class AddFitterFriendItem extends BaseDataItemView {

		public AddFitterFriendItem(Context context) {
			super(context);
		}

		@Override
		public void onDispatchData(JSONObject data) {
			L.red(data);
			AddFitterFriendData addFitterFriendData = new AddFitterFriendData();
			addFitterFriendData = new XSON().fromJSON(addFitterFriendData, data);
			
			findView(R.id.info, TextView.class).setText(addFitterFriendData.getNickname() + "("
					+ addFitterFriendData.getMobile() +")");
			findView(R.id.portrait, RoundImageView.class).ajaxImage(GlobalConstant.HOST_IP + addFitterFriendData.getPortrait());
			
			boolean isChecked = JSONUtil.getBoolean(data, "is_checked");
			findView(R.id.is_invite, CheckBox.class).setChecked(isChecked);
		}

		@Override
		public void setupViews() {
			setContentView(R.layout.view_item_add_fitter_friend);
		}
		
		public CheckBox getCheckBox() {
			return findView(R.id.is_invite, CheckBox.class);
		}
	}

	public void setAllChecked(boolean isChecked) {
		for (int i = 0; i < mDataSource.length(); i++) {
			JSONObject obj = JSONUtil.getJsonObjByIndex(mDataSource, i);
			try {
				obj.put("is_checked", isChecked);
				mDataSource.put(i, obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		notifyDataSetChanged();
	}

	public JSONArray getSearchData(String text) {
		JSONArray newArray = new JSONArray();
		for (int i = 0; i < mDataSource.length(); i++) {
			JSONObject obj = JSONUtil.getJsonObjByIndex(mDataSource, i);
			AddFitterFriendData data = new AddFitterFriendData();
			data = new XSON().fromJSON(data, obj);
			if (text.equals(data.getMobile())) {
				newArray.put(obj);
			}
		}
		return newArray;
	}
	
	public String getCheckedPhoneNum() {
		StringBuffer phoneNums = new StringBuffer();
		for (int i = 0; i < mDataSource.length(); i++) {
			JSONObject obj = JSONUtil.getJsonObjByIndex(mDataSource, i);
			AddFitterFriendData addFitterFriendData = new AddFitterFriendData();
			addFitterFriendData = new XSON().fromJSON(addFitterFriendData, obj);
			if (JSONUtil.getBoolean(obj, "is_checked")) {
				phoneNums.append(addFitterFriendData.getMobile());
				phoneNums.append(",");
			}
		}
		String str = phoneNums.substring(0, phoneNums.length() - 1);
		L.red(str);
		return str;
		
	}
	
}
