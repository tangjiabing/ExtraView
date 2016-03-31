package com.extra.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class BitmapUtil {

	public static Bitmap getFitBitmap(String path, int viewWidth, int viewHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = getSampleSize(options.outWidth,
				options.outHeight, viewWidth, viewHeight);
		return BitmapFactory.decodeFile(path, options);
	}

	public static Bitmap getFitBitmap(Context context, int resId,
			int viewWidth, int viewHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resId, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = getSampleSize(options.outWidth,
				options.outHeight, viewWidth, viewHeight);
		return BitmapFactory.decodeResource(context.getResources(), resId,
				options);
	}

	public static Bitmap getVideoThumbnail(String path, int viewWidth,
			int viewHeight) {
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(path);
			Bitmap frame = retriever.getFrameAtTime(-1);
			int sampleSize = getSampleSize(frame.getWidth(), frame.getHeight(),
					viewWidth, viewHeight);
			int width = frame.getWidth() / sampleSize;
			int height = frame.getHeight() / sampleSize;
			Bitmap bitmap = Bitmap.createScaledBitmap(frame, width, height,
					true);
			if (bitmap != frame)
				frame.recycle();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			retriever.release();
		}
	}

	// ************************************************************
	// 私有方法

	private static int getSampleSize(int srcWidth, int srcHeight, int desWidth,
			int desHeight) {
		int wSampleSize = srcWidth / desWidth;
		int hSampleSize = srcHeight / desHeight;
		int sampleSize = Math.max(wSampleSize, hSampleSize);
		if (sampleSize < 1)
			sampleSize = 1;
		return sampleSize;
	}
}
