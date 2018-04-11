package com.goodocom.gocsdk.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;

import com.goodocom.gocsdk.Commands;
import com.goodocom.gocsdk.Config;
import com.goodocom.gocsdk.HfpStatus;
import com.goodocom.gocsdk.IGocsdkService;
import com.goodocom.gocsdk.R;
import com.goodocom.gocsdk.fragment.FragmentCallPhone;
import com.goodocom.gocsdk.fragment.FragmentCallog;
import com.goodocom.gocsdk.fragment.FragmentMusic;
import com.goodocom.gocsdk.fragment.FragmentPairedList;
import com.goodocom.gocsdk.fragment.FragmentPhonebookList;
import com.goodocom.gocsdk.fragment.FragmentSearch;
import com.goodocom.gocsdk.fragment.FragmentSetting;
import com.goodocom.gocsdk.key.HomeKey;
import com.goodocom.gocsdk.key.HomeKey.OnHomePressedListener;
import com.goodocom.gocsdk.service.GocsdkCallbackImp;
import com.goodocom.gocsdk.service.GocsdkService;
import com.goodocom.gocsdk.service.PlayerService;
import com.goodocom.gocsdk.view.MyFragmentTabHost;

public class MainActivity extends FragmentActivity {
	public static final int MSG_COMING = 4;
	public static final int MSG_OUTGING = 5;
	public static final int MSG_TALKING = 6;
	public static final int MSG_HANGUP = 7;
	public static final int MSG_DEVICENAME = 11;
	public static final int MSG_DEVICEPINCODE = 12;
	public static final int MSG_UPDATE_PHONEBOOK = 17;
	public static final int MSG_UPDATE_PHONEBOOK_DONE = 18;
	public static final int MSG_SET_MICPHONE_ON = 19;
	public static final int MSG_SET_MICPHONE_OFF = 20;
	public static final int MSG_UPDATE_INCOMING_CALLLOG = 25;
	public static final int MSG_UPDATE_CALLOUT_CALLLOG = 26;
	public static final int MSG_UPDATE_MISSED_CALLLOG = 27;
	public static final int MSG_UPDATE_CALLLOG_DONE = 28;
	public static final int MSG_CURRENT_CONNECT_DEVICE_NAME = 29;

	public static String mLocalName = null;
	public static String mPinCode = null;
	public static String currentDeviceName = "";

	private Intent gocsdkService;
	private MyConn conn;
	public static GocsdkCallbackImp callback;
	private static IGocsdkService iGocsdkService;
	private static MyFragmentTabHost tabhost;

