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
import com.chzh.fitter.util.L;

public class CourseMediaManager extends MediaWrapper{

	//课程播放的资源
	private HashMap<String, ArrayList<SingleAction>> mCourseMap;

	//课程播放完毕的监听器
	private CoursePlayListener mOnCoursePlayListener;

	//每个动作播放完毕的时间记录
	private LinkedHashMap<String, Float> mCoursePlayRecord;

	private Timer mRestTimer;

	public CourseMediaManager(Context context) {
		super(context);
		mCourseMap = new HashMap<String, ArrayList<SingleAction>>();
		mCoursePlayRecord = new LinkedHashMap<String, Float>();
		mRestTimer = new Timer();
	}

	/**
	 * 播放课程
	 * @param whichPlayer
	 * @param course
	 * @param startIndex
	 */
	public void playCourse(String whichPlayer, ArrayList<SingleAction> course, int startIndex) {
		if(!mCourseMap.containsKey(whichPlayer)) {
			//set course
			mCourseMap.put(whichPlayer, course);
		}

		if (getMediaPlayer(whichPlayer).isPlaying()) {
			getMediaPlayer(whichPlayer).stop();
		}

		SingleAction currentAction = getSingleActionAt(whichPlayer, startIndex);
		playAction(whichPlayer, currentAction);
	}

	private SingleAction getSingleActionAt(String whichPlayer, int index) {
		return mCourseMap.get(whichPlayer) == null ? null : mCourseMap.get(whichPlayer).get(index);
	}

	private int _ActionVideoIndex = 0; // range 0 ~ 2
	private int _CourseVideoIndex = 0; // range 0 ~ course.size()
	private ActionVideoHandler _ActionVideoHandler;

	private void playAction(String whichPlayer, SingleAction action) {

		//如果是休息动作
		if(isRest(action)){
			rest(whichPlayer);
			return;
		}

		if (mOnCoursePlayListener != null) {
			mOnCoursePlayListener.onCoursePlaying(_CourseVideoIndex, action);
		}

		int videoPreview = action.getVideoPreview();
		int videoIntroduce = action.getVideoIntroduce();
		int video = action.getVideo();

		L.red("playing action title : " + action.getActionTitle());

		_ActionVideoHandler = new ActionVideoHandler(whichPlayer, videoPreview, videoIntroduce, video);
		playVideosFromRaw(whichPlayer, 0, videoPreview, videoIntroduce, video);

		//when single action finish play next action
		_ActionVideoHandler.setOnSingleActionCompleteListener(new OnSingleActionCompleteListener() {
			@Override
			public void onComplete(String whichPlayer) {
				if(courseComplete(whichPlayer)) {
					//reset for next playing
					_CourseVideoIndex = 0;
					//call back
					if(mOnCoursePlayListener != null) mOnCoursePlayListener.onCourseComplete(getCoursePlayInfo());
				}else {
					totalVideoPlayDuration(whichPlayer, _CourseVideoIndex, getMainActionPlayDuration(whichPlayer));
					playNextAction(whichPlayer);
				}
			}
		});
	}


