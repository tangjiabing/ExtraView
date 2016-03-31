package com.extra.view;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.extra.bean.DataBean;
import com.extra.bean.OperateBean;
import com.extra.bean.ResultBean;
import com.extra.global.ExtraGlobal;
import com.extra.global.PreviewGlobal;
import com.extra.res.ColorRes;
import com.extra.res.DimenRes;
import com.extra.res.DrawableRes;
import com.extra.res.LayoutRes;
import com.extra.res.StringRes;
import com.extra.res.ViewRes;
import com.extra.util.AudioAppUtil;
import com.extra.util.AudioPlayerUtil;
import com.extra.util.AudioPlayerUtil.OnAudioCompleteListener;
import com.extra.util.BitmapUtil;
import com.extra.util.CacheUtil;
import com.extra.util.CameraAppUtil;
import com.extra.util.DialogUtil;
import com.extra.util.DialogUtil.OnOperateListener;
import com.extra.util.DiskCacheDirUtil;
import com.extra.util.FileAppUtil;
import com.extra.util.FileNameUtil;
import com.extra.util.GalleryAppUtil;
import com.extra.util.MD5Util;
import com.extra.util.ResUtil;
import com.extra.util.ToastUtil;
import com.extra.util.VideoAppUtil;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class ExtraView extends LinearLayout {

	public final static int ACTION_GALLERY = 0x11;
	public final static int ACTION_CAMERA = 0x12;
	public final static int ACTION_VIDEO = 0x13;
	public final static int ACTION_FILE = 0x14;
	public final static int ACTION_AUDIO = 0x15;
	private final static String KEY_FILE_ICON = "drawable/extra_file";
	private final static String KEY_AUDIO_ICON = "drawable/extra_audio";
	private final static String EXTRA_DISK_CACHE = "extra";
	private final static int DEFAULT_RECORD_VIDEO_TIME = 20;
	private final static int DEFAULT_RECORD_AUDIO_TIME = 40;
	private final static int DEFAULT_MAX_EXTRA_NUMBER = 50;
	private Activity mActivity = null;
	private TextView mPictureText = null;
	private TextView mVideoText = null;
	private TextView mAudioText = null;
	private TextView mFileText = null;
	private Gallery mGallery = null;
	private GalleryBaseAdapter mAdapter = null;
	private ArrayList<DataBean> mDataList = null;
	private DialogUtil mDialogUtil = null;
	private int mAction = 0;
	private String mExtraCacheDir = null;
	private int mViewWidth = 0;
	private int mViewHeight = 0;
	private int mMaxVideoRecordTime = 0;
	private int mMaxAudioRecordTime = 0;
	private int mMaxExtraNumber = 0;
	private int mMaxPictureNumber = 0;
	private int mMaxVideoNumber = 0;
	private int mMaxAudioNumber = 0;
	private int mMaxFileNumber = 0;
	private int mCurrentExtraCount = 0;
	private int mCurrentPictureCount = 0;
	private int mCurrentVideoCount = 0;
	private int mCurrentAudioCount = 0;
	private int mCurrentFileCount = 0;
	private View mCurrentClickGalleryItemView = null;
	private ArrayList<String> mUploadPathList = null;
	private OnExtraNumberChangeListener mExtraNumberChangeListener = null;
	private ResUtil mResUtil = null;

	public ExtraView(Context context) {
		this(context, null);
	}

	public ExtraView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ExtraView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mActivity = (Activity) context;
		setOrientation(LinearLayout.VERTICAL);
		mResUtil = new ResUtil(context);
		View.inflate(context, mResUtil.getIdFromLayout(LayoutRes.extraview),
				this);
		findView();
		init();
		registerListener();
	}

	// **************************************************************
	// findView，init，registerListener

	private void findView() {
		mPictureText = (TextView) findViewById(mResUtil
				.getIdFromView(ViewRes.pictureText));
		mVideoText = (TextView) findViewById(mResUtil
				.getIdFromView(ViewRes.videoText));
		mAudioText = (TextView) findViewById(mResUtil
				.getIdFromView(ViewRes.audioText));
		mFileText = (TextView) findViewById(mResUtil
				.getIdFromView(ViewRes.fileText));
		mGallery = (Gallery) findViewById(mResUtil
				.getIdFromView(ViewRes.gallery));
	}

	private void init() {
		mDataList = new ArrayList<DataBean>();
		mAdapter = new GalleryBaseAdapter(mActivity, mDataList);
		mGallery.setAdapter(mAdapter);
		mDialogUtil = new DialogUtil(mActivity);
		mExtraCacheDir = DiskCacheDirUtil.getDiskCacheDir(mActivity,
				EXTRA_DISK_CACHE).getAbsolutePath();
		mViewWidth = (int) mActivity
				.getResources()
				.getDimension(
						mResUtil.getIdFromDimen(DimenRes.extra_imageview_wh_gallery_item));
		mViewHeight = mViewWidth;
		mMaxVideoRecordTime = DEFAULT_RECORD_VIDEO_TIME;
		mMaxAudioRecordTime = DEFAULT_RECORD_AUDIO_TIME;
		mUploadPathList = new ArrayList<String>();
		mMaxExtraNumber = DEFAULT_MAX_EXTRA_NUMBER;
		mMaxPictureNumber = DEFAULT_MAX_EXTRA_NUMBER;
		mMaxVideoNumber = DEFAULT_MAX_EXTRA_NUMBER;
		mMaxAudioNumber = DEFAULT_MAX_EXTRA_NUMBER;
		mMaxFileNumber = DEFAULT_MAX_EXTRA_NUMBER;
	}

	private void registerListener() {
		mPictureText.setOnClickListener(new ViewClickListener());
		mVideoText.setOnClickListener(new ViewClickListener());
		mAudioText.setOnClickListener(new ViewClickListener());
		mFileText.setOnClickListener(new ViewClickListener());
		mGallery.setOnItemClickListener(new GalleryItemClickListener());
	}

	// **************************************************************
	// 公有方法

	/**
	 * 设置最大视频录制时间，单位为秒
	 * 
	 * @param maxTime
	 */
	public void setMaxVideoRecordTime(int maxTime) {
		mMaxVideoRecordTime = maxTime;
	}

	/**
	 * 设置最大音频录制时间，单位为秒
	 * 
	 * @param maxTime
	 */
	public void setMaxAudioRecordTime(int maxTime) {
		mMaxAudioRecordTime = maxTime;
	}

	/**
	 * 设置最大附件数，此方法调用后，setMaxPictureNumber、setMaxVideoNumber、
	 * setMaxVideoNumber、setMaxAudioNumber需要重新设置才行
	 * 
	 * @param maxExtraNumber
	 */
	public void setMaxExtraNumber(int maxExtraNumber) {
		mMaxExtraNumber = maxExtraNumber;
		mMaxPictureNumber = maxExtraNumber;
		mMaxVideoNumber = maxExtraNumber;
		mMaxAudioNumber = maxExtraNumber;
		mMaxFileNumber = maxExtraNumber;
	}

	/**
	 * 设置附件中最大图片数
	 * 
	 * @param maxPictureNumber
	 */
	public void setMaxPictureNumber(int maxPictureNumber) {
		mMaxPictureNumber = maxPictureNumber;
		if (mMaxPictureNumber > mMaxExtraNumber)
			mMaxPictureNumber = mMaxExtraNumber;
	}

	/**
	 * 设置附件中最大视频数
	 * 
	 * @param maxVideoNumber
	 */
	public void setMaxVideoNumber(int maxVideoNumber) {
		mMaxVideoNumber = maxVideoNumber;
		if (mMaxVideoNumber > mMaxExtraNumber)
			mMaxVideoNumber = mMaxExtraNumber;
	}

	/**
	 * 设置附件中最大音频数
	 * 
	 * @param maxAudioNumber
	 */
	public void setMaxAudioNumber(int maxAudioNumber) {
		mMaxAudioNumber = maxAudioNumber;
		if (mMaxAudioNumber > mMaxExtraNumber)
			mMaxAudioNumber = mMaxExtraNumber;
	}

	/**
	 * 设置附件中最大文件数（不包括图片）
	 * 
	 * @param maxFileNumber
	 */
	public void setMaxFileNumber(int maxFileNumber) {
		mMaxFileNumber = maxFileNumber;
		if (mMaxFileNumber > mMaxExtraNumber)
			mMaxFileNumber = mMaxExtraNumber;
	}

	public ArrayList<String> getUploadPathList() {
		return mUploadPathList;
	}

	public void setOnExtraNumberChangeListener(
			OnExtraNumberChangeListener listener) {
		mExtraNumberChangeListener = listener;
	}

	public void clearDiskCache() {
		DiskCacheDirUtil.clearDiskCache(mActivity, EXTRA_DISK_CACHE);
	}

	public boolean onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		ResultBean bean = null;
		int type = 0;
		switch (mAction) {
		case ACTION_GALLERY:
			bean = GalleryAppUtil.onActivityResult(mActivity, requestCode,
					resultCode, intent);
			type = DataBean.TYPE_PICTURE;
			break;
		case ACTION_CAMERA:
			bean = CameraAppUtil.onActivityResult(requestCode, resultCode,
					intent);
			type = DataBean.TYPE_PICTURE;
			break;
		case ACTION_VIDEO:
			bean = VideoAppUtil.onActivityResult(requestCode, resultCode,
					intent);
			type = DataBean.TYPE_VIDEO;
			break;
		case ACTION_FILE:
			bean = FileAppUtil.onActivityResult(mActivity, requestCode,
					resultCode, intent);
			type = DataBean.TYPE_FILE;
			break;
		case ACTION_AUDIO:
			bean = AudioAppUtil.onActivityResult(requestCode, resultCode,
					intent);
			type = DataBean.TYPE_AUDIO;
			break;
		}
		if (bean == null)
			return false;
		addGalleryItem(bean.getPath(), type);
		return bean.isResult();
	}

	public void onDestroy() {
		CacheUtil.clear();
		AudioPlayerUtil.releasePlayer();
	}

	// **************************************************************
	// 私有方法

	private void pictureDialog() {
		String title = mActivity.getResources().getString(
				mResUtil.getIdFromString(StringRes.extra_dialog_picture_title));
		String gallery = mActivity.getResources().getString(
				mResUtil.getIdFromString(StringRes.extra_dialog_gallery));
		String camera = mActivity.getResources().getString(
				mResUtil.getIdFromString(StringRes.extra_dialog_camera));
		ArrayList<OperateBean> dataList = new ArrayList<OperateBean>();
		dataList.add(new OperateBean(OperateBean.OPERATE_OPEN_GALLERY, gallery));
		dataList.add(new OperateBean(OperateBean.OPERATE_OPEN_CAMERA, camera));
		mDialogUtil.dialog(title, dataList, new OnOperateListener() {
			@Override
			public void operate(int type) {
				switch (type) {
				case OperateBean.OPERATE_OPEN_GALLERY:
					mAction = ACTION_GALLERY;
					GalleryAppUtil.openGalleryApp(mActivity);
					break;
				case OperateBean.OPERATE_OPEN_CAMERA:
					mAction = ACTION_CAMERA;
					CameraAppUtil.openCameraApp(mActivity, mExtraCacheDir);
					break;
				}
			}

			@Override
			public void dismiss() {

			}
		});
	}

	private void deleteOrPreviewDialog(final DataBean bean,
			final LinearLayout maskLinearLayout) {
		String title = bean.getName();
		String delete = mActivity.getResources().getString(
				mResUtil.getIdFromString(StringRes.extra_dialog_delete));
		String preview = mActivity.getResources().getString(
				mResUtil.getIdFromString(StringRes.extra_dialog_preview));
		ArrayList<OperateBean> dataList = new ArrayList<OperateBean>();
		dataList.add(new OperateBean(OperateBean.OPERATE_DELETE, delete));
		dataList.add(new OperateBean(OperateBean.OPERATE_PREVIEW, preview));
		mDialogUtil.dialog(title, dataList, new OnOperateListener() {
			@Override
			public void operate(int type) {
				switch (type) {
				case OperateBean.OPERATE_DELETE:
					deleteGalleryItem(bean);
					break;
				case OperateBean.OPERATE_PREVIEW:
					// previewOldMethod(bean, maskLinearLayout);
					previewNewMethod(bean);
					break;
				}
			}

			private void previewNewMethod(final DataBean bean) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setAction(Intent.ACTION_VIEW);
				if (bean.getType() == DataBean.TYPE_FILE)
					intent.setData(Uri.fromFile(new File(bean.getPath())));
				else {
					String dataType = null;
					if (bean.getType() == DataBean.TYPE_PICTURE)
						dataType = "image/*";
					else if (bean.getType() == DataBean.TYPE_VIDEO)
						dataType = "video/*";
					else if (bean.getType() == DataBean.TYPE_AUDIO)
						dataType = "audio/*";
					intent.setDataAndType(
							Uri.fromFile(new File(bean.getPath())), dataType);
				}
				mActivity.startActivity(intent);
			}

			@SuppressWarnings("unused")
			private void previewOldMethod(DataBean bean,
					LinearLayout maskLinearLayout) {
				if (bean.getType() == DataBean.TYPE_AUDIO)
					startPlayAudio(bean, maskLinearLayout);
				else {
					Intent intent = null;
					if (bean.getType() == DataBean.TYPE_FILE) {
						intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.fromFile(new File(bean.getPath())));
					} else {
						intent = new Intent();
						if (bean.getType() == DataBean.TYPE_PICTURE)
							intent.setClassName(mActivity,
									ExtraGlobal.CLASS_NAME_PICTURE_PREVIEW);
						else if (bean.getType() == DataBean.TYPE_VIDEO)
							intent.setClassName(mActivity,
									ExtraGlobal.CLASS_NAME_VIDEO_PREVIEW);
						intent.putExtra(PreviewGlobal.KEY_LOCAL_PATH,
								bean.getPath());
					}
					mActivity.startActivity(intent);
				}
			}

			@Override
			public void dismiss() {
				if (mCurrentClickGalleryItemView != null) {
					mCurrentClickGalleryItemView.setBackgroundResource(mResUtil
							.getIdFromColor(ColorRes.extra_linearlayout_root_bg_gallery_item));
					mCurrentClickGalleryItemView = null;
				}
			}
		});
	}

	private void addGalleryItem(String path, int type) {
		if (path != null) {
			if (type == DataBean.TYPE_FILE) {
				String[] imgSuffix = new String[] { "jpg", "jpeg", "png",
						"gif", "bmp", "webp" };
				String suffix = FileNameUtil.getSuffix(path);
				boolean isImgFile = false;
				for (int i = 0; i < imgSuffix.length; i++) {
					if (suffix.equalsIgnoreCase(imgSuffix[i])) {
						isImgFile = true;
						break;
					}
				}
				if (isImgFile == true) {
					if (mCurrentPictureCount == mMaxPictureNumber) {
						ToastUtil
								.toast(mActivity,
										mResUtil.getIdFromString(StringRes.extra_picture_is_enough));
						return;
					} else {
						mCurrentPictureCount++;
						type = DataBean.TYPE_PICTURE;
					}
				} else
					mCurrentFileCount++;
			} else if (type == DataBean.TYPE_PICTURE)
				mCurrentPictureCount++;
			else if (type == DataBean.TYPE_AUDIO)
				mCurrentAudioCount++;
			else if (type == DataBean.TYPE_VIDEO)
				mCurrentVideoCount++;
			mCurrentExtraCount++;
			mDataList.add(new DataBean(type, path, FileNameUtil.getName(path)));
			mAdapter.notifyDataSetChanged();
			mUploadPathList.add(path);
			if (mExtraNumberChangeListener != null)
				mExtraNumberChangeListener.change(mCurrentExtraCount);
			if (mCurrentExtraCount > 2)
				mGallery.setSelection(mCurrentExtraCount - 2, true);
			else
				mGallery.setSelection(mCurrentExtraCount - 1, true);
		}
	}

	private void deleteGalleryItem(DataBean bean) {
		if (bean.getType() == DataBean.TYPE_PICTURE)
			mCurrentPictureCount--;
		else if (bean.getType() == DataBean.TYPE_AUDIO)
			mCurrentAudioCount--;
		else if (bean.getType() == DataBean.TYPE_VIDEO)
			mCurrentVideoCount--;
		else if (bean.getType() == DataBean.TYPE_FILE)
			mCurrentFileCount--;
		mCurrentExtraCount--;
		mDataList.remove(bean);
		mAdapter.notifyDataSetChanged();
		mUploadPathList.remove(bean.getPath());
		if (mExtraNumberChangeListener != null)
			mExtraNumberChangeListener.change(mCurrentExtraCount);
	}

	private void startPlayAudio(final DataBean bean,
			final LinearLayout maskLinearLayout) {
		boolean flag = AudioPlayerUtil.startPlay(mActivity, bean.getPath());
		if (flag) {
			maskLinearLayout.setVisibility(View.VISIBLE);
			AudioPlayerUtil
					.setOnAudioCompleteListener(new OnAudioCompleteListener() {
						@Override
						public void complete() {
							maskLinearLayout.setVisibility(View.GONE);
						}
					});
		}
	}

	private void stopPlayAudio(DataBean bean, LinearLayout maskLinearLayout) {
		if (AudioPlayerUtil.isPlaying()) {
			AudioPlayerUtil.stopPlay();
			maskLinearLayout.setVisibility(View.GONE);
		}
	}

	// **************************************************************
	// 自定义的类

	public interface OnExtraNumberChangeListener {
		public void change(int currentExtraNumber);
	}

	private class ViewClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (AudioPlayerUtil.isPlaying()) {
				ToastUtil.toast(mActivity, mResUtil
						.getIdFromString(StringRes.extra_please_stop_player));
				return;
			}
			if (mCurrentExtraCount < mMaxExtraNumber) {
				int id = v.getId();
				if (id == mResUtil.getIdFromView(ViewRes.pictureText)) {
					if (mCurrentPictureCount == mMaxPictureNumber)
						ToastUtil
								.toast(mActivity,
										mResUtil.getIdFromString(StringRes.extra_picture_is_enough));
					else
						pictureDialog();
				} else if (id == mResUtil.getIdFromView(ViewRes.videoText)) {
					if (mCurrentVideoCount == mMaxVideoNumber)
						ToastUtil
								.toast(mActivity,
										mResUtil.getIdFromString(StringRes.extra_video_is_enough));
					else {
						mAction = ACTION_VIDEO;
						VideoAppUtil.openVideoApp(mActivity, mExtraCacheDir,
								mMaxVideoRecordTime);
					}
				} else if (id == mResUtil.getIdFromView(ViewRes.audioText)) {
					if (mCurrentAudioCount == mMaxAudioNumber)
						ToastUtil
								.toast(mActivity,
										mResUtil.getIdFromString(StringRes.extra_audio_is_enough));
					else {
						mAction = ACTION_AUDIO;
						AudioAppUtil.openAudioApp(mActivity, mExtraCacheDir,
								mMaxAudioRecordTime);
					}
				} else if (id == mResUtil.getIdFromView(ViewRes.fileText)) {
					if (mCurrentFileCount == mMaxFileNumber)
						ToastUtil
								.toast(mActivity,
										mResUtil.getIdFromString(StringRes.extra_file_is_enough));
					else {
						mAction = ACTION_FILE;
						FileAppUtil.openFileApp(mActivity);
					}
				}
			} else
				ToastUtil.toast(mActivity, mResUtil
						.getIdFromString(StringRes.extra_number_is_enough));
		}
	}

	private class GalleryBaseAdapter extends BaseAdapter {

		private LayoutInflater inflater = null;
		private ArrayList<DataBean> dataList = null;

		public GalleryBaseAdapter(Context context, ArrayList<DataBean> dataList) {
			inflater = LayoutInflater.from(context);
			this.dataList = dataList;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		class ViewHolder {
			ImageView imageView = null;
			TextView nameText = null;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(
						mResUtil.getIdFromLayout(LayoutRes.extra_gallery_item),
						null);
				holder.imageView = (ImageView) convertView
						.findViewById(mResUtil.getIdFromView(ViewRes.imageView));
				holder.nameText = (TextView) convertView.findViewById(mResUtil
						.getIdFromView(ViewRes.nameText));
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			DataBean bean = dataList.get(position);
			int type = bean.getType();
			String path = bean.getPath();

			Bitmap bitmap = null;
			String key = null;
			if (type == DataBean.TYPE_FILE)
				key = MD5Util.getMD5(KEY_FILE_ICON);
			else if (type == DataBean.TYPE_AUDIO)
				key = MD5Util.getMD5(KEY_AUDIO_ICON);
			else
				key = MD5Util.getMD5(path);
			bitmap = CacheUtil.get(key);

			if (bitmap == null) {
				switch (type) {
				case DataBean.TYPE_PICTURE:
					bitmap = BitmapUtil.getFitBitmap(path, mViewWidth,
							mViewHeight);
					break;
				case DataBean.TYPE_VIDEO:
					bitmap = BitmapUtil.getVideoThumbnail(path, mViewWidth,
							mViewHeight);
					break;
				case DataBean.TYPE_FILE:
					bitmap = BitmapUtil.getFitBitmap(mActivity,
							mResUtil.getIdFromDrawable(DrawableRes.extra_file),
							mViewWidth, mViewHeight);
					break;
				case DataBean.TYPE_AUDIO:
					bitmap = BitmapUtil
							.getFitBitmap(
									mActivity,
									mResUtil.getIdFromDrawable(DrawableRes.extra_audio),
									mViewWidth, mViewHeight);
					break;
				}
				CacheUtil.put(key, bitmap);
			}

			holder.imageView.setImageBitmap(bitmap);
			holder.nameText.setText(bean.getName());

			return convertView;
		}
	}

	private class GalleryItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			DataBean bean = mDataList.get(position);
			LinearLayout maskLinearLayout = (LinearLayout) view
					.findViewById(mResUtil
							.getIdFromView(ViewRes.maskLinearLayout));
			if (maskLinearLayout.getVisibility() == View.GONE) {
				deleteOrPreviewDialog(bean, maskLinearLayout);
				mCurrentClickGalleryItemView = view;
				mCurrentClickGalleryItemView
						.setBackgroundResource(mResUtil
								.getIdFromColor(ColorRes.extra_linearlayout_root_selected_bg_gallery_item));
			} else
				stopPlayAudio(bean, maskLinearLayout);
		}
	}

}