	private BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				Log.d("goc", "screen close!");
				try {
					iGocsdkService.closeBlueTooth();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else if (Intent.ACTION_SCREEN_ON.equals(action)) {
				Log.d("goc", "screen open!");
				try {
					iGocsdkService.openBlueTooth();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	};
	public static final int FIREST_POSITION = 2;
	private int mImageID[] = { R.drawable.btn_calllog_selector, R.drawable.btn_contact_selector,
			/*R.drawable.btn_message_selector,8*/R.drawable.btn_jianpan_selector, R.drawable.btn_music_selector,
			R.drawable.btn_btinfo_selector, R.drawable.btn_btpairlist_selector, R.drawable.btn_setting_selector };
	private Class[] mFragment = { FragmentCallog.class, FragmentPhonebookList.class,
			/*FragmentMessage.class,*/ FragmentCallPhone.class, FragmentMusic.class,
			FragmentSearch.class, FragmentPairedList.class, FragmentSetting.class };

	private String[] mString = new String[] { "通话记录", "通讯录",/* "短信信息",*/ "拨号盘", "蓝牙音乐", "蓝牙信息", "蓝牙配对列表", "设置" };

	private static Handler hand = null;

	public static Handler getHandler() {
		return hand;
	}

	public static IGocsdkService getService() {
		return iGocsdkService;
	}

	public static MyFragmentTabHost getTabHost() {
		return tabhost;
	}

	private HomeKey mHomeKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setHomeKey();
		new Thread() {
			public void run() {
				Commands.initCommands();
			};
		}.start();
		initScreenReceiver();
		gocsdkService = new Intent(MainActivity.this, GocsdkService.class);
		conn = new MyConn();
		startService(gocsdkService);
		bindService(gocsdkService, conn, BIND_AUTO_CREATE);

        if(Config.JAVA_PLAYER) {
		    Intent playerService = new Intent(this, PlayerService.class);
		    startService(playerService);
        }

		initView();
		tabhost.setCurrentTab(FIREST_POSITION);
		callback = new GocsdkCallbackImp();
		hand = handler;
	}
	private void initScreenReceiver() {
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(Intent.ACTION_SCREEN_OFF);
		mFilter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(mScreenReceiver, mFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.d("app", "MainActivity onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		mHomeKey.stopWatch();
		Log.d("app", "MainActivity onDestroy");
		unregisterReceiver(mScreenReceiver);
		// 注销蓝牙回调
		try {
			iGocsdkService.unregisterCallback(callback);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		// 解绑服务
		unbindService(conn);
		super.onDestroy();
	}

	private class MyConn implements ServiceConnection {
		// 绑定成功之后 会调用该方法
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d("app", "onServiceConnected");
			iGocsdkService = IGocsdkService.Stub.asInterface(service);
			try {
				iGocsdkService.registerCallback(callback);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        iGocsdkService.inqueryHfpStatus();
                        iGocsdkService.inqueryA2dpStatus();
                        iGocsdkService.musicUnmute();
                        iGocsdkService.getLocalName();
                        iGocsdkService.getPinCode();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }, 500);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
	}
	
	private void initView() {
		tabhost = (MyFragmentTabHost) findViewById(android.R.id.tabhost);
		tabhost.setup(MainActivity.this, getSupportFragmentManager(), R.id.fl_content_show);
		for (int i = 0; i < mImageID.length; i++) {
			TabSpec tabSpec = tabhost.newTabSpec(mString[i]).setIndicator(getTabItemView(i));
			tabhost.addTab(tabSpec, mFragment[i], null);
		}
	}
	
	private View getTabItemView(int index) {
		View view = View.inflate(this, R.layout.tabhost_item_view, null);
		ImageView iv_flg = (ImageView) view.findViewById(R.id.iv_flg);
		iv_flg.setImageResource(mImageID[index]);
		return view;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_HANGUP:
				break;
			case MSG_COMING: {// 来电
				String number = (String) msg.obj;
				IncomingActivity.start(MainActivity.this, HfpStatus.INCOMING, number);
				break;
			}
			case MSG_TALKING: {// 通话中
				String number = (String) msg.obj;
				if ((!IncomingActivity.running) && (!CallActivity.running)) {
					CallActivity.start(MainActivity.this, HfpStatus.TALKING, number);
				}
				break;
			}
			case MSG_OUTGING:// 拨出
				String number = (String) msg.obj;
				if ((!IncomingActivity.running) && (!CallActivity.running)) {
					CallActivity.start(MainActivity.this, HfpStatus.CALLING, number);
				}
				break;
			case MSG_DEVICENAME:// 蓝牙设备名称
				String name = (String) msg.obj;
				mLocalName = name;
				break;
			case MSG_DEVICEPINCODE:// 蓝牙设备的PIN码
				String pincode = (String) msg.obj;
				mPinCode = pincode;
				break;
			case MSG_CURRENT_CONNECT_DEVICE_NAME:
				currentDeviceName = (String) msg.obj;
				break;
			}
		};
	};

	private void setHomeKey() {
		mHomeKey = new HomeKey(this);
		mHomeKey.setOnHomePressedListener(new OnHomePressedListener() {

			@Override
			public void onHomePressed() {
				finish();
			}

			@Override
			public void onHomeLongPressed() {
				finish();
			}
		});
		mHomeKey.startWatch();
	}
}
