package com.chzh.fitter.video;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

import com.chzh.fitter.data.SingleAction;
import com.chzh.fitter.struct.CourseActionData;
import com.chzh.fitter.util.L;

public class CourseMediaManager1 extends MediaWrapper {

	// 课程播放的资源
	private HashMap<String, ArrayList<CourseActionData>> mCourseMap;

	// 课程播放完毕的监听器
	private CoursePlayListener mOnCoursePlayListener;

	// 每个动作播放完毕的时间记录
	private LinkedHashMap<String, Float> mCoursePlayRecord;

	private Timer mRestTimer;

	public CourseMediaManager1(Context context) {
		super(context);
		mCourseMap = new HashMap<String, ArrayList<CourseActionData>>();
		mCoursePlayRecord = new LinkedHashMap<String, Float>();
		mRestTimer = new Timer();
	}

	/**
	 * 播放课程
	 * 
	 * @param whichPlayer
	 * @param course
	 * @param startIndex
	 */
	public void playCourse(String whichPlayer,
			ArrayList<CourseActionData> course, int startIndex) {
		if (!mCourseMap.containsKey(whichPlayer)) {
			// set course
			mCourseMap.put(whichPlayer, course);
		}

		if (getMediaPlayer(whichPlayer).isPlaying()) {
			getMediaPlayer(whichPlayer).stop();
		}

		CourseActionData currentAction = getSingleActionAt(whichPlayer,
				startIndex);
		playAction(whichPlayer, currentAction);
	}

	private CourseActionData getSingleActionAt(String whichPlayer, int index) {
		return mCourseMap.get(whichPlayer) == null ? null : mCourseMap.get(
				whichPlayer).get(index);
	}

	private int _ActionVideoIndex = 0; // range 0 ~ 2
	private int _CourseVideoIndex = 0; // range 0 ~ course.size()
	private ActionVideoHandler _ActionVideoHandler;

	private void playAction(String whichPlayer, CourseActionData action) {

		// 如果是休息动作
		if (isRest(action)) {
			rest(whichPlayer, action);
			return;
		}

		String videoPreview = action.getPreviewFilePath();
		String videoIntroduce = action.getIntroduceFilePath();
		String video = action.getMainVideoFilePath();

		L.red("playing action title : " + action.getActionTitle());

		_ActionVideoHandler = new ActionVideoHandler(whichPlayer, action,
				videoIntroduce, videoPreview, video);
		
		// 每个动作从0开始播放
		playVideosFromFile(whichPlayer, 0, videoIntroduce, videoPreview, video);

		if (mOnCoursePlayListener != null) {
			mOnCoursePlayListener.onCoursePlaying(_CourseVideoIndex, action);
		}

		// when single action finish play next action
		_ActionVideoHandler
				.setActionPlayListener(new ActionPlayListener() {
					@Override
					public void onComplete(String whichPlayer) {
						totalVideoPlayDuration(whichPlayer, _CourseVideoIndex,
								getMainActionPlayDuration(whichPlayer));
						
						if (isCoursePlayOver(whichPlayer)) {
							// reset for next playing
							_CourseVideoIndex = 0;
							// call back
							if (mOnCoursePlayListener != null)
								mOnCoursePlayListener
										.onCourseComplete(getCoursePlayInfo());
						} else {
							// totalVideoPlayDuration(whichPlayer,
							// _CourseVideoIndex,
							// getMainActionPlayDuration(whichPlayer));
							playNextAction(whichPlayer);
						}
					}
				});
	}

