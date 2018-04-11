package com.goodocom.gocsdk.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.goodocom.gocsdk.R;
import com.goodocom.gocsdk.activity.MainActivity;
import com.goodocom.gocsdk.domain.CallLogInfo;
import com.goodocom.gocsdk.service.GocsdkCallbackImp;
import com.goodocom.gocsdk.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentCallog extends Fragment implements OnClickListener {
	private MainActivity activity;
	private ImageView ib_call_in;
	private ImageView ib_call_out;
	private ImageView ib_call_missed;
	private NoScrollViewPager vp_content;
	private String[] callLogString = { "拨出", "打进", "未接" };
	private static final int CALLLOG_IN = 3;
	private static final int CALLLOG_OUT = 1;
	private static final int CALLLOG_MISS = 2;
	private List<ListView> listViews = new ArrayList<ListView>();
	private View view;

	public List<Map<String, String>> call_log_in = new ArrayList<Map<String, String>>();
	public List<Map<String, String>> call_log_out = new ArrayList<Map<String, String>>();
	public List<Map<String, String>> call_log_miss = new ArrayList<Map<String, String>>();
	private SimpleAdapter mSimpleAdapterIn;
	private SimpleAdapter mSimpleAdapterOut;
	private SimpleAdapter mSimpleAdapterMiss;
	public final static int MSG_CALLLOG = 1;// 通话记录下载
	public final static int MSG_CALLLOG_DONE = 2;// 通话记录下载结束
	public final static int MSG_DEVICE_CONNECTED = 3;
	public final static int MSG_DEVICE_DISCONNECTED = 4;
	private static Handler hand = null;
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_CALLLOG: {
				CallLogInfo info = (CallLogInfo) msg.obj;

				Map<String, String> map = null;
				switch (info.type) {
				case 4: {
					if (call_log_out.size() <= 100) {
						map = new HashMap<String, String>();
						String name = null;
						if (TextUtils.isEmpty(info.name)) {
                            name = "未知号码";
						} else {
                            name = info.name;
						}
						map.put("itemName", name);
						map.put("itemNum", info.number);
						call_log_out.add(map);
						mSimpleAdapterOut.notifyDataSetChanged();
					}
					break;
				}
				case 5: {
					if (call_log_miss.size() <= 100) {
						map = new HashMap<String, String>();
						String name = null;
						if (TextUtils.isEmpty(info.name)) {
                            name = "未知号码";
						} else {
                            name = info.name;
						}
						map.put("itemName", name);
						map.put("itemNum", info.number);
						call_log_miss.add(map);
						mSimpleAdapterMiss.notifyDataSetChanged();
					}
					break;
				}
				case 6: {
					if (call_log_in.size() <= 100) {
						map = new HashMap<String, String>();
						String name = null;
						if (TextUtils.isEmpty(info.name)) {
                            name = "未知号码";
						} else {
                            name = info.name;
						}
						map.put("itemName", name);
						map.put("itemNum", info.number);
						call_log_in.add(map);
						mSimpleAdapterIn.notifyDataSetChanged();
					}
					break;
				}
				}
			}
			case MSG_CALLLOG_DONE:
				//Toast.makeText(activity, "当前通话记录下载完毕！", 0).show();
				rl_downloading.setVisibility(View.GONE);
				vp_content.setVisibility(View.VISIBLE);
				break;
			case MSG_DEVICE_CONNECTED:
				showConnect();
				break;
			case MSG_DEVICE_DISCONNECTED:
				showDisconnect();
				break;
			}
		};
	};
	private Handler mainHandler;
	private ImageView image_animation;
	private RelativeLayout rl_downloading;
	private TextView tv_device_disconnected;
	private FrameLayout fl_content;
	private LinearLayout ll_title;

	public static Handler getHandler() {
        return hand;
	}

	// 创建该对象时，调用该方法
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	// 加载页面
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(activity, R.layout.fragmentcalllog, null);
		initView();
		initEvents();
		Log.d("app","GocsdkCallbackImp.hfpStatus="+GocsdkCallbackImp.hfpStatus);
		if (GocsdkCallbackImp.hfpStatus >= 3) {
			showConnect();
		} else {
			showDisconnect();
		}
		hand = handler;
		return view;
	}

	private void initEvents() {
		ib_call_in.setOnClickListener(this);
		ib_call_out.setOnClickListener(this);
		ib_call_missed.setOnClickListener(this);
	}

	private void showDisconnect() {
		ll_title.setVisibility(View.GONE);
		fl_content.setVisibility(View.GONE);
		tv_device_disconnected.setVisibility(View.VISIBLE);
	}

	private void showConnect() {
		ll_title.setVisibility(View.VISIBLE);
		fl_content.setVisibility(View.VISIBLE);
		tv_device_disconnected.setVisibility(View.GONE);
	}

	private void initView() {
		ll_title = (LinearLayout) view.findViewById(R.id.ll_title);
		fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
		tv_device_disconnected = (TextView) view.findViewById(R.id.tv_device_disconnect);
		ib_call_in = (ImageView) view.findViewById(R.id.ib_call_in);
		ib_call_out = (ImageView) view.findViewById(R.id.ib_call_out);
		ib_call_missed = (ImageView) view.findViewById(R.id.ib_call_missed);
		vp_content = (NoScrollViewPager) view.findViewById(R.id.vp_content);
		rl_downloading = (RelativeLayout) view.findViewById(R.id.rl_downloading);

		image_animation = (ImageView) view.findViewById(R.id.image_animation);
		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.8f);
		animation.setDuration(1000);
		animation.setFillAfter(false);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		image_animation.startAnimation(animation);
		InitData();
		vp_content.setAdapter(new MyPagerAdapter());

		for (int i = 0; i < callLogString.length; i++) {
			ListView lv_item = new ListView(activity);
			lv_item.setVerticalScrollBarEnabled(false);
			lv_item.setSelector(R.drawable.contact_list_item_selector);
			paddingData(lv_item, i);
			listViews.add(lv_item);
		}
	}

	private void InitData() {
		LoadIncomingData();
	}

	// 切换页面
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_call_in:
		    ib_call_in.setBackgroundColor(0x700000FF);
            ib_call_out.setBackgroundColor(0x00000000);
            ib_call_missed.setBackgroundColor(0x00000000);

			LoadIncomingData();
            ListView lv_in = listViews.get(1);
            paddingData(lv_in, 1);
			vp_content.setCurrentItem(0, false);
			break;
		case R.id.ib_call_out:
            ib_call_in.setBackgroundColor(0x00000000);
            ib_call_out.setBackgroundColor(0x700000FF);
            ib_call_missed.setBackgroundColor(0x00000000);

			LoadCalloutData();
			ListView lv_callout = listViews.get(1);
			paddingData(lv_callout, 1);
			vp_content.setCurrentItem(1, false);
			break;
		case R.id.ib_call_missed:
            ib_call_in.setBackgroundColor(0x00000000);
            ib_call_out.setBackgroundColor(0x00000000);
            ib_call_missed.setBackgroundColor(0x700000FF);

			LoadMissedData();
			ListView lv_missed = listViews.get(2);
			paddingData(lv_missed, 2);
			vp_content.setCurrentItem(2, false);
			break;
		}
	}

	private void LoadMissedData() {
		if (GocsdkCallbackImp.hfpStatus >= 3) {
			if (call_log_miss.size() == 0) {
				rl_downloading.setVisibility(View.VISIBLE);
				vp_content.setVisibility(View.GONE);
				if (mainHandler != null) {
					mainHandler
							.sendEmptyMessage(MainActivity.MSG_UPDATE_MISSED_CALLLOG);
				}
				call_log_miss.clear();
				try {
					MainActivity.getService().callLogstartUpdate(CALLLOG_MISS);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void LoadCalloutData() {
		if (GocsdkCallbackImp.hfpStatus >= 3) {
			if (call_log_out.size() == 0) {
				rl_downloading.setVisibility(View.VISIBLE);
				vp_content.setVisibility(View.GONE);
				if (mainHandler != null) {
					mainHandler
							.sendEmptyMessage(MainActivity.MSG_UPDATE_CALLOUT_CALLLOG);
				}
				call_log_out.clear();
				try {
					MainActivity.getService().callLogstartUpdate(CALLLOG_OUT);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void LoadIncomingData() {
		if (call_log_in.size() == 0) {
			rl_downloading.setVisibility(View.VISIBLE);
			vp_content.setVisibility(View.GONE);
			mainHandler = MainActivity.getHandler();
			mainHandler
					.sendEmptyMessage(MainActivity.MSG_UPDATE_INCOMING_CALLLOG);
			call_log_in.clear();
			try {
				MainActivity.getService().callLogstartUpdate(CALLLOG_IN);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ListView listView = listViews.get(position);
			ViewGroup parent = (ViewGroup) listView.getParent();
			if (parent != null) {
				parent.removeView(listView);
			}
			container.addView(listView);
			return listView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	private void clickItemCall(SimpleAdapter mSimpleAdapter, int position) {

		if (mSimpleAdapter != null) {
			Map<String, String> map = (Map<String, String>) mSimpleAdapter.getItem(position);
			String Name = map.get("itemName");
			final String Num = map.get("itemNum");
			createCallOutDialog(Name,Num);
		}

	}
	private void createCallOutDialog(String Name, final String Num) {
		AlertDialog.Builder builder = new Builder(activity);
		builder.setMessage("确定要拨打吗?" + Name + ":" + Num);
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						callOut(Num);
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
	
	
	private void callOut(String phoneNumber2) {
		placeCall(phoneNumber2);
	}

	// 给控件填充数据
	public void paddingData(ListView listView, int position) {

		switch (position) {
		case 0:
			mSimpleAdapterIn = new SimpleAdapter(activity, call_log_in,
					R.layout.call_log_in_listview_item_view, new String[] {
							"itemName", "itemNum", "itemTime" },
					new int[] { R.id.tv_in_name, R.id.tv_in_number, R.id.tv_in_time });
			listView.setAdapter(mSimpleAdapterIn);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (GocsdkCallbackImp.hfpStatus >= 3) {
						clickItemCall(mSimpleAdapterIn, position);
					} else {
						Toast.makeText(activity, "请您先连接设备", Toast.LENGTH_SHORT).show();
					}
				}

			});
			break;
		case 1:
			mSimpleAdapterOut = new SimpleAdapter(activity,
					call_log_out,
					R.layout.call_log_out_listview_item_view,
					new String[] {"itemName", "itemNum", "itemTime" },
					new int[] {R.id.tv_out_name, R.id.tv_out_number, R.id.tv_out_time });

			listView.setAdapter(mSimpleAdapterOut);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (GocsdkCallbackImp.hfpStatus >= 3) {
						clickItemCall(mSimpleAdapterOut, position);
					} else {
						Toast.makeText(activity, "请您先连接设备", Toast.LENGTH_SHORT).show();
					}

				}
			});
			break;
		case 2:
			mSimpleAdapterMiss = new SimpleAdapter(activity,
					call_log_miss,
					R.layout.call_log_miss_listview_item_view,
					new String[] {"itemName", "itemNum", "itemTime" },
					new int[] {R.id.tv_miss_name, R.id.tv_miss_number, R.id.tv_miss_time });

			listView.setAdapter(mSimpleAdapterMiss);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (GocsdkCallbackImp.hfpStatus >= 3) {
						clickItemCall(mSimpleAdapterMiss, position);
					} else {
						Toast.makeText(activity, "请您先连接设备", Toast.LENGTH_SHORT).show();
					}

				}
			});
			break;
		}

	}

	// 拨打正确的电话
	private static void placeCall(String mLastNumber) {
		if (mLastNumber.length() == 0)
			return;
		if (PhoneNumberUtils.isGlobalPhoneNumber(mLastNumber)) {
			// place the call if it is a valid number
			if (mLastNumber == null || !TextUtils.isGraphic(mLastNumber)) {
				// There is no number entered.
				return;
			}
			try {
				MainActivity.getService().phoneDail(mLastNumber);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}