	/**
	 * 处理需要休息的动作
	 */
	private void rest(final String whichPlayer) {
		L.red("rest...");
		if (mOnCoursePlayListener != null) {
			mOnCoursePlayListener.onRestStart(_CourseVideoIndex);
		}

		//30秒后处理下个个动作
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
	 * @param action
	 * @return
	 */
	private boolean isRest(SingleAction action) {
		//目前休息就是没有动作描述
		return action.getActionDescription().length() == 0;
	}

	/**
	 * 播放下一个动作
	 * @param whichPlayer
	 */
	public void playNextAction(String whichPlayer) {
		_CourseVideoIndex ++;

		//处理最后一个action(用户多次按播放下一个动作的异常处理)
		_CourseVideoIndex = _CourseVideoIndex >= mCourseMap.get(whichPlayer).size() ? mCourseMap.get(whichPlayer).size() - 1 : _CourseVideoIndex;

		playAction(whichPlayer, getActionAt(whichPlayer, _CourseVideoIndex));
	}

	/**
	 * 播放上一个动作
	 * @param whichPlayer
	 */
	public void playPreAction(String whichPlayer) {
		_CourseVideoIndex --;

		//处理第一个action(用户多次按播放上一个动作的异常处理)
		_CourseVideoIndex = _CourseVideoIndex < 0 ? 0 : _CourseVideoIndex;

		playAction(whichPlayer, getActionAt(whichPlayer, _CourseVideoIndex));
	}

	private SingleAction getActionAt(String whichPlayer, int index) {
		return mCourseMap.get(whichPlayer) == null ? null : mCourseMap.get(whichPlayer).get(index);
	}

	private boolean courseComplete(String whichPlayer) {
		return _CourseVideoIndex == mCourseMap.get(whichPlayer).size() -1;
	}

	private OnPreparedListener _ActionPrepearListener = new OnPreparedListener() {
		@Override
		public void onPrepared(MediaPlayer mp) {
			mp.start();
		}
	};

	/**
	 * handle action video play. loop play the preview video, introduce, and the main video. this will call back when main video end
	 * @author jarrahwu
	 *
	 */
	public class ActionVideoHandler implements OnCompletionListener{

		private String whichPlayer;
		private int[] res;
		private OnSingleActionCompleteListener onSingleActionCompleteListener;

		public ActionVideoHandler(String whichPlayer, int... res) {
			this.whichPlayer = whichPlayer;
			this.res = res;
		}

		public void setOnSingleActionCompleteListener(OnSingleActionCompleteListener l) {
			this.onSingleActionCompleteListener = l;
		}

		@Override
		public void onCompletion(MediaPlayer mp) {
			//if the action video end
			if(_ActionVideoIndex == res.length-1) {
				if(onSingleActionCompleteListener != null)
					onSingleActionCompleteListener.onComplete(whichPlayer);
				_ActionVideoIndex = 0;//reset index for next playing
				return;
			}
			//else
			_ActionVideoIndex ++;
			playVideosFromRaw(whichPlayer, _ActionVideoIndex, res);
		}
	}

	private void playVideosFromRaw(String whichPlayer, int startIndex, int... res) {
		MediaPlayer player = getMediaPlayer(whichPlayer);
		L.red("raw play index : " + startIndex);
		prepearRawFile(whichPlayer, res[startIndex]);//prepare
		player.setOnPreparedListener(_ActionPrepearListener);//when prepared start
		player.setOnCompletionListener(_ActionVideoHandler);//when complete play next
	}

	public interface OnSingleActionCompleteListener {
		void onComplete(String whichPlayer);
	}

	public interface CoursePlayListener {
		//课程播放完毕之后,返回每个Action的播放时长
		void onCourseComplete(LinkedHashMap<String, Float> courseDurationMap);
		void onRestStart(int coursePosition);
		void onRestOver();//休息结束
		void onCoursePlaying(int positon, SingleAction action);
	}

	public void setOnCoursePlayListener(CoursePlayListener l) {
		mOnCoursePlayListener = l;
	}

	public void manualPlayNext(String whichPlayer) {
		float lastPlayingDuration = getMainActionPlayDuration(whichPlayer); // convert to second
		totalVideoPlayDuration(whichPlayer, _CourseVideoIndex, lastPlayingDuration);
		//如果是休息的动作再跳下一个
		if(isRest(mCourseMap.get(whichPlayer).get(_CourseVideoIndex + 1))) _CourseVideoIndex ++;
		playNextAction(whichPlayer);
	}

	public void manualPlayPre(String whichPlayer) {
		float lastPlayingDuration = getMainActionPlayDuration(whichPlayer); // convert to second
		totalVideoPlayDuration(whichPlayer, _CourseVideoIndex, lastPlayingDuration);

		//如果是休息的动作再跳上一个
		if(isRest(mCourseMap.get(whichPlayer).get(_CourseVideoIndex - 1))) _CourseVideoIndex --;
		playPreAction(whichPlayer);
	}

	/**
	 * 获取正片播放的时间
	 * @param whichPlayer
	 * @return
	 */
	public float getMainActionPlayDuration(String whichPlayer) {
		if(_ActionVideoIndex == 2) {
			return getMediaPlayer(whichPlayer).getCurrentPosition() / 1000.0f;
		}else {
			return 0.0f;
		}
	}

	/**
	 * 统计播放时长
	 * @param courseVideoIndex 视频课程的下标
	 * @param duration 播放的时长
	 */
	private void totalVideoPlayDuration(String whichPlayer, int courseVideoIndex, float duration) {
		L.red("duration added : " + duration + " key is :" + courseVideoIndex);
		int actionId = mCourseMap.get(whichPlayer).get(_CourseVideoIndex).getActionId();
		mCoursePlayRecord.put("" + actionId, duration);
	}

	/**
	 * 获取课程播放的信息
	 * @return 返回的Map key是video id, 对应的是该Video播放的时间
	 */
	public LinkedHashMap<String, Float> getCoursePlayInfo() {
		return mCoursePlayRecord;
	}

	public void cancelTimerTask() {
		mRestTimer.cancel();
	}
}
