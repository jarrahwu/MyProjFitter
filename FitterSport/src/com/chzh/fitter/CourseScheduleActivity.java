package com.chzh.fitter;

import java.io.File;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.chzh.fitter.adapter.CourseScheduleAdataper;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.DownloadManager;
import com.chzh.fitter.network.FileDownloadCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.SportLogData;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;
import com.chzh.fitter.view.BounceListView;
import com.jarrah.blur.Blur;

/**
 * 运动计划详情
 *
 */
public class CourseScheduleActivity extends SimpleTitleSActivity{

	private BounceListView mListView;
	private CourseScheduleAdataper mAdapter;
	private SportLogData mCourseDetailData;

	@Override
	protected void setupGUI() {
		mListView = findView(R.id.action_list, BounceListView.class);
		mAdapter = new CourseScheduleAdataper(this);
		mListView.setAdapter(mAdapter);
		queryCourseInfo();
		queryCourseList();
	}

	private void queryCourseInfo() {
		ajaxImage(findView(R.id.courseBanner, ImageView.class), GlobalConstant.HOST_IP + mCourseDetailData.getBannerBg());
		setBackgroundRes(GlobalConstant.HOST_IP + mCourseDetailData.getBackground());
	}

	private void queryCourseList() {
		String url = mCourseDetailData.getCourseDetailUrl();
		String cookie = new UICore(this).getToken();
		JHttpManager httpManager = new JHttpManager(this);
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("fid", mCourseDetailData.getCourseId());
		data.put("pid", mCourseDetailData.getPlanId());
		httpManager.post(data , url, new CodeCallBack() {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				L.red("详细课程数据: " + obj);
				JSONArray array = JSONUtil.getJsonArrays(obj, "elem");
				mAdapter.setData(array);
			}
		}, cookie);
	}

	@Override
	protected String getTitleName() {
		mCourseDetailData = (SportLogData) getIntent().getSerializableExtra("detail_data");
		return mCourseDetailData.getTitle();
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_course_schedule;
	}
	
	private void setBackgroundRes(String bgUrl) {
		DownloadManager dm = new DownloadManager(this);
		dm.downloadFile(bgUrl, 60 * 60 * 1000, new FileDownloadCallBack() {
			private Bitmap mBackgroundBitmap;
			private Bitmap mBlurBackgroundBitmap;

			@Override
			public void onFileDownloaded(String url, File object) {
				mBackgroundBitmap = BitmapFactory.decodeFile(object.getAbsolutePath());
				mBlurBackgroundBitmap = Blur.fastblur(CourseScheduleActivity.this, mBackgroundBitmap, 117);
				setAppBackground(mBlurBackgroundBitmap);
			}
		});
	}

}
