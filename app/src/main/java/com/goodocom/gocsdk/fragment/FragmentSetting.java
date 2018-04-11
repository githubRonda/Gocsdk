package com.goodocom.gocsdk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodocom.gocsdk.R;
import com.goodocom.gocsdk.activity.MainActivity;
import com.goodocom.gocsdk.view.MyEditText;
import com.goodocom.gocsdk.view.MyEditText.onFinishComposingListener;

public class FragmentSetting extends Fragment implements OnClickListener {

	public static final int MSG_DEVICE_NAME = 0;
	public static final int MSG_PIN_CODE = 1;
	public static final int MSG_AUTO_STATUS = 2;

	private boolean isConnectSwitch = false;
	private boolean isAnswerSwitch = false;

	private MainActivity activity;
	private static Handler hand = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_DEVICE_NAME:
				String deviceName = (String) msg.obj;
				et_device_name.setText(deviceName);
				et_device_name.setSelection(et_device_name.getText().length(), et_device_name.getText().length());
				break;
			case MSG_PIN_CODE:
				String pinCode = (String) msg.obj;
				et_pin_code.setText(pinCode);
				et_pin_code.setSelection(et_pin_code.getText().length(), et_pin_code.getText().length());
				break;
			case MSG_AUTO_STATUS:
				String autoStatus = (String) msg.obj;
				if (autoStatus.charAt(0) != '0') {
					auto_connect_switch.setImageResource(R.drawable.ico_4157_kai);
				} else {
					auto_connect_switch.setImageResource(R.drawable.ico_4158_guan);
				}
				if (autoStatus.charAt(1) != '0') {
					auto_answer_switch.setImageResource(R.drawable.ico_4157_kai);
				} else {
					auto_answer_switch.setImageResource(R.drawable.ico_4158_guan);
				}
				break;
			}
		};
	};

	public static Handler getHandler() {
		return hand;
	}

	private MyEditText et_device_name;
	private MyEditText et_pin_code;
	private ImageView auto_connect_switch;
	private ImageView auto_answer_switch;
	private TextView tv_imei;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(activity, R.layout.fragmentsettings, null);

		initView(view);
		initData();
		hand = handler;
		return view;
	}

	private void initData() {
		try {
			if(MainActivity.getService() != null){
				MainActivity.getService().getAutoConnectAnswer();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private View initView(View view) {
		et_device_name = (MyEditText) view.findViewById(R.id.et_device_name);
		et_pin_code = (MyEditText) view.findViewById(R.id.et_pin_code);
		auto_connect_switch = (ImageView) view.findViewById(R.id.auto_connect_switch);
		auto_answer_switch = (ImageView) view.findViewById(R.id.auto_answer_switch);
		tv_imei = (TextView) view.findViewById(R.id.tv_imei);
		setImei();
		auto_answer_switch.setOnClickListener(this);
		auto_connect_switch.setOnClickListener(this);

		if (!TextUtils.isEmpty(MainActivity.mLocalName)) {
			et_device_name.setText(MainActivity.mLocalName);
		} else {
			et_device_name.setText("hello");
		}
		if (!TextUtils.isEmpty(MainActivity.mPinCode)) {
			et_pin_code.setText(MainActivity.mPinCode);
		} else {
			et_pin_code.setText("0000");
		}

		et_device_name.setOnFinishComposingListener(new onFinishComposingListener() {

			@Override
			public void finishComposing() {

				String deviceName = et_device_name.getText().toString();
				if (!TextUtils.isEmpty(deviceName)) {
					try {
						MainActivity.getService().setLocalName(deviceName);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				Handler handler = FragmentSearch.getHandler();
				if (handler != null) {
					Message msg = Message.obtain();
					msg.what = FragmentSearch.MSG_DEVICE_NAME;
					msg.obj = deviceName;
					handler.sendMessage(msg);
				}

			}
		});
		et_pin_code.setOnFinishComposingListener(new onFinishComposingListener() {

			@Override
			public void finishComposing() {

				String pinCode = et_pin_code.getText().toString();
				if (!TextUtils.isEmpty(pinCode)) {
					try {
						MainActivity.getService().setPinCode(pinCode);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				Handler handler = FragmentSearch.getHandler();
				if (handler != null) {
					Message msg = Message.obtain();
					msg.what = FragmentSearch.MSG_PIN_CODE;
					msg.obj = pinCode;
					handler.sendMessage(msg);
				}

			}
		});
		return view;
	}

	private void setImei() {
		try {
			TelephonyManager telephonyManager = ((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE));
			if (telephonyManager != null) {
				String imei = telephonyManager.getDeviceId();
				if (imei != null) tv_imei.setText(imei);
			}
		}catch (java.lang.SecurityException exception){

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.auto_connect_switch:
			isConnectSwitch();
			break;
		case R.id.auto_answer_switch:
			isAnswerSwitch();
			break;
		}
	}

	private void isAnswerSwitch() {
		isAnswerSwitch = !isAnswerSwitch;
		if (isAnswerSwitch) {
			auto_answer_switch.setImageResource(R.drawable.ico_4157_kai);
			try {
				MainActivity.getService().setAutoAnswer();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			auto_answer_switch.setImageResource(R.drawable.ico_4158_guan);
			try {
				MainActivity.getService().cancelAutoAnswer();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	private void isConnectSwitch() {
		isConnectSwitch = !isConnectSwitch;
		if (isConnectSwitch) {
			auto_connect_switch.setImageResource(R.drawable.ico_4157_kai);
			try {
				MainActivity.getService().setAutoConnect();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			auto_connect_switch.setImageResource(R.drawable.ico_4158_guan);
			try {
				MainActivity.getService().cancelAutoConnect();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

}
