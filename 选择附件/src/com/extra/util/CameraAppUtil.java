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
public class CameraAppUtil {

	private static final String PICTURE_SUFFIX = ".jpg";
	private static String mCurrentPicturePath = null;

	public static void openCameraApp(Activity activity, String pictureDir) {
		mCurrentPicturePath = pictureDir + File.separator
				+ System.currentTimeMillis() + PICTURE_SUFFIX;
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(mCurrentPicturePath)));
		activity.startActivityForResult(intent, ExtraGlobal.REQUEST_CODE_CAMERA);
	}

	public static ResultBean onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		boolean result = false;
		String path = null;
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == ExtraGlobal.REQUEST_CODE_CAMERA) {
				path = mCurrentPicturePath;
				result = true;
			}
		}
		ResultBean bean = new ResultBean(result, path);
		return bean;
	}

}
