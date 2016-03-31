package com.extra.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 
 * @author tangjiabing
 * 
 * @see ��Դʱ�䣺2016��03��31��
 * 
 *      �ǵø��Ҹ�starŶ~
 * 
 */
public class ToastUtil {

	public static void toast(Context context, int msgId) {
		Toast toast = Toast.makeText(context, msgId, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
