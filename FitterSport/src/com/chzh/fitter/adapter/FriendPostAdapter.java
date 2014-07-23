package com.chzh.fitter.adapter;

import android.content.Context;

import com.chzh.fitter.framework.BaseDataAdapter;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.view.FriendPostItemView;

public class FriendPostAdapter extends BaseDataAdapter {

	public FriendPostAdapter(Context context) {
		super(context);
	}

	@Override
	public void configureItem(BaseDataItemView itemView) {
		
	}

	@Override
	public BaseDataItemView instanceItemView() {
		return new FriendPostItemView(mContext);
	}

}
