package com.chzh.fitter.download;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseView;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.network.DownloadManager;
import com.chzh.fitter.network.FileDownloadCallBack;
import com.chzh.fitter.struct.CourseActionData;
import com.chzh.fitter.struct.PlayListData;
import com.chzh.fitter.util.L;
import com.chzh.fitter.video.ActionPreviewActivity;
import com.chzh.fitter.video.VideoPlayerActivity;
import com.jw.progress.ProgressHUD;

/**
 * 每个动作的下载器
 */
public class ActionDownView extends BaseView {

	public static final long VIDEO_EXPIRE = 0;//视频缓存时间
	
	private ProgressBar mDownloadProgressBar;
	private Button mRetryButton;
	private TextView mDownloadTitle;
	private CourseActionData mData;
	
	private DownloadManager mDownloadManager;
	
	private int mActionDownloadIndex = 0;
	
	private ArrayList<CourseActionData> mList; //播放列表的list

	private ProgressHUD progressHUD;

	public ActionDownView(Context context) {
		super(context);
	}

	public ActionDownView(Context context, CourseActionData data) {
		this(context);
		mData = data;
		startDownload();
	}
	
	public ActionDownView(Context context, ArrayList<CourseActionData> list) {
		this(context);
		mList = list;
		downloadList(list);
	}
	
	/**
	 * 下载整个课程的播放列表
	 * @param list
	 */
	private void downloadList(ArrayList<CourseActionData> list) {
		ActionDownloadHandler actionDownloadHandler = new ActionDownloadHandler(mContext, list.get(mActionDownloadIndex)) {
			@Override
			public void onActionDownloaded(CourseActionData data) {
				restoreListData(data);
				
				CourseActionData next = getNextdata();
				if ( next != null) {
					setDownloadData(next);
					this.startDownload();
				}else { // List 下载完毕 跳转到视频播放页面
					skipTo("play_list", new PlayListData(mList), VideoPlayerActivity.class);
					progressHUD.dismiss();
				}
				
			}
		};
		
		actionDownloadHandler.startDownload();
	}

	/**
	 * 覆盖数据
	 * @param data
	 */
	protected void restoreListData(CourseActionData data) {	
		L.red("intro:" + data.getIntroduceFilePath());
		L.red("pre:" + data.getPreviewFilePath());
		L.red("main:"+ data.getMainVideoFilePath());
		mList.set(mActionDownloadIndex, data);
	}

	/**
	 * 获取下一个下载的数据
	 * @return
	 */
	protected CourseActionData getNextdata() {
		mActionDownloadIndex ++;
		
		if (mActionDownloadIndex < mList.size()) {
			return mList.get(mActionDownloadIndex);
		}else {
			return null;
		}
		
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.view_action_download);
		mDownloadProgressBar = findView(R.id.download_action_progress,
				ProgressBar.class);
		mRetryButton = findView(R.id.retry, Button.class);
		mDownloadTitle = findView(R.id.download_action_title, TextView.class);
		
		mDownloadManager = new DownloadManager(mContext);
		
		ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
		if (imageView == null) {
			return;
		}
		AnimationDrawable spinner = (AnimationDrawable) imageView
				.getBackground();
		spinner.start();

		bindClickEvent(mRetryButton, "onRetry");
	}

	public void startDownload() {
		if (mData == null) {
			L.red("download data is null!");
			return;
		}
		
		mDownloadTitle.setText("正在下载 : " + mData.getActionTitle());
		String url = GlobalConstant.HOST_IP + mData.getVideoPreviewUrl();
		
		mDownloadManager.downloadFileWithProgress(url, VIDEO_EXPIRE, mDownloadProgressBar, new FileDownloadCallBack() {
			@Override
			public void onFileDownloaded(String url, File object) {
				mData.setPreviewFilePath(object.getAbsolutePath());
				//下载完毕跳转到预览并播放
				skipTo("single_action", mData, ActionPreviewActivity.class);
				progressHUD.dismiss();
			}
		});
	}
	
	public void onRetry(View v) {

	}
	
	/**
	 * @author jarrahwu
	 * 整个action视频的下载处理器
	 */
	public class ActionDownloadHandler extends FileDownloadCallBack {
		
		private CourseActionData _data;
		
		private String[] _URL;
		private static final int INTRO = 0;
		private static final int PRE = INTRO + 1;
		private static final int MAIN = PRE + 1;
		
		private int _urlIndex = 0;
		
		public ActionDownloadHandler(Context context, CourseActionData data) {
			_data = data;
			//配置下载的url
			_URL = new String[3];
			_URL[INTRO] = GlobalConstant.HOST_IP + data.getVideoIntroduceUrl();//介绍的URL
			_URL[PRE] = GlobalConstant.HOST_IP + data.getVideoPreviewUrl();//预览的URL
			_URL[MAIN] = GlobalConstant.HOST_IP + data.getMainViedoUrl();//正片URL
		}
		
		public void setDownloadData(CourseActionData data) {
			_data = data;
			//配置下载的url
			_URL = new String[3];
			_URL[INTRO] = GlobalConstant.HOST_IP + data.getVideoIntroduceUrl();//介绍的URL
			_URL[PRE] = GlobalConstant.HOST_IP + data.getVideoPreviewUrl();//预览的URL
			_URL[MAIN] = GlobalConstant.HOST_IP + data.getMainViedoUrl();//正片URL
		}
		
		public void startDownload() {
			mDownloadProgressBar.setIndeterminate(true);
//			mDownloadTitle.setText("正在下载课程资源 : " + _data.getActionTitle());
			mDownloadTitle.setText("正在下载课程资源 : ");
			mDownloadManager.downloadFileWithProgress(_URL[_urlIndex], VIDEO_EXPIRE, mDownloadProgressBar, this);
		}
		
		@Override
		public void onFileDownloaded(String url, File object) {
			L.red("Downloaded url :" + url);
			if (!url.equals(_URL[MAIN])) { //如果还没有下载到最后一条
				
				if (_urlIndex == INTRO) {
					_data.setIntroduceFileParh(object.getAbsolutePath());
				}
				
				if (_urlIndex == PRE) {
					_data.setPreviewFilePath(object.getAbsolutePath());
				}
				_urlIndex ++;
				//继续下载
				startDownload();
			}else { // 整个动作的视频下载完毕
				_urlIndex = 0;
				_data.setMainVideoFilePath(object.getAbsolutePath());
				onActionDownloaded(_data);
			}
		}
		
		public void onActionDownloaded(CourseActionData data) {
			
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mDownloadManager.cancelDownload();
	}

	public void dismissDialogWhenDownloaded(ProgressHUD progressHUD) {
		this.progressHUD = progressHUD;
	}
	
	
}
