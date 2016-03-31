package com.extra.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.extra.res.LayoutRes;
import com.extra.res.ViewRes;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class ProgressDialogUtil {

	private static AlertDialog mCurrentDialog = null;
	private static TextView mSuccessNumText = null;
	private static TextView mFailNumText = null;
	private static ProgressBar mUploadProgressBar = null;
	private static TextView mProgressText = null;
	private static Button mCancelButton = null;
	private static OnCancelUploadListener mCancelListener = null;

	public static void showDialog(Context context,
			OnCancelUploadListener listener) {
		ResUtil resUtil = new ResUtil(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(
				resUtil.getIdFromLayout(LayoutRes.extra_progress_dialog), null);
		mSuccessNumText = (TextView) view.findViewById(resUtil
				.getIdFromView(ViewRes.successNumText));
		mFailNumText = (TextView) view.findViewById(resUtil
				.getIdFromView(ViewRes.failNumText));
		mUploadProgressBar = (ProgressBar) view.findViewById(resUtil
				.getIdFromView(ViewRes.uploadProgressBar));
		mProgressText = (TextView) view.findViewById(resUtil
				.getIdFromView(ViewRes.progressText));
		mCancelButton = (Button) view.findViewById(resUtil
				.getIdFromView(ViewRes.cancelButton));
		mCurrentDialog = new AlertDialog.Builder(context).create();
		mCurrentDialog.setCancelable(false);
		mCurrentDialog.show();
		mCurrentDialog.getWindow().setContentView(view);
		mCancelListener = listener;
		mCancelButton.setOnClickListener(new ButtonClickListener());
	}

	public static void dismiss() {
		mCurrentDialog.dismiss();
		setNull();
	}

	public static void setSuccessNum(int num) {
		mSuccessNumText.setText(num + "");
	}

	public static void setFailNum(int num) {
		mFailNumText.setText(num + "");
	}

	public static void setProgressText(int currentNum, int totalNum) {
		mProgressText.setText(currentNum + "/" + totalNum);
	}

	public static void setProgress(int progress) {
		mUploadProgressBar.setProgress(progress);
	}

	public static void setMax(int max) {
		mUploadProgressBar.setMax(max);
	}

	private static void setNull() {
		mSuccessNumText = null;
		mFailNumText = null;
		mUploadProgressBar = null;
		mProgressText = null;
		mCancelButton = null;
		mCurrentDialog = null;
		mCancelListener = null;
	}

	private static class ButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (mCancelListener != null) {
				mCancelListener.cancel();
				dismiss();
			}
		}
	}

	public interface OnCancelUploadListener {
		public void cancel();
	}

}
