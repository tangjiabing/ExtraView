package com.extra.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class ToastUtil {

	public static void toast(Context context, int msgId) {
		Toast toast = Toast.makeText(context, msgId, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
