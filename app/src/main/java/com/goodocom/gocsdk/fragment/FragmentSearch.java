package com.goodocom.gocsdk.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.goodocom.gocsdk.R;
import com.goodocom.gocsdk.activity.MainActivity;
import com.goodocom.gocsdk.domain.BlueToothInfo;
import com.goodocom.gocsdk.service.GocsdkCallbackImp;
import com.goodocom.gocsdk.view.TransparentDialog;

import java.util.ArrayList;
import java.util.List;

public class FragmentSearch extends Fragment implements OnClickListener {

	public static final int MSG_SEARCHE_DEVICE = 0;
	public static final int MSG_SEARCHE_DEVICE_DONE = 1;
	public static final int MSG_DEVICE_NAME = 2;
	public static final int MSG_PIN_CODE = 3;
	public static final int MSG_CONNECT_SUCCESS = 4;
	public static final int MSG_CONNECT_FAILE = 5;
	public static final int MSG_HFP_STATUS = 6;

	private MainActivity activity;
	private ImageView iv_search_bt;
	private ListView lv_device_list;
	private Button btn_device_name;
	private Button btn_pin_code;
	private List<BlueToothInfo> bts = new ArrayList<BlueToothInfo>();
	private MyAdapter adapter;
	private boolean isSearch = false;
	private static Handler hand = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SEARCHE_DEVICE: {
				BlueToothInfo info = (BlueToothInfo) msg.obj;
				bts.add(info);
				adapter.notifyDataSetChanged();
				isSearch = true;
				break;
			}
			case MSG_SEARCHE_DEVICE_DONE: {
				Toast.makeText(activity, "搜索完成", Toast.LENGTH_SHORT).show();
				iv_anim.setVisibility(View.GONE);
				iv_anim.clearAnimation();
				isSearch = false;
				break;
			}
			case MSG_DEVICE_NAME: {
				String deviceName = (String) msg.obj;
				btn_device_name.setText(deviceName);
				break;
			}
			case MSG_PIN_CODE: {
				String pinCode = (String) msg.obj;
				btn_pin_code.setText(pinCode);
				break;
			}
			case MSG_CONNECT_SUCCESS:
				try {
					MainActivity.getService().getCurrentDeviceName();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				showConnected();
				if (dialog != null) {
					dialog.dismiss();
				}
				break;
			case MSG_CONNECT_FAILE:
				if (dialog != null) {
					dialog.dismiss();
				}
				showSearch();
				break;
			case MSG_HFP_STATUS:
				int status = (Integer) msg.obj;
				if(status>=3){
					showConnected();
				}else{
					showSearch();
				}
				break;
			}
		};
	};
	private ImageView iv_anim;
	private TransparentDialog dialog;
	private LinearLayout ll_bt_left;
	private LinearLayout ll_connected;
	private TextView tv_content_name;

	public static Handler getHandler() {
		return hand;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View
				.inflate(activity, R.layout.fragmentbluetoothinfo, null);
		initView(view);
		hand = handler;
		return view;
	}

	private void initView(View view) {
		ll_bt_left = (LinearLayout) view.findViewById(R.id.ll_bt_left);
		iv_search_bt = (ImageView) view.findViewById(R.id.iv_search_bt);
		lv_device_list = (ListView) view.findViewById(R.id.lv_device_list);
		lv_device_list.setSelector(R.drawable.contact_list_item_selector);
		btn_device_name = (Button) view.findViewById(R.id.btn_device_name);
		btn_pin_code = (Button) view.findViewById(R.id.btn_pin_code);
		iv_anim = (ImageView) view.findViewById(R.id.iv_anim);
		ll_connected = (LinearLayout) view.findViewById(R.id.ll_connected);
		tv_content_name = (TextView) view.findViewById(R.id.tv_content_name);
		if (GocsdkCallbackImp.hfpStatus >= 3) {
			showConnected();
		} else {
			showSearch();
		}
		if (!TextUtils.isEmpty(MainActivity.mLocalName)) {
			btn_device_name.setText(MainActivity.mLocalName);
		} else {
			btn_device_name.setText("hello");
		}
		if (!TextUtils.isEmpty(MainActivity.mPinCode)) {
			btn_pin_code.setText(MainActivity.mPinCode);
		} else {
			btn_pin_code.setText("0000");
		}
		iv_search_bt.setOnClickListener(this);
		adapter = new MyAdapter();
		lv_device_list.setAdapter(adapter);
		lv_device_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (GocsdkCallbackImp.hfpStatus >= 3) {
					Toast.makeText(activity, "已有设备连接", Toast.LENGTH_SHORT)
							.show();
				} else {
					try {
						MainActivity.getService().stopDiscovery();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					connectDevice(position);
				}
			}

		});
	}

	private void connectDevice(int position) {
		showPopupWindow();
		BlueToothInfo blueToothInfo = adapter.getItem(position);
		if (TextUtils.isEmpty(blueToothInfo.name)) {
			MainActivity.currentDeviceName = blueToothInfo.address;
		} else {
			MainActivity.currentDeviceName = blueToothInfo.name;
		}
		try {
			MainActivity.getService().connectDevice(blueToothInfo.address);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void showSearch() {
		ll_connected.setVisibility(View.INVISIBLE);
		// iv_connected.setVisibility(View.INVISIBLE);
		ll_bt_left.setVisibility(View.VISIBLE);
		tv_content_name.setText("");

	}

	private void showConnected() {
		ll_connected.setVisibility(View.VISIBLE);
		ll_bt_left.setVisibility(View.INVISIBLE);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (MainActivity.currentDeviceName != null) {
					tv_content_name.setText(MainActivity.currentDeviceName);
				}
			}
		}, 200);

	}

	protected void showPopupWindow() {
		dialog = new TransparentDialog(activity, R.style.transparentdialog);
		dialog.setCanceledOnTouchOutside(false);// 点击不消失
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search_bt:
			if (isSearch) {
				iv_anim.setVisibility(View.GONE);
				iv_anim.clearAnimation();
				try {
					MainActivity.getService().stopDiscovery();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				iv_anim.setVisibility(View.VISIBLE);
				RotateAnimation ra = new RotateAnimation(0.0f, 359.0f,
						RotateAnimation.RELATIVE_TO_SELF, 0.5f,
						RotateAnimation.RELATIVE_TO_SELF, 0.5f);
				ra.setRepeatCount(-1);
				ra.setRepeatMode(Animation.RESTART);
				ra.setDuration(1000);
				LinearInterpolator li = new LinearInterpolator();
				ra.setInterpolator(li);
				iv_anim.startAnimation(ra);

				bts.clear();
				adapter.notifyDataSetChanged();
				try {
					MainActivity.getService().startDiscovery();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return bts.size();
		}

		@Override
		public BlueToothInfo getItem(int position) {
			return bts.get(position);
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
						R.layout.bluetooth_listview_item_layout, null);
				holder = new ViewHolder();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			BlueToothInfo blueToothInfo = bts.get(position);
			String name = null;
			if (TextUtils.isEmpty(blueToothInfo.name)) {
				name = "该设备无名称";
			} else {
				name = blueToothInfo.name;
			}
			holder.tv_name.setText(name);
			holder.tv_address.setText(blueToothInfo.address);
			return convertView;
		}
	}

	private static class ViewHolder {
		public TextView tv_name;
		public TextView tv_address;
	}

}
