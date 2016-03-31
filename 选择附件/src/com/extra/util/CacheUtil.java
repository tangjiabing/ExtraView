package com.extra.util;

import android.graphics.Bitmap;

import com.extra.cache.LruMemeryCache;

/**
 * 
 * @author tangjiabing
 * 
 * @see ��Դʱ�䣺2016��03��31��
 * 
 *      �ǵø��Ҹ�starŶ~
 * 
 */
public class CacheUtil {

	private static LruMemeryCache mCache = null;

	static {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mCache = new LruMemeryCache(cacheSize);
	}

	public static void put(String key, Bitmap bitmap) {
		mCache.put(key, bitmap);
	}

	public static Bitmap get(String key) {
		return mCache.get(key);
	}

	public static void remove(String key) {
		mCache.remove(key);
	}

	public static void clear() {
		mCache.evictAll();
	}

}
