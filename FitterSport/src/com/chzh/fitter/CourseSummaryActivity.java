package com.chzh.fitter;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.chzh.fitter.adapter.CourseSummaryAdapter;
import com.chzh.fitter.download.ActionDownView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.DownloadManager;
import com.chzh.fitter.network.FileDownloadCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.CourseSummaryData;
import com.chzh.fitter.util.JSONUtil;
import com.chzh.fitter.util.L;
import com.chzh.fitter.view.HeaderButton;
import com.chzh.fitter.view.HeaderTextView;
import com.chzh.fitter.view.SlideBounceListView;
import com.chzh.fitter.view.SlideBounceListView.HeaderStickyListener;
import com.jarrah.blur.Blur;
import com.jw.progress.ProgressHUD;

public class CourseSummaryActivity extends SimpleTitleSActivity implements GlobalConstant, HeaderStickyListener{

	private SlideBounceListView mListView;
	private CourseSummaryAdapter mAdapter;
	
	private Bitmap mBackgroundBitmap;//正常效果的背景
	private Bitmap mBlurBackgroundBitmap;//模糊效果的背景
	
	private HeaderTextView mHeaderText;
	private HeaderButton mHeaderButton;
	
	//传递过来的数据源
	private CourseSummaryData mData;
	private ProgressHUD mProgressDialog;
	
	private boolean isBlurBackgroundUsed;
	private boolean isNormalBackgroundUsed;

	@Override
	protected void setupGUI() {
		mListView = findView(R.id.summary_list, SlideBounceListView.class);
		//add header
		mHeaderText = new HeaderTextView(this);
		mHeaderButton = new HeaderButton(this);
		mListView.addHeaderView(mHeaderButton);
		mListView.addHeaderView(mHeaderText);
		
		mAdapter = new CourseSummaryAdapter(this);
		mListView.setAdapter(mAdapter);
		mListView.enableScrollStickyHeader(findView(R.id.sticky_header));
		mListView.setHeaderStickyListener(this);
		
		bindClickEvent(findView(R.id.sticky_header), "startCourse");
		bindClickEvent(mHeaderButton.findViewById(R.id.add_course), "addCourse");
		bindClickEvent(mHeaderText, "startCourse");
		
		update();
	}
	
	//处理其他方案的数据
	private void update() {
		boolean isLock = mData.isLock();
		if (isLock) {
			showToast("由于你的等级关系,该课程不能播放.");
		}
		mAdapter.setCoursePlayEnable(!isLock);
		
		String bgUrl = mData.getCourseBg();
		setBackgroundRes(HOST_IP + bgUrl);
		mAdapter.setCourseBackground(mData.getCourseBg());
		
		updateActionList();
	}

	private void updateActionList() {
		JHttpManager httpManager = new JHttpManager(this);
		httpManager.get(mData.getActionsUrl(), new CodeCallBack(this, true) {
			
			@Override
			public void handleCallBack(JSONObject obj) {
				L.red("动作列表数据: " + obj);
				JSONArray array = JSONUtil.getJsonArrays(obj, "elem");
				mAdapter.setData(array);
			}
		}, null);
	}

	private void setBackgroundRes(String bgUrl) {
		DownloadManager dm = new DownloadManager(this);
		dm.downloadFile(bgUrl, 60 * 60 * 1000, new FileDownloadCallBack() {
			@Override
			public void onFileDownloaded(String url, File object) {
				mBackgroundBitmap = BitmapFactory.decodeFile(object.getAbsolutePath());
				mBlurBackgroundBitmap = Blur.fastblur(CourseSummaryActivity.this, mBackgroundBitmap, 117);
				setAppBackground(mBackgroundBitmap);
			}
		});
	}
	
	/**
	 * 设置正常的背景
	 */
	private void setNormalBackground() {
		if (mBackgroundBitmap != null && !isNormalBackgroundUsed) {
			setAppBackground(mBackgroundBitmap);
			isNormalBackgroundUsed = true;
			isBlurBackgroundUsed = false;
		}
	}
	
	/**
	 * 设置模糊的背景
	 */
	private void setBulurBackground() {
		if (mBlurBackgroundBitmap != null && !isBlurBackgroundUsed ) {
			setAppBackground(mBlurBackgroundBitmap);
			isBlurBackgroundUsed = true;
			isNormalBackgroundUsed = false;
		}
	}

	public void addCourse(View v) {
		showToast("已添加课程.");
	}

	/**
	 * 开始播放视频课程
	 * @param v
	 */
	public void startCourse(View v) {
		if (mData.isLock())
			showToast("没有权限播放该视频");
		else {
			ActionDownView view = new ActionDownView(this, mAdapter.getCoursePlayList());
			mProgressDialog = ProgressHUD.showDownload(this, view, null);
			view.dismissDialogWhenDownloaded(mProgressDialog);
		}
	}

	@Override
	protected String getTitleName() {
		String title = mData.getCourseTitle();
		title = title == null ? "基础课程" : title;
		return title;
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_course_summary;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBackgroundBitmap != null) {
			mBackgroundBitmap.recycle();
		}
		
		if (mBlurBackgroundBitmap != null) {
			mBlurBackgroundBitmap.recycle();
		}
	}
	
	@Override
	protected void setupViews() {
		//get data
		mData = (CourseSummaryData) getIntent().getSerializableExtra("course_data");
		if(mData == null) {showToast("无法获取数据"); finish(); return;}
		
		super.setupViews();
	}
	
	@Override
	public void onBackPressed() {
		if(mProgressDialog != null)
			mProgressDialog.dismiss();
		super.onBackPressed();
	}

	@Override
	public void onHeaderStickyTop() {
		setBulurBackground();
	}

	@Override
	public void onHeaderScroll() {
		setNormalBackground();
	}
}
