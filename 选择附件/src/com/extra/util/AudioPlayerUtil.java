package com.extra.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.extra.res.StringRes;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class AudioPlayerUtil {

	private static MediaPlayer mPlayer = null;
	private static OnAudioCompleteListener mListener = null;

	public static boolean startPlay(Context context, String audioLocalPath) {
		if (mPlayer == null)
			mPlayer = new MediaPlayer();
		else
			mPlayer.reset();
		try {
			mPlayer.setDataSource(audioLocalPath);
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setOnCompletionListener(new AudioCompletionListener());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			ResUtil resUtil = new ResUtil(context);
			ToastUtil.toast(context,
					resUtil.getIdFromString(StringRes.extra_play_audio_fail));
			return false;
		}
	}

	public static boolean isPlaying() {
		if (mPlayer != null)
			return mPlayer.isPlaying();
		return false;
	}

	public static void setOnAudioCompleteListener(
			OnAudioCompleteListener listener) {
		mListener = listener;
	}

	public static void stopPlay() {
		if (mPlayer != null && mPlayer.isPlaying())
			mPlayer.stop();
	}

	public static void releasePlayer() {
		stopPlay();
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}

	// ******************************************************************************
	// 自定义的类

	public interface OnAudioCompleteListener {
		public void complete();
	}

	private static class AudioCompletionListener implements
			OnCompletionListener {
		@Override
		public void onCompletion(MediaPlayer mp) {
			if (mListener != null)
				mListener.complete();
		}
	}

}
