package com.extra.view;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.extra.global.AudioGlobal;
import com.extra.res.LayoutRes;
import com.extra.res.StringRes;
import com.extra.res.ViewRes;
import com.extra.util.AudioPlayerUtil;
import com.extra.util.AudioPlayerUtil.OnAudioCompleteListener;
import com.extra.util.AudioRecorderUtil;
import com.extra.util.ResUtil;
import com.extra.util.ToastUtil;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class AudioAppActivity extends Activity {

	/** 最大录制时间，单位为秒 */
	private static final int MAX_RECORD_TIME = 3600;
	private ImageView mBackImageView = null;
	private ImageView mAudioImageView = null;
	private TextView mTimeText = null;
	private LinearLayout mCompleteLinearLayout = null;
	private Button mCancelButton = null;
	private Button mPlayButton = null;
	private Button mConfirmButton = null;
	private Handler mHandler = null;
	private String mAudioLocalPath = null;
	private Timer mTimer = null;
	private RecordTimerTask mTimerTask = null;
	private int mNeedRecordTime = 0;
	private int mRecordTime = -1;
	private ResUtil mResUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mResUtil = new ResUtil(this);
		setContentView(mResUtil
				.getIdFromLayout(LayoutRes.extra_audio_app_activity));
		findView();
		init();
		registerListener();
	}

	@Override
	public void onBackPressed() {
		cancel();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AudioRecorderUtil.releaseRecorder();
		AudioPlayerUtil.releasePlayer();
	}

	// ***************************************************************************************
	// findView，init，registerListener

	private void findView() {
		mBackImageView = (ImageView) findViewById(mResUtil
				.getIdFromView(ViewRes.backImageView));
		mAudioImageView = (ImageView) findViewById(mResUtil
				.getIdFromView(ViewRes.audioImageView));
		mTimeText = (TextView) findViewById(mResUtil
				.getIdFromView(ViewRes.timeText));
		mCompleteLinearLayout = (LinearLayout) findViewById(mResUtil
				.getIdFromView(ViewRes.completeLinearLayout));
		mCancelButton = (Button) findViewById(mResUtil
				.getIdFromView(ViewRes.cancelButton));
		mPlayButton = (Button) findViewById(mResUtil
				.getIdFromView(ViewRes.playButton));
		mConfirmButton = (Button) findViewById(mResUtil
				.getIdFromView(ViewRes.confirmButton));
	}

	private void init() {
		Intent intent = getIntent();
		mAudioLocalPath = intent.getStringExtra(AudioGlobal.KEY_LOCAL_PATH);
		mNeedRecordTime = intent.getIntExtra(AudioGlobal.KEY_RECORD_TIME,
				MAX_RECORD_TIME);
		if (mNeedRecordTime > MAX_RECORD_TIME)
			mNeedRecordTime = MAX_RECORD_TIME;
		mHandler = new Handler();
	}

	private void registerListener() {
		mBackImageView.setOnClickListener(new ViewClickListener());
		mAudioImageView.setOnClickListener(new ViewClickListener());
		mCancelButton.setOnClickListener(new ViewClickListener());
		mPlayButton.setOnClickListener(new ViewClickListener());
		mConfirmButton.setOnClickListener(new ViewClickListener());
	}

	// ***************************************************************************************
	// 私有方法

	private void cancel() {
		stopPlayAudio();
		File file = new File(mAudioLocalPath);
		if (file.exists())
			file.delete();
		setResult(RESULT_CANCELED);
		finish();
	}

	private String getCurrentRecordTime() {
		int minute = 0;
		int second = 0;
		minute = mRecordTime / 60;
		second = mRecordTime - minute * 60;
		StringBuilder builder = new StringBuilder();
		if (minute < 10)
			builder.append("0" + minute);
		else
			builder.append(minute);
		builder.append(":");
		if (second < 10)
			builder.append("0" + second);
		else
			builder.append(second);
		return builder.toString();
	}

	private void startTiming() {
		if (mTimer != null && mTimerTask != null) {
			mTimer.cancel();
			mTimerTask.cancel();
		}
		mRecordTime = -1;
		mTimerTask = new RecordTimerTask();
		mTimer = new Timer();
		mTimer.schedule(mTimerTask, 100, 1000);
	}

	private void stopTiming() {
		if (mTimer != null && mTimerTask != null) {
			mTimer.cancel();
			mTimerTask.cancel();
			mTimer = null;
			mTimerTask = null;
		}
	}

	private void stopPlayAudio() {
		AudioPlayerUtil.stopPlay();
		mPlayButton.setText(mResUtil
				.getIdFromString(StringRes.extra_button_play));
	}

	// ***************************************************************************************
	// 自定义的类

	private class ViewClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == mResUtil.getIdFromView(ViewRes.backImageView))
				cancel();
			else if (id == mResUtil.getIdFromView(ViewRes.audioImageView)) {
				if (mAudioImageView.isSelected()) { // 正在录音-->停止录音
					stopTiming();
					AudioRecorderUtil.stopRecord();
					mAudioImageView.setSelected(false);
					File file = new File(mAudioLocalPath);
					if (file.exists())
						mCompleteLinearLayout.setVisibility(View.VISIBLE);
					else
						ToastUtil
								.toast(AudioAppActivity.this,
										mResUtil.getIdFromString(StringRes.extra_audio_file_is_not_exist));
				} else { // 开始录音
					stopPlayAudio();
					boolean flag = AudioRecorderUtil.startRecord(
							AudioAppActivity.this, mAudioLocalPath);
					if (flag) {
						startTiming();
						mAudioImageView.setSelected(true);
						mCompleteLinearLayout.setVisibility(View.INVISIBLE);
					}
				}
			} else if (id == mResUtil.getIdFromView(ViewRes.cancelButton))
				cancel();
			else if (id == mResUtil.getIdFromView(ViewRes.playButton)) {
				if (AudioPlayerUtil.isPlaying())
					stopPlayAudio();
				else {
					boolean flag = AudioPlayerUtil.startPlay(
							AudioAppActivity.this, mAudioLocalPath);
					if (flag) {
						AudioPlayerUtil
								.setOnAudioCompleteListener(new OnAudioCompleteListener() {
									@Override
									public void complete() {
										mPlayButton.setText(mResUtil
												.getIdFromString(StringRes.extra_button_play));
									}
								});
						mPlayButton.setText(mResUtil
								.getIdFromString(StringRes.extra_button_stop));
					}
				}
			} else if (id == mResUtil.getIdFromView(ViewRes.confirmButton)) {
				setResult(RESULT_OK);
				finish();
			}
		}
	}

	private class RecordTimerTask extends TimerTask {
		@Override
		public void run() {
			mRecordTime++;
			final String time = getCurrentRecordTime();
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mTimeText.setText(time);
					if (mRecordTime >= mNeedRecordTime) {
						stopTiming();
						AudioRecorderUtil.stopRecord();
						mAudioImageView.setSelected(false);
						mCompleteLinearLayout.setVisibility(View.VISIBLE);
					}
				}
			});
		}
	}

}
