package com.chzh.fitter.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.database.CrossProcessCursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzh.fitter.ActionDownloadActivity;
import com.chzh.fitter.R;
import com.chzh.fitter.data.SingleAction;
import com.chzh.fitter.download.ActionDownView;
import com.chzh.fitter.framework.BaseDataAdapter;
import com.chzh.fitter.framework.BaseDataItemView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.struct.CourseActionData;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;
import com.chzh.fitter.video.ActionPreviewActivity;
import com.chzh.fitter.video.VideoPlayerActivity;
import com.jarrah.json.XSON;
import com.jw.progress.ProgressHUD;

public class CourseSummaryAdapter extends BaseDataAdapter{

	private boolean mItemClickCanPlay = true; //权限问题,有等级关系决定
	private String mBackgroundUrl;

	public CourseSummaryAdapter(Context context) {
		super(context);
	}

	@Override
	public void configureItem(BaseDataItemView itemView) {

	}

	@Override
	public BaseDataItemView instanceItemView() {
		return new ActionIntroItemView(mContext);
	}

	//动作介绍
	public class ActionIntroItemView extends BaseDataItemView {

		private CourseActionData courseActionData;

		public ActionIntroItemView(Context context) {
			super(context);
		}

		@Override
		public void onDispatchData(JSONObject data) {
			
			if(data == null) return;
			
			courseActionData = new CourseActionData();
			courseActionData = new XSON().fromJSON(courseActionData, data);
			courseActionData.setBackgroundUrl(mBackgroundUrl);
			ajaxImage(findView(R.id.action_img, ImageView.class), GlobalConstant.HOST_IP + courseActionData.getActionIcon());
			findView(R.id.action_name, TextView.class).setText(courseActionData.getActionTitle());
			
			String ending = courseActionData.getActionType() == 1 ? "resp" : "s";
			
			findView(R.id.action_count, TextView.class).setText(courseActionData.getActionAmount() + ending);
		}

		@Override
		public void setupViews() {
			setContentView(R.layout.view_item_action_intro);
			bindClickEvent(this, "gotoActionPreview");
		}

		public void gotoActionPreview(View v) {
			
			if (courseActionData.getActionType() == 2) { //休息,不能点击播放
				showToast("休息没有动作预览");
				return;
			}
			
			if (mItemClickCanPlay) {
				ActionDownView view = new ActionDownView(mContext, courseActionData);
				view.dismissDialogWhenDownloaded(ProgressHUD.showDownload(mContext, view, null));
			}else
				showToast("没权限预览该视频.");
		}
	}

	public void setCoursePlayEnable(boolean b) {
		mItemClickCanPlay = b;//
	}
	
	
	public void setCourseBackground(String url) {
		mBackgroundUrl = url;
	}
	
	/**
	 * 获取播放列表
	 * @return
	 */
	public ArrayList<CourseActionData> getCoursePlayList() {
		ArrayList<CourseActionData> list = new ArrayList<CourseActionData>();
		XSON xson = new XSON();
		for (int i = 0; i < mDataSource.length(); i++) {
			JSONObject obj = JSONUtil.getJsonObjByIndex(mDataSource, i);
			CourseActionData action = new CourseActionData();
			action = xson.fromJSON(action, obj);
			list.add(action);
		}
		return list;
	}
}
