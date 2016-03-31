package com.extra.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

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
public class GalleryAppUtil {

	public static void openGalleryApp(Activity activity) {
		boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		// 在4.3或以下，可以直接用ACTION_GET_CONTENT；
		// 但在4.4或以上，官方建议用ACTION_OPEN_DOCUMENT。其实区别不大，真正的区别在于返回的Uri
		Intent intent = null;
		if (isKitKat == false)
			intent = new Intent(Intent.ACTION_GET_CONTENT);
		else
			intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.setType("image/*");
		Intent wrapperIntent = Intent.createChooser(intent, null);
		activity.startActivityForResult(wrapperIntent,
				ExtraGlobal.REQUEST_CODE_GALLERY);
	}

	public static ResultBean onActivityResult(Activity activity,
			int requestCode, int resultCode, Intent intent) {
		boolean result = false;
		String path = null;
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == ExtraGlobal.REQUEST_CODE_GALLERY) {
				if (intent != null)
					path = RealPathUtil.getPath(activity, intent.getData());
				result = true;
			}
		}
		ResultBean bean = new ResultBean(result, path);
		return bean;
	}

}
