package com.chzh.fitter.video;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.chzh.fitter.R;
import com.chzh.fitter.framework.BaseView;
import com.chzh.fitter.util.L;

public class VideoControllerView extends BaseView implements OnSeekBarChangeListener{

	private static final int VIDEO_SURFACE_ONLY = 2;//表示还没有添加ViewController
	private static final int DEFAULT_DISMISS_DURATION = 3000;//默认消失时间3S
	private static final int VIDEO_CONTROLLER_ADDED = 3;

	private MediaPlayerControl	mMediaPlayerControl;
	private FrameLayout   	   	mAnchorView;
	private FrameLayout 		mVolParent;
	private SeekBar 			mAudioSeekBar;
	private AudioManager 		mAudioManager;
	private int				    mLastProgress;

	public VideoControllerView(Context context) {
		super(context);
	}

	public VideoControllerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setupViews() {
		setContentView(R.layout.media_controller);
		mVolParent = findView(R.id.vol_parent, FrameLayout.class);
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		initAudioSeekBar();
		initNextAndPre();
	}

	private void initNextAndPre() {
		bindClickEvent(findView(R.id.prev), "onPreClick");
		bindClickEvent(findView(R.id.next), "onNextClick");
	}

	public void onPreClick(View v) {
		mMediaPlayerControl.onPre();
	}

	public void onNextClick(View v) {
		mMediaPlayerControl.onNext();
	}

	private void initAudioSeekBar() {
		mAudioSeekBar = findView(R.id.audio_seek, SeekBar.class);
		mAudioSeekBar.setOnSeekBarChangeListener(this);
		updateAudioProgress();
	}

	public void setMediaPlayer(MediaPlayerControl mediaPlayerControl) {
		mMediaPlayerControl = mediaPlayerControl;
	}

	public void show() {
		setVisibility(View.VISIBLE);
		dismissAtTime(DEFAULT_DISMISS_DURATION);
	}

	public void setAnchorView(FrameLayout viewGroup) {
		mAnchorView = viewGroup;
		bindClickEvent(mAnchorView, "onAnchorTouch");
		bindClickEvent(findView(R.id.pause), "onPauseClick");
		bindClickEvent(findView(R.id.audio), "onAudioClick");
	}

	public void onAnchorTouch(View v) {
		L.red("onAnchorTouch");

		if (mAnchorView.getChildCount() == VIDEO_SURFACE_ONLY) {
			mAnchorView.addView(this);
		}

		if (mAnchorView.getChildCount()  == VIDEO_CONTROLLER_ADDED) { //说明已经把video控件添加进去了
			setVisibility(View.VISIBLE);
		}
		dismissAtTime(DEFAULT_DISMISS_DURATION);
	}

	public void onPauseClick(View v) {
		if(mMediaPlayerControl.isPlaying()){
			mMediaPlayerControl.pause();
		}else {
			mMediaPlayerControl.start();
		}
		updatePauseButton(mMediaPlayerControl.isPlaying());
	}

	private void updatePauseButton(boolean isPlaying) {
		if (isPlaying) {
			findView(R.id.pause, ImageButton.class).setImageResource(R.drawable.ic_media_pause);
		}else {
			findView(R.id.pause, ImageButton.class).setImageResource(R.drawable.ic_media_play);
		}
	}

	public void onAudioClick(View v) {
		L.red("audio");
		updateAudioProgress();
		mVolParent.setVisibility(View.VISIBLE);
	}

	private void dismissAtTime(int duration) {
		postDelayed(new Runnable() {

			@Override
			public void run() {
					mVolParent.setVisibility(View.GONE);
					VideoControllerView.this.setVisibility(View.GONE);
			}
		}, duration);
	}

	public interface MediaPlayerControl {
		void start();

		void onNext();

		void onPre();

		void pause();

		int getDuration();

		int getCurrentPosition();

		void seekTo(int pos);

		boolean isPlaying();

		int getBufferPercentage();

		boolean canPause();
	}


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
			float adjust = (progress - mLastProgress) / (float)seekBar.getMax();
			L.red("adjust : " + adjust);
			adjustAudio(adjust);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		updateAudioProgress();
	}

	public void adjustAudio(float progress) {
		int tag = progress > 0 ? AudioManager.ADJUST_RAISE: AudioManager.ADJUST_LOWER;
		progress = Math.abs(progress);
		int adjust = (int)Math.round(progress);
		mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, tag, adjust);
	}

	public void updateAudioProgress() {
		float max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float cur = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float progress = cur / max * 100;
		L.red("max " + max);
		L.red("cur " + cur);
		L.red("current progress "  + progress);
		mAudioSeekBar.setProgress((int)progress);
		mLastProgress = mAudioSeekBar.getProgress();
	}


}