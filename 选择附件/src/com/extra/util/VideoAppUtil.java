package com.extra.util;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.extra.bean.ResultBean;
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
public class VideoAppUtil {

	private static final String VIDEO_SUFFIX = ".mp4";
	private static String mCurrentVideoPath = null;

	public static void openVideoApp(Activity activity, String videoDir,
			int maxRecordTime) {
		mCurrentVideoPath = videoDir + File.separator
				+ System.currentTimeMillis() + VIDEO_SUFFIX;
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		// 存储路径，默认存储在媒体库中
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(mCurrentVideoPath)));
		// 图像质量，有两种值：0表示低质量视频（MMS），1表示高质量视频（全分辨率）
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		// 录制最大时长，单位为秒
		intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, maxRecordTime);
		activity.startActivityForResult(intent, ExtraGlobal.REQUEST_CODE_VIDEO);
	}

	public static ResultBean onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		boolean result = false;
		String path = null;
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == ExtraGlobal.REQUEST_CODE_VIDEO) {
				path = mCurrentVideoPath;
				result = true;
			}
		}
		ResultBean bean = new ResultBean(result, path);
		return bean;
	}

}
