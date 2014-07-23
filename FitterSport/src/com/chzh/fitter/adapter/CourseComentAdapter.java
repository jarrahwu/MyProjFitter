package com.chzh.fitter.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import com.chzh.fitter.framework.BaseDataAdapter;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.struct.CourseActionData;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;
import com.chzh.fitter.view.CourseCommentItemView;
import com.chzh.fitter.view.CourseCommentItemView.OnFeelSelectedLisener;

public class CourseComentAdapter extends BaseDataAdapter implements OnFeelSelectedLisener{

	private ArrayList<CourseActionData> mDataList;

	public CourseComentAdapter(Context context) {
		super(context);
	}

	@Override
	public void setData(JSONArray jsonArray) {
		mDataSource = jsonArray;
		modifyDataSource();
		notifyDataSetChanged();
	}

	private void modifyDataSource() {
		
		if (mDataSource == null) return;
		
		for (int i = 0; i < mDataSource.length(); i++) {
			JSONObject obj = JSONUtil.getJsonObjByIndex(mDataSource, i);
			try {
				obj.put("feel_id", 0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				mDataSource.put(i,obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void configureItem(BaseDataItemView itemView) {
		CourseCommentItemView courseCommentItemView = (CourseCommentItemView) itemView;
		courseCommentItemView.setOnFeelSelectedListener(this);
		
		courseCommentItemView.setCommentData(this.mDataList.get(courseCommentItemView.getPosition()));
	}

	@Override
	public BaseDataItemView instanceItemView() {
		return new CourseCommentItemView(mContext);
	}

	public JSONArray getDataSource() {
		return mDataSource;
	}

	public  ArrayList<CourseActionData> getCourseCommentResult() {
		for (int i = 0; i < mDataSource.length(); i++) {
			JSONObject obj = JSONUtil.getJsonObjByIndex(mDataSource, i);
			CourseActionData data = mDataList.get(i);
			int feelId = JSONUtil.getInt(obj, "feel_id");
			data.setFeelId(feelId);
			mDataList.set(i, data);
		}
		return mDataList;
	}

	@Override
	public void onFeelSelected(int postion, int feelId) {
		JSONObject obj = JSONUtil.getJsonObjByIndex(mDataSource, postion);
		try {
			obj.put("feel_id", feelId);
			mDataSource.put(postion, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setCourseCommentList(ArrayList<CourseActionData> list) {
		this.mDataList = list;
	}

}