	/**
	 * 处理需要休息的动作
	 */
	private void rest(final String whichPlayer, final CourseActionData data) {
		L.red("rest...");
		if (mOnCoursePlayListener != null) {
			mOnCoursePlayListener.onRestStart(_CourseVideoIndex, data);
		}

		// 30秒后处理下个个动作
		mRestTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				L.red("rest over");
				if (mOnCoursePlayListener != null) {
					mOnCoursePlayListener.onRestOver();
				}
				playNextAction(whichPlayer);
			}
		}, 30 * 1000);
	}

	/**
	 * 判断这个动作是不是为休息
	 * 
	 * @param action
	 * @return
	 */
	private boolean isRest(CourseActionData action) {
		return action.getActionType() == 2;
	}

	/**
	 * 播放下一个动作
	 * 
	 * @param whichPlayer
	 */
	public void playNextAction(String whichPlayer) {
		_CourseVideoIndex++;

		// 处理最后一个action(用户多次按播放下一个动作的异常处理)
		_CourseVideoIndex = _CourseVideoIndex >= mCourseMap.get(whichPlayer).size() ?
				mCourseMap.get(whichPlayer).size() - 1
				: _CourseVideoIndex;
		
		_ActionVideoIndex = 0;
		playAction(whichPlayer, getActionAt(whichPlayer, _CourseVideoIndex));
	}

	/**
	 * 播放上一个动作
	 * 
	 * @param whichPlayer
	 */
	public void playPreAction(String whichPlayer) {
		_CourseVideoIndex--;

		// 处理第一个action(用户多次按播放上一个动作的异常处理)
		_CourseVideoIndex = _CourseVideoIndex < 0 ? 0 : _CourseVideoIndex;
		
		_ActionVideoIndex = 0;
		playAction(whichPlayer, getActionAt(whichPlayer, _CourseVideoIndex));
	}

	private CourseActionData getActionAt(String whichPlayer, int index) {
		return mCourseMap.get(whichPlayer) == null ? null : mCourseMap.get(
				whichPlayer).get(index);
	}

	/**
	 * 课程播放完毕
	 * 
	 * @param whichPlayer
	 * @return
	 */
	private boolean isCoursePlayOver(String whichPlayer) {
		return _CourseVideoIndex == mCourseMap.get(whichPlayer).size() - 1;
	}

	private OnPreparedListener _ActionPrepearListener = new OnPreparedListener() {
		@Override
		public void onPrepared(MediaPlayer mp) {
			mp.start();
		}
	};

	/**
	 * handle action video play. loop play the preview video, introduce, and the
	 * main video. this will call back when main video end
	 * 
	 * @author jarrahwu
	 * 
	 */
	public class ActionVideoHandler implements OnCompletionListener {

		private String whichPlayer;
		private String[] res;
		private ActionPlayListener actionPlayListener;
		private CourseActionData data;

		public ActionVideoHandler(String whichPlayer, CourseActionData data,
				String... res) {
			this.whichPlayer = whichPlayer;
			this.res = res;
			this.data = data;
		}

		public void setActionPlayListener(ActionPlayListener l) {
			this.actionPlayListener = l;
		}

		@Override
		public void onCompletion(MediaPlayer mp) {

			if (_ActionVideoIndex == 0) { // 处理预览
				mOnCoursePlayListener.onPreviewStart(data);
			} else {
				mOnCoursePlayListener.onPreviewEnd(data);
			}

			// if the action video end
			if (_ActionVideoIndex >= res.length - 1) {
				if (actionPlayListener != null){
					// reset index for next playing
					_ActionVideoIndex = 0;
					actionPlayListener.onComplete(whichPlayer);
					return;
				}
			}

			// else
			_ActionVideoIndex++;
			playVideosFromFile(whichPlayer, _ActionVideoIndex, res);
		}
	}

	private void playVideosFromFile(String whichPlayer, int startIndex,
			String... res) {
		MediaPlayer player = getMediaPlayer(whichPlayer);
		
		L.red("action play index : " + startIndex + " path:" + res[startIndex]);
		
		prepearFile(whichPlayer, res[startIndex]);// prepare
		player.setOnPreparedListener(_ActionPrepearListener);// when prepared
																// start
		if(startIndex == 0 && mOnCoursePlayListener != null) {
			mOnCoursePlayListener.onIntroduceStart();
		}
		
		if (startIndex != 0 && mOnCoursePlayListener != null) {
			mOnCoursePlayListener.onIntoruceEnd();
		}
		
		player.setOnCompletionListener(_ActionVideoHandler);// when complete
															// play next
	}

	public interface ActionPlayListener {
		void onComplete(String whichPlayer);
	}

	/**
	 * @author jarrahwu 课程播放的监听器
	 */
	public interface CoursePlayListener {
		// 课程播放完毕之后,返回每个Action的播放时长
		void onCourseComplete(LinkedHashMap<String, Float> courseDurationMap);

		void onIntoruceEnd();

		void onIntroduceStart();

		void onPreviewEnd(CourseActionData data); // 介绍动作结束

		void onPreviewStart(CourseActionData data); // 介绍动作开始

		void onRestStart(int coursePosition, CourseActionData data);

		void onRestOver();// 休息结束

		void onCoursePlaying(int positon, CourseActionData action);
	}

	public void setOnCoursePlayListener(CoursePlayListener l) {
		mOnCoursePlayListener = l;
	}

	public void manualPlayNext(String whichPlayer) {
		float lastPlayingDuration = getMainActionPlayDuration(whichPlayer); // convert
																			// to
																			// second
		totalVideoPlayDuration(whichPlayer, _CourseVideoIndex,
				lastPlayingDuration);
		// 如果是休息的动作再跳下一个
		if (isRest(mCourseMap.get(whichPlayer).get(_CourseVideoIndex + 1)))
			_CourseVideoIndex++;

		playNextAction(whichPlayer);
	}

	public void manualPlayPre(String whichPlayer) {
		float lastPlayingDuration = getMainActionPlayDuration(whichPlayer); // convert
																			// to
																			// second
		totalVideoPlayDuration(whichPlayer, _CourseVideoIndex,
				lastPlayingDuration);

		// 如果是休息的动作再跳上一个
		if (isRest(mCourseMap.get(whichPlayer).get(_CourseVideoIndex - 1)))
			_CourseVideoIndex--;
		playPreAction(whichPlayer);
	}

	/**
	 * 获取正片播放的时间
	 * 
	 * @param whichPlayer
	 * @return
	 */
	public float getMainActionPlayDuration(String whichPlayer) {
		if (_ActionVideoIndex == 2) {
			return getMediaPlayer(whichPlayer).getCurrentPosition() / 1000.0f;
		} else {
			return 0.0f;
		}
	}

	/**
	 * 统计播放时长
	 * 
	 * @param courseVideoIndex
	 *            视频课程的下标
	 * @param duration
	 *            播放的时长
	 */
	private void totalVideoPlayDuration(String whichPlayer,
			int courseVideoIndex, float duration) {
		int actionId = mCourseMap.get(whichPlayer).get(_CourseVideoIndex)
				.getActionId();
		L.red("duration added : " + duration + " key is :" + actionId);
		mCoursePlayRecord.put("" + actionId, duration);
	}

	/**
	 * 获取课程播放的信息
	 * 
	 * @return 返回的Map key是video id, 对应的是该Video播放的时间
	 */
	public LinkedHashMap<String, Float> getCoursePlayInfo() {
		return mCoursePlayRecord;
	}

	public void cancelTimerTask() {
		mRestTimer.cancel();
	}
}
