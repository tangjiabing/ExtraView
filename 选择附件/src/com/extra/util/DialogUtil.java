package com.extra.util;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.extra.bean.OperateBean;
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
public class DialogUtil {

	private Activity mActivity = null;
	private LayoutInflater mInflater = null;
	private OnOperateListener mListener = null;
	private AlertDialog mCurrentDialog = null;
	private ResUtil mResUtil = null;

	public DialogUtil(Activity activity) {
		mActivity = activity;
		mInflater = LayoutInflater.from(activity);
		mResUtil = new ResUtil(activity);
	}

	public void dialog(String title, ArrayList<OperateBean> dataList,
			OnOperateListener listener) {
		View view = mInflater.inflate(
				mResUtil.getIdFromLayout(LayoutRes.extra_dialog), null);
		TextView titleText = (TextView) view.findViewById(mResUtil
				.getIdFromView(ViewRes.titleText));
		titleText.setText(title);
		ListView listView = (ListView) view.findViewById(mResUtil
				.getIdFromView(ViewRes.listView));
		ListBaseAdapter adapter = new ListBaseAdapter(mInflater, dataList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new ListItemClickListener());
		mListener = listener;

		mCurrentDialog = new AlertDialog.Builder(mActivity).create();
		mCurrentDialog.show();
		mCurrentDialog.getWindow().setContentView(view);
		mCurrentDialog.setOnDismissListener(new DialogDismissListener());
	}

	// ********************************************************
	// 自定义的类

	private class ListBaseAdapter extends BaseAdapter {

		private LayoutInflater inflater = null;
		private ArrayList<OperateBean> dataList = null;

		public ListBaseAdapter(LayoutInflater inflater,
				ArrayList<OperateBean> dataList) {
			this.inflater = inflater;
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
			TextView operateText = null;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(mResUtil
						.getIdFromLayout(LayoutRes.extra_dialog_listview_item),
						null);
				holder.operateText = (TextView) convertView
						.findViewById(mResUtil
								.getIdFromView(ViewRes.operateText));
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			OperateBean bean = dataList.get(position);
			holder.operateText.setText(bean.getName());

			return convertView;
		}
	}

	private class ListItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (mListener != null) {
				OperateBean bean = (OperateBean) parent
						.getItemAtPosition(position);
				mListener.operate(bean.getType());
			}
			mCurrentDialog.dismiss();
		}
	}

	private class DialogDismissListener implements OnDismissListener {
		@Override
		public void onDismiss(DialogInterface dialog) {
			if (mListener != null)
				mListener.dismiss();
		}
	}

	public interface OnOperateListener {

		public void operate(int type);

		public void dismiss();
	}

}
