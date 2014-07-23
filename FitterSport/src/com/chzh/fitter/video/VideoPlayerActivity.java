package com.chzh.fitter.video;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chzh.fitter.CourseCommentActivity;
import com.chzh.fitter.R;
import com.chzh.fitter.core.UICore;
import com.chzh.fitter.framework.GlobalConstant;
import com.chzh.fitter.network.CodeCallBack;
import com.chzh.fitter.network.JHttpManager;
import com.chzh.fitter.struct.CourseActionData;
import com.chzh.fitter.struct.CourseCommentData;
import com.chzh.fitter.struct.PlayListData;
import com.chzh.fitter.util.L;
import com.chzh.fitter.video.CourseMediaManager1.CoursePlayListener;
import com.chzh.fitter.view.RoundImageView;

public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback, VideoControllerView.MediaPlayerControl, CoursePlayListener, GlobalConstant {

    protected static final int REST = 0;
	protected static final int REST_OVER = 1;
	protected static final int VIDEO_TITLE = 2;


	SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;

    TextView mTitleTextView;

    CourseMediaManager1 mMediaManager;

    Handler mUIHandler;

    @SuppressLint("HandlerLeak")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_player);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);

        mTitleTextView = (TextView) findViewById(R.id.courseTitle);

//        player = new MediaPlayer();
        controller = new VideoControllerView(this);

        //调用wrapper
        mMediaManager = new CourseMediaManager1(this);
        mMediaManager.makeMedia("video");
        player = mMediaManager.getMediaPlayer("video");

        mUIHandler = new Handler() {
        	@Override
        	public void handleMessage(Message msg) {
        		switch (msg.what) {
				case REST:
					CourseActionData data = (CourseActionData) msg.obj;
					RoundImageView roundImageView = (RoundImageView) findViewById(R.id.rest_img);
					roundImageView.setVisibility(View.VISIBLE);
					roundImageView.ajaxImage(GlobalConstant.HOST_IP + data.getRestPic());
					Toast.makeText(VideoPlayerActivity.this, "休息30S", Toast.LENGTH_SHORT).show();
					break;

				case REST_OVER:
					findViewById(R.id.rest_img).setVisibility(View.GONE);
					 break;

				case VIDEO_TITLE:
					CourseActionData action = (CourseActionData) msg.obj;
					mTitleTextView.setText(action.getActionTitle());
					break;

				default:
					break;
				}
        	}
        };
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	player.setDisplay(holder);
    	controller.setMediaPlayer(VideoPlayerActivity.this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        
        //设置播放资源
        mMediaManager.setOnCoursePlayListener(this);
        mMediaManager.playCourse("video", getPlaySource(), 0);
    }

    /**
     * 获取播放资源
     * @return
     */
    private ArrayList<CourseActionData> getPlaySource() {
		PlayListData playListData = (PlayListData) getIntent().getSerializableExtra("play_list");
		return playListData.getList();
	}


	@Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

	@Override
	public void onNext() {
		findViewById(R.id.rest_img).setVisibility(View.GONE);
		findViewById(R.id.videoIntroduce).setVisibility(View.GONE);
		mMediaManager.manualPlayNext("video");
	}

	@Override
	public void onPre() {
		findViewById(R.id.videoIntroduce).setVisibility(View.GONE);
		findViewById(R.id.rest_img).setVisibility(View.GONE);
		mMediaManager.manualPlayPre("video");
	}

	@Override
	public void onCoursePlaying(int positon, CourseActionData action) {
		Message msg = new Message();
		msg.what = VIDEO_TITLE;
		msg.obj = action;
		mUIHandler.sendMessage(msg);
	}

	@Override
	public void onRestStart(int coursePosition, CourseActionData data) {
		Message msg = new Message();
		msg.what = REST;
		msg.obj = data;
		mUIHandler.sendMessage(msg);
	}

	@Override
	public void onRestOver() {
		mUIHandler.sendEmptyMessage(REST_OVER);
	}

	@Override //课程播放结束
	public void onCourseComplete(LinkedHashMap<String, Float> courseDurationMap) {
		Toast.makeText(this, "正在分析数据...", Toast.LENGTH_SHORT).show();
		//跳转到课程评价页面
		skipTo("comment_list", new CourseCommentData(compareData(courseDurationMap)), CourseCommentActivity.class);
		finish();
	}
	
	/**
	 * 对比数据 对比总数据, 与播放过的数据, 然后得出评价的数据
	 * @param courseDurationMap
	 */
	private ArrayList<CourseActionData> compareData(LinkedHashMap<String, Float> courseDurationMap) {
		ArrayList<CourseActionData> playSource = getPlaySource();
		ArrayList<CourseActionData> ret = new ArrayList<CourseActionData>();
		
		//key 是action id  Value 是 对应播放的时间
		for (String key : courseDurationMap.keySet()) {
			int actionId = Integer.valueOf(key);
			
			//遍历播放资源里面actionId 相同的
			for (int i = 0; i < playSource.size(); i++) {
				CourseActionData curData = playSource.get(i);
				
				if (actionId == curData.getActionId()) {
					curData.setPlayDuration(courseDurationMap.get(key));
					ret.add(curData);
					break;
				}
			}
		}
		
		return ret;
	}

	/**
	 * Activity跳转
	 * @param cls
	 */
	public void skipTo(String key, Serializable data, Class<?> cls) {
		Intent i = new Intent();
		i.setClass(this, cls);
		i.putExtra(key, data);
		startActivity(i);
	}

	@Override
	protected void onDestroy() {
		mMediaManager.cancelTimerTask();
		mUIHandler = null;
		super.onDestroy();
	}


	@Override
	public void onPreviewStart(CourseActionData data) {
		TextView tv = (TextView) findViewById(R.id.videoIntroduce);
		tv.setVisibility(View.VISIBLE);
		tv.setText(data.getActionDescription());
	}


	@Override
	public void onPreviewEnd(CourseActionData data) {
		findViewById(R.id.videoIntroduce).setVisibility(View.GONE);
	}


	@Override
	public void onIntoruceEnd() {
		findViewById(R.id.courseTitle).setVisibility(View.INVISIBLE);
	}


	@Override
	public void onIntroduceStart() {
		findViewById(R.id.courseTitle).setVisibility(View.VISIBLE);
	}

}
