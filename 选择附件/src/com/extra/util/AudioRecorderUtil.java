package com.extra.util;

import android.content.Context;
import android.media.MediaRecorder;

import com.extra.res.StringRes;

/**
 * 
 * @author tangjiabing
 * 
 * @see ��Դʱ�䣺2016��03��31��
 * 
 *      �ǵø��Ҹ�starŶ~
 * 
 */
public class AudioRecorderUtil {

	private static MediaRecorder mRecorder = null;
	private static boolean mIsRecording = false;

	public static boolean startRecord(Context context, String audioSavePath) {
		if (mRecorder == null)
			mRecorder = new MediaRecorder();
		else
			mRecorder.reset();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // ��˷���Դ
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // ���3GP��ʽ
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // AMR��խ������Ƶ�������
		mRecorder.setAudioSamplingRate(8000); // AMR_NB is 8kHz
		mRecorder.setOutputFile(audioSavePath);
		try {
			mRecorder.prepare();
			mRecorder.start();
			mIsRecording = true;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			ResUtil resUtil = new ResUtil(context);
			ToastUtil.toast(context, resUtil
					.getIdFromString(StringRes.extra_start_record_audio_fail));
			return false;
		}
	}

	public static boolean isRecording() {
		return mIsRecording;
	}

	public static void stopRecord() {
		if (mRecorder != null && mIsRecording) {
			mRecorder.stop();
			mIsRecording = false;
		}
	}

	public static void releaseRecorder() {
		stopRecord();
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}
	}

}
