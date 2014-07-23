package com.chzh.fitter.core;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;

public class VideoPlayer implements OnBufferingUpdateListener,
		OnCompletionListener, MediaPlayer.OnPreparedListener,
		SurfaceHolder.Callback {

	private int mVideoWidth;
	private int mVideoHeight;

	public MediaPlayer mMdeMediaPlayer;
	private SurfaceHolder mSurfaceHolder;


	private SeekBar mSeekBar;
	private Timer mTimer = new Timer();

	public VideoPlayer(SurfaceView surfaceView, SeekBar skbProgress) {
		this.mSeekBar = skbProgress;
		mSurfaceHolder = surfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mTimer.schedule(mTimerTask, 0, 1000);
	}

	//handler 跟 timer 一起更新进度

	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mMdeMediaPlayer == null)
				return;
			if (mMdeMediaPlayer.isPlaying() && mSeekBar.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {

			int position = mMdeMediaPlayer.getCurrentPosition();
			int duration = mMdeMediaPlayer.getDuration();

			if (duration > 0) {
				long pos = mSeekBar.getMax() * position / duration;
				mSeekBar.setProgress((int) pos);
			}
		};
	};

	// *****************************************************

	public void play() {
		mMdeMediaPlayer.start();
	}

	public void playUrl(String videoUrl) {
		try {
			mMdeMediaPlayer.reset();
			mMdeMediaPlayer.setDataSource(videoUrl);
			mMdeMediaPlayer.prepare();// prepare之后自动播放
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		mMdeMediaPlayer.pause();
	}

	public void stop() {
		if (mMdeMediaPlayer != null) {
			mMdeMediaPlayer.stop();
			mMdeMediaPlayer.release();
			mMdeMediaPlayer = null;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		Log.e("mediaPlayer", "surface changed");
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		try {
			mMdeMediaPlayer = new MediaPlayer();
			mMdeMediaPlayer.setDisplay(mSurfaceHolder);
			mMdeMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMdeMediaPlayer.setOnBufferingUpdateListener(this);
			mMdeMediaPlayer.setOnPreparedListener(this);
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}
		Log.e("mediaPlayer", "surface created");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.e("mediaPlayer", "surface destroyed");
	}

	@Override
	/**
	 * 通过onPrepared播放
	 */
	public void onPrepared(MediaPlayer arg0) {
		mVideoWidth = mMdeMediaPlayer.getVideoWidth();
		mVideoHeight = mMdeMediaPlayer.getVideoHeight();
		if (mVideoHeight != 0 && mVideoWidth != 0) {
			arg0.start();
		}
		Log.e("mediaPlayer", "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {

	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		mSeekBar.setSecondaryProgress(bufferingProgress);
		int currentProgress = mSeekBar.getMax()
				* mMdeMediaPlayer.getCurrentPosition()
				/ mMdeMediaPlayer.getDuration();
		Log.e(currentProgress + "% play", bufferingProgress + "% buffer");

	}

}
