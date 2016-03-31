package com.extra.util;

import android.graphics.Bitmap;

import com.extra.cache.LruMemeryCache;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
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
