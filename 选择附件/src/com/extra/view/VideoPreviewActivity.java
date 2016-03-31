package com.extra.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.extra.global.PreviewGlobal;
import com.extra.res.LayoutRes;
import com.extra.res.ViewRes;
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
public class VideoPreviewActivity extends Activity {

	private VideoView mVideoView = null;
	private ResUtil mResUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // ���ر�����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // ���õ�ǰ����Ϊȫ����ʾ
		mResUtil = new ResUtil(this);
		setContentView(mResUtil
				.getIdFromLayout(LayoutRes.extra_video_preview_activity));
		findView();
		init();
	}

	private void findView() {
		mVideoView = (VideoView) findViewById(mResUtil
				.getIdFromView(ViewRes.videoView));
	}

	private void init() {
		Intent intent = getIntent();
		String path = intent.getStringExtra(PreviewGlobal.KEY_LOCAL_PATH);
		MediaController controller = new MediaController(this);
		mVideoView.setVideoPath(path);
		mVideoView.setMediaController(controller);
		mVideoView.requestFocus();
		mVideoView.start();
	}

}
