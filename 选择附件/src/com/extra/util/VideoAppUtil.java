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
 * @see ��Դʱ�䣺2016��03��31��
 * 
 *      �ǵø��Ҹ�starŶ~
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
		// �洢·����Ĭ�ϴ洢��ý�����
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(mCurrentVideoPath)));
		// ͼ��������������ֵ��0��ʾ��������Ƶ��MMS����1��ʾ��������Ƶ��ȫ�ֱ��ʣ�
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		// ¼�����ʱ������λΪ��
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
