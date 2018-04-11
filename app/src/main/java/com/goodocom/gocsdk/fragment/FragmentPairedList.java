package com.goodocom.gocsdk.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.goodocom.gocsdk.R;
import com.goodocom.gocsdk.activity.MainActivity;
import com.goodocom.gocsdk.domain.BlueToothPairedInfo;
import com.goodocom.gocsdk.view.TransparentDialog;

import java.util.ArrayList;
import java.util.List;

public class FragmentPairedList extends Fragment {

	public static final int MSG_PAIRED_DEVICE = 0;
	public static final int MSG_CONNECT_ADDRESS = 1;
	public static final int MSG_CONNECT_SUCCESS = 2;
	public static final int MSG_CONNECT_FAILE = 3;
	public static final int MSG_CURRENT_STATUS = 4;
	public static final int MSG_HFP_STATUS = 5;

	private MainActivity activity;
	private ListView lv_paired_list;
	private DeviceAdapter deviceAdapter;
	private String address = null;
	private boolean isConnecting = true;
	private TransparentDialog dialog;

	private List<BlueToothPairedInfo> btpi = new ArrayList<BlueToothPairedInfo>();
	private static Handler hand = null;
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_PAIRED_DEVICE:
				BlueToothPairedInfo pairedInfo = (BlueToothPairedInfo) msg.obj;
				if (pairedInfo.index > 0) {
					if(!btpi.contains(pairedInfo)){
						btpi.add(pairedInfo);
					}
				}
				deviceAdapter.notifyDataSetChanged();
				break;
			case MSG_CONNECT_ADDRESS:
				address = (String) msg.obj;
				break;
			case MSG_CONNECT_SUCCESS:
				isConnecting = true;
				if (dialog != null) {
					dialog.dismiss();
				}
				Log.d("app","connect success initData");
				initData();
				break;
			case MSG_CONNECT_FAILE:
				isConnecting = false;
				address = null;
				if (dialog != null) {
					dialog.dismiss();
				}
				Log.d("app","connect failed initData");
				initData();
				break;

			case MSG_CURRENT_STATUS:
				isConnecting = (Boolean) msg.obj;
				break;

			case MSG_HFP_STATUS:
				int status = (Integer) msg.obj;
				if(status<3){
					address = null;
				}
				initData();
				break;
			}
		};
	};

	public static Handler getHandler() {
		return hand;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(activity, R.layout.fragmentbluetoothlist, null);
		lv_paired_list = (ListView) view.findViewById(R.id.lv_paired_list);
		lv_paired_list.setSelector(R.drawable.contact_list_item_selector);
		deviceAdapter = new DeviceAdapter();
		lv_paired_list.setAdapter(deviceAdapter);
		Log.d("app","onCreateView initData");
		initData();
		lv_paired_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BlueToothPairedInfo blueToothPairedInfo = deviceAdapter
						.getItem(position);
				if(!TextUtils.isEmpty(address)){
					if (isConnecting && blueToothPairedInfo.address.equals(address)) {
						createDisContentBtDialog(blueToothPairedInfo.name,
								blueToothPairedInfo.address);
					} else if(isConnecting&& !blueToothPairedInfo.address.equals(address)){
						createRemindDialog();
					}else {
						createContentBtDialog(blueToothPairedInfo.name,
								blueToothPairedInfo.address);
					}
				}else{
					createContentBtDialog(blueToothPairedInfo.name,
							blueToothPairedInfo.address);
				}
			}
		});
		hand = handler;
		return view;
	}

	protected void createRemindDialog() {
		AlertDialog dialog = new  AlertDialog.Builder(activity)
				.setTitle("提醒").
				setMessage("请先断开当前连接！").
				create();
			dialog.setCancelable(true);
			dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialog.show();
	}

	protected void showPopupWindow() {
		dialog = new TransparentDialog(activity, R.style.transparentdialog);
		dialog.setCanceledOnTouchOutside(false);// 点击不消失
		dialog.show();
	}

	private void createContentBtDialog(String Name, final String address) {
		MainActivity.currentDeviceName = Name;
		AlertDialog.Builder builder = new Builder(activity);
		builder.setMessage("确定要连接该设备吗?" + Name + ":" + address);
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						showPopupWindow();
						try {
							MainActivity.getService().connectDevice(address);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	private void createDisContentBtDialog(String Name, final String address) {
		AlertDialog.Builder builder = new Builder(activity);
		builder.setMessage("确定要断开该设备吗?" + Name + ":" + address);
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						try {
							MainActivity.getService().disconnect();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	private void initData() {
		//清理数据
		btpi.clear();
		//数据改变了，通知adapter
		deviceAdapter.notifyDataSetChanged();
		//请求数据，请求配对列表到了，添加数据，刷新数据
		//获取当前连接地址
		if (MainActivity.getService() != null) {
			try {
				MainActivity.getService().getPairList();
				MainActivity.getService().getCurrentDeviceAddr();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (MainActivity.getService() != null) {
						try {
							MainActivity.getService().getPairList();
							MainActivity.getService().getCurrentDeviceAddr();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				}
			}, 500);
		}
	}

	private class DeviceAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return btpi.size();
		}

		@Override
		public BlueToothPairedInfo getItem(int position) {
			return btpi.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(activity,
						R.layout.btpair_list_item_layout, null);
				holder = new ViewHolder();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_name = (TextView) convertView.findViewById(R.id.bt_name);
			holder.iv_remove = (ImageView) convertView
					.findViewById(R.id.iv_remove);
			/*
			 * holder.iv_isconnect = (ImageView) convertView
			 * .findViewById(R.id.iv_isconnect);
			 */
			holder.tv_isconnect = (TextView) convertView
					.findViewById(R.id.tv_isconnect);
			final BlueToothPairedInfo blueToothInfo = btpi.get(position);
			String name = null;
			if (TextUtils.isEmpty(blueToothInfo.name)) {
				name = "该设备无名称";
			} else {
				name = blueToothInfo.name;
			}
			if (!TextUtils.isEmpty(address)
					&& blueToothInfo.address.equals(address)) {
				holder.tv_isconnect.setText("已经连接");
			} else {
				holder.tv_isconnect.setText("连接断开");
			}
			holder.tv_name.setText(name);
			holder.iv_remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					btpi.remove(blueToothInfo);
					deviceAdapter.notifyDataSetChanged();
					try {
						MainActivity.getService().disconnect();
						MainActivity.getService().deletePair(blueToothInfo.address);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			});
			return convertView;
		}
	}

	private static class ViewHolder {
		public TextView tv_name;
		public ImageView iv_remove;
		// public ImageView iv_isconnect;
		public TextView tv_isconnect;
	}

}
