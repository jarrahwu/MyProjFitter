package com.chzh.fitter.video;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.chzh.fitter.R;
import com.chzh.fitter.data.SingleAction;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.framework.SimpleTitleSActivity;
import com.chzh.fitter.network.DownloadManager;
import com.chzh.fitter.network.FileDownloadCallBack;
import com.chzh.fitter.struct.CourseActionData;

public class ActionPreviewActivity extends SimpleTitleSActivity implements OnCompletionListener, OnPreparedListener{

	private CourseActionData mCouseActionData;

	private VideoView mVideoView;

	private TextView mIntroduce;

	private Timer mPlayDelayTimer;

	protected Bitmap mBackgroundBitmap;

	@Override
	protected void setupGUI() {
		mPlayDelayTimer = new Timer();

		mVideoView = findView(R.id.action_video, VideoView.class);
		mVideoView.setVideoPath(mCouseActionData.getPreviewFilePath());//
		mVideoView.setOnCompletionListener(this);
		mVideoView.setOnPreparedListener(this);

		mIntroduce = findView(R.id.action_intro, TextView.class);
		mIntroduce.setText(mCouseActionData.getActionDescription());

		bindClickEvent(findView(R.id.play), "onPlayClick");
		bindClickEvent(findView(R.id.video_parent), "onVideoClick");
//		setBackgroundRes(mCouseActionData.getBackgroundUrl());
	}

	public void onPlayClick(View v) {
		if (!mVideoView.isPlaying()) {
			mVideoView.start();
			gone(R.id.play);
		}
	}

	public void onVideoClick(View v) {
		if (mVideoView.isPlaying()) {
			mVideoView.pause();
			findView(R.id.play, ImageView.class).setImageResource(R.drawable.ic_media_pause);
			visible(R.id.play);
		}else {
			onPlayClick(mVideoView);
		}
	}

	@Override
	protected String getTitleName() {
		mCouseActionData = (CourseActionData) getIntent().getSerializableExtra("single_action");
		return mCouseActionData == null ? "动作介绍" : mCouseActionData.getActionTitle();
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_action_introduce;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		visible(R.id.play);
		findView(R.id.play, ImageView.class).setImageResource(R.drawable.ic_media_play);
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mPlayDelayTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						onPlayClick(findView(R.id.play));
					}
				});
			}
		}, 1 * 1000);
	}
	
	private void setBackgroundRes(String bgUrl) {
		bgUrl = GlobalConstant.HOST_IP + bgUrl;
		DownloadManager dm = new DownloadManager(this);
		dm.downloadFile(bgUrl, 60 * 60 * 1000, new FileDownloadCallBack() {
			@Override
			public void onFileDownloaded(String url, File object) {
				mBackgroundBitmap = BitmapFactory.decodeFile(object.getAbsolutePath());
				setAppBackground(mBackgroundBitmap);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mBackgroundBitmap != null)
			mBackgroundBitmap.recycle();
			mBackgroundBitmap = null;
	}

}
