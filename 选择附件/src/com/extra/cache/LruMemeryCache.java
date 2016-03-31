package com.extra.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 
 * @author tangjiabing
 * 
 * @see ��Դʱ�䣺2016��03��31��
 * 
 *      �ǵø��Ҹ�starŶ~
 * 
 */
public class LruMemeryCache extends LruCache<String, Bitmap> {

	public LruMemeryCache(int maxSize) {
		super(maxSize);
	}

	@Override
	protected void entryRemoved(boolean evicted, String key, Bitmap oldValue,
			Bitmap newValue) {
//		if (evicted)
//			oldValue.recycle();
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getByteCount();
	}

}
