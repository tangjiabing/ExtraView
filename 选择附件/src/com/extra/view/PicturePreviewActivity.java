package com.extra.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.extra.global.PreviewGlobal;
import com.extra.res.LayoutRes;
import com.extra.res.ViewRes;
import com.extra.util.BitmapUtil;
import com.extra.util.ResUtil;

/**
 * 
 * @author tangjiabing
 * 
 * @see ��Դʱ�䣺2016��03��31��
 * 
 *      �ǵø��Ҹ�starŶ~
 * 
 */
public class PicturePreviewActivity extends Activity {

	private ImageView mImageView = null;
	private Bitmap mBitmap = null;
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	private ResUtil mResUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // ���ر�����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // ���õ�ǰ����Ϊȫ����ʾ
		mResUtil = new ResUtil(this);
		setContentView(mResUtil
				.getIdFromLayout(LayoutRes.extra_picture_preview_activity));
		findView();
		init();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBitmap.recycle();
		mBitmap = null;
	}

	// ***********************************************************
	// findView��init

	private void findView() {
		mImageView = (ImageView) findViewById(mResUtil
				.getIdFromView(ViewRes.imageView));
	}

	private void init() {
		WindowManager windowManager = getWindowManager();
		mScreenWidth = windowManager.getDefaultDisplay().getWidth();
		mScreenHeight = windowManager.getDefaultDisplay().getHeight();
		Intent intent = getIntent();
		String path = intent.getStringExtra(PreviewGlobal.KEY_LOCAL_PATH);
		mBitmap = BitmapUtil.getFitBitmap(path, mScreenWidth, mScreenHeight);
		mImageView.setImageBitmap(mBitmap);
	}
}
