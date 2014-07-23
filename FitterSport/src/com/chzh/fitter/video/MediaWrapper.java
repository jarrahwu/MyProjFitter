package com.chzh.fitter.video;

import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.widget.Toast;

import com.chzh.fitter.util.L;

public class MediaWrapper {

	private Context mContext;

	private HashMap<String, MediaPlayer> mMediaMap;

	public MediaWrapper(Context context) {
		mContext = context;
		mMediaMap = new HashMap<String, MediaPlayer>();
	}

	public MediaPlayer makeMedia(String playerName) {
		if (!mMediaMap.containsKey(playerName)) {
			MediaPlayer player = new MediaPlayer();
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.setScreenOnWhilePlaying(true);
			mMediaMap.put(playerName, player);
		}
		return getMediaPlayer(playerName);
	}

	public MediaPlayer getMediaPlayer(String key){
		MediaPlayer player = mMediaMap.get(key);
		if (player == null) {
			L.red("player is not found is null");
		}
		return player;
	}

	public void clearMedia(String key) {
		if (mMediaMap.containsKey(key)) {
			mMediaMap.get(key).release();
			mMediaMap.remove(key);
		}
	}

	public void prepearFile(String whichPlayer, String filePath) {
		final MediaPlayer player = getMediaPlayer(whichPlayer);
		player.reset();
    	try {
			player.setDataSource(filePath);
			player.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void playFile(String whichPlayer, String filePath) {
		final MediaPlayer player = getMediaPlayer(whichPlayer);
		if (player == null) {
			return;
		}
		prepearFile(whichPlayer, filePath);
    	player.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				player.start();
			}
		});
	}

//	private int playIndex = 0;//播放的的数组标识
//	public void playFiles(final boolean isLoopPlay, final String whichPlayer, final String... filePaths) {
//		MediaPlayer player = getMediaPlayer(whichPlayer);
//
//		if(playIndex < filePaths.length)
//			playFile(whichPlayer, filePaths[playIndex]);
//
//		playIndex ++;
//
//		if(isLoopPlay)
//			playIndex = playIndex >= filePaths.length ? 0 : playIndex;
//
//
//		player.setOnCompletionListener(new OnCompletionListener() {
//
//			@Override
//			public void onCompletion(MediaPlayer mp) {
//				if(!isLoopPlay && playIndex == filePaths.length) return;
//
//				playFiles(isLoopPlay, whichPlayer, filePaths);
//			}
//		});
//	}


	
	
	
//	---------------------------------------------------------------------------
	

	public void prepearRawFile(String whichPlayer, int rawId) {
		final MediaPlayer player = getMediaPlayer(whichPlayer);
		AssetFileDescriptor afd = mContext.getResources().openRawResourceFd(
				rawId);

		player.reset();
		try {
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getDeclaredLength());
			player.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

//	/**
//	 * 播放本地的资源文件
//	 * @param whichPlayer
//	 * @param rawId
//	 */
//	public void playRawFile(String whichPlayer, int rawId) {
//		final MediaPlayer player = getMediaPlayer(whichPlayer);
//		if (player == null) {
//			return;
//		}
//		prepearRawFile(whichPlayer, rawId);
//		player.setOnPreparedListener(new OnPreparedListener() {
//			@Override
//			public void onPrepared(MediaPlayer mp) {
//				player.start();
//			}
//		});
//	}

//	private int playRawIndex;
//	private int curRawIndex;
//	private int[] theRawIds;
//	public void playRawFiles(final boolean isLoopPlay, final String whichPlayer, final int... rawIds) {
//		final MediaPlayer player = getMediaPlayer(whichPlayer);
//		theRawIds = rawIds;
//		if(playRawIndex < rawIds.length) {
//			playRawFile(whichPlayer, rawIds[playRawIndex]);
//			L.red(curRawIndex);
//			curRawIndex = playRawIndex;
//		}
//
//		playRawIndex ++;
//
//		if(isLoopPlay)
//			playRawIndex = playRawIndex >= rawIds.length ? 0 : playRawIndex;
//
//
//		player.setOnCompletionListener(new OnCompletionListener() {
//
//			@Override
//			public void onCompletion(MediaPlayer mp) {
//				if(!isLoopPlay && playRawIndex == rawIds.length) {//the end
//					player.release();
//					Toast.makeText(mContext, "这已经是最后的一个动作了!", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				playRawFiles(isLoopPlay, whichPlayer, rawIds);
//			}
//		});
//	}

//	public void playNextRaw(String whichPlayer) {
//		curRawIndex ++;
//		if (curRawIndex >=theRawIds.length) {
//			Toast.makeText(mContext, "这是最后一个动作了!", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		playRawIndex = curRawIndex;
//		getMediaPlayer(whichPlayer).stop();
//		playRawFiles(false, whichPlayer, theRawIds);
//	}
//
//	public void playPre(String whichPlayer) {
//		curRawIndex --;
//		if (curRawIndex < 0) {
//			curRawIndex = 0;
//			Toast.makeText(mContext, "这是第一个动作了!", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		playRawIndex = curRawIndex;
//		getMediaPlayer(whichPlayer).stop();
//		playRawFiles(false, whichPlayer, theRawIds);
//	}
}
