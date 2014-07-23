package com.chzh.fitter.adapter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.chzh.fitter.framework.BaseDataAdapter;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;
import com.chzh.fitter.view.InviteItemView;

public class InviteAdapter extends BaseDataAdapter {

	public InviteAdapter(Context context) {
		super(context);
	}

	@Override
	public void configureItem(BaseDataItemView itemView) {
		final InviteItemView item = (InviteItemView) itemView;
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
		return new InviteItemView(mContext);
	}
	
	public String getCheckedPhoneNum() {
		StringBuffer phoneNums = new StringBuffer();
		for (int i = 0; i < mDataSource.length(); i++) {
			JSONObject obj = JSONUtil.getJsonObjByIndex(mDataSource, i);
			if (JSONUtil.getBoolean(obj, "is_checked")) {
				phoneNums.append(JSONUtil.getString(obj, "phone"));
				phoneNums.append(",");
			}
		}
		String str = phoneNums.substring(0, phoneNums.length() - 1);
		L.red(str);
		return str;
		
	}

}
