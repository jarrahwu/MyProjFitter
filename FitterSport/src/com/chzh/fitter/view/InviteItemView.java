package com.chzh.fitter.view;

import org.json.JSONObject;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.util.JSONUtil;

public class InviteItemView extends BaseDataItemView {

	private CheckBox mBox;

	public InviteItemView(Context context) {
		super(context);
	}

	@Override
	public void onDispatchData(JSONObject data) {
		String name = JSONUtil.getString(data, "name");
		String phone = JSONUtil.getString(data, "phone");
		String text = name + " (" + phone + ")";
		findView(R.id.name, TextView.class).setText(text);
		boolean isCheck = JSONUtil.getBoolean(data, "is_checked");
		mBox.setChecked(isCheck);
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_item_invite);
		mBox = findView(R.id.is_invite, CheckBox.class);
	}

	public CheckBox getCheckBox() {
		return mBox;
	}

}
