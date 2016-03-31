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
 * @see ��Դʱ�䣺2016��03��31��
 * 
 *      �ǵø��Ҹ�starŶ~
 * 
 */
public class GalleryAppUtil {

	public static void openGalleryApp(Activity activity) {
		boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		// ��4.3�����£�����ֱ����ACTION_GET_CONTENT��
		// ����4.4�����ϣ��ٷ�������ACTION_OPEN_DOCUMENT����ʵ���𲻴��������������ڷ��ص�Uri
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
