package com.extra.util;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.extra.bean.ResultBean;
import com.extra.global.AudioGlobal;
import com.extra.global.ExtraGlobal;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class AudioAppUtil {

	private static final String AUDIO_SUFFIX = ".mp3";
	private static String mCurrentAudioPath = null;

	public static void openAudioApp(Activity activity, String audioDir,
			int maxRecordTime) {
		mCurrentAudioPath = audioDir + File.separator
				+ System.currentTimeMillis() + AUDIO_SUFFIX;
		Intent intent = new Intent();
		intent.setClassName(activity, ExtraGlobal.CLASS_NAME_AUDIO_APP);
		intent.putExtra(AudioGlobal.KEY_LOCAL_PATH, mCurrentAudioPath);
		intent.putExtra(AudioGlobal.KEY_RECORD_TIME, maxRecordTime);
		activity.startActivityForResult(intent, ExtraGlobal.REQUEST_CODE_AUDIO);
	}

	/**
	 * 使用系统自带的录音App，音频无法保存到指定的路径中
	 * 
	 * @param activity
	 * @param audioDir
	 * @param maxRecordTime
	 */
	public static void openSystemAudioApp(Activity activity, String audioDir,
			int maxRecordTime) {
		mCurrentAudioPath = audioDir + File.separator
				+ System.currentTimeMillis() + AUDIO_SUFFIX;
		Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(mCurrentAudioPath)));
		activity.startActivityForResult(intent, ExtraGlobal.REQUEST_CODE_AUDIO);
	}

	public static ResultBean onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		boolean result = false;
		String path = null;
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == ExtraGlobal.REQUEST_CODE_AUDIO) {
				path = mCurrentAudioPath;
				result = true;
			}
		}
		ResultBean bean = new ResultBean(result, path);
		return bean;
	}

}
