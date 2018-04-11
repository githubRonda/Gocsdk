package com.goodocom.gocsdk.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.goodocom.gocsdk.R;
import com.goodocom.gocsdk.activity.MainActivity;
import com.goodocom.gocsdk.activity.TransActivity;
import com.goodocom.gocsdk.db.GocDatabase;
import com.goodocom.gocsdk.service.GocsdkCallbackImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentPhonebookList extends Fragment implements OnClickListener {
	public final static int MSG_PHONE_BOOK = 1;// 更新联系人
	public final static int MSG_PHONE_BOOK_DONE = 2;// 更新联系人结束
	public final static int MSG_CURRENT_DEVICE_ADDRESS = 3;
	public final static int MSG_DEVICE_CONNECT = 4;
	public final static int MSG_CONNECT_FAILE = 5;

	private TextView tv_contacts_count;
	private ImageView image_animation;
	private RelativeLayout rl_downloading;
	private TextView tv_device_disconnect;
	private MainActivity activity;
	private ListView lv_content;
	private SimpleAdapter simpleAdapter;
	private List<Map<String, String>> contacts = new ArrayList<Map<String, String>>();
	private String address;
	private boolean isDisconnect = false;

	private void saveDeviceAddress(String address) {
		SharedPreferences sp = activity.getSharedPreferences("config",
				MainActivity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("address", address);
		editor.commit();
	};

	private String getDeviceAddress() {
		SharedPreferences sp = activity.getSharedPreferences("config",
				MainActivity.MODE_PRIVATE);
		String string = sp.getString("address", "");
		return string;
	}

	// 静态内部类
	public static class Phonebook {
		public String name = null;
		public String num = null;
	}

	public static Handler hand = null;

	public static Handler getHandler() {
		return hand;
	}

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_PHONE_BOOK:
				Phonebook phonebook = (Phonebook) msg.obj;
				Map<String, String> phoBook = new HashMap<String, String>();
				phoBook.put("itemName", phonebook.name);
				phoBook.put("itemNum", phonebook.num);
				contacts.add(phoBook);
				// 更改联系人个数
				tv_contacts_count.setText("正在更新联系人： "+contacts.size());
				simpleAdapter.notifyDataSetChanged();

				GocDatabase.getDefault().insertPhonebook(phonebook.name,phonebook.num);
				break;
			case MSG_PHONE_BOOK_DONE: {
				// 如果该数据库不为空，关闭该数据库，并赋值为null
				Handler handlerFinish = TransActivity.getHandler();
				if(handlerFinish!=null){
					handlerFinish.sendEmptyMessage(TransActivity.MSG_FINISH);
				}
				if(isDisconnect){
					showDisconnect();
				}else{
					showData();
				}
				break;
			}
			case MSG_CURRENT_DEVICE_ADDRESS:
				address = (String) msg.obj;
				String deviceAddress = getDeviceAddress();
				if (!deviceAddress.equals(address) && contacts.size() > 0
						&& simpleAdapter != null) {
					contacts.clear();
					simpleAdapter.notifyDataSetChanged();
				}
				saveDeviceAddress(address);
				break;
			case MSG_DEVICE_CONNECT:
				isDisconnect = false;
				showConnect();
				break;
			case MSG_CONNECT_FAILE:
				isDisconnect = true;
				Handler handlerFinish = TransActivity.getHandler();
				if(handlerFinish!=null){
					handlerFinish.sendEmptyMessage(TransActivity.MSG_FINISH);
				}
				showDisconnect();
				break;
			}
		}

	};
	private TextView tv_download;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(activity, R.layout.fragmentmaillist, null);
		initView(view);
		Log.d("app","GocsdkCallbackImp.hfpStatus="+GocsdkCallbackImp.hfpStatus);
		if (GocsdkCallbackImp.hfpStatus >= 3) {
			showConnect();
		} else {
			showDisconnect();
		}
		hand = handler;
		return view;
	}

	private void showConnect() {
		rl_downloading.setVisibility(View.GONE);
		lv_content.setVisibility(View.GONE);
		tv_device_disconnect.setVisibility(View.GONE);
		tv_download.setVisibility(View.VISIBLE);
	}

	private void showData() {
		rl_downloading.setVisibility(View.GONE);
		lv_content.setVisibility(View.VISIBLE);
		tv_device_disconnect.setVisibility(View.GONE);
	}

	private void showDisconnect() {
		contacts.clear();
		simpleAdapter.notifyDataSetChanged();
		tv_contacts_count.setText("");
		rl_downloading.setVisibility(View.GONE);
		lv_content.setVisibility(View.GONE);
		tv_device_disconnect.setVisibility(View.VISIBLE);
		tv_download.setVisibility(View.GONE);
	}

	private void initView(View view) {
		lv_content = (ListView) view.findViewById(R.id.lv_content);
		tv_device_disconnect = (TextView) view
				.findViewById(R.id.tv_device_disconnect);
		lv_content.setSelector(R.drawable.contact_list_item_selector);
		rl_downloading = (RelativeLayout) view
				.findViewById(R.id.rl_downloading);
		image_animation = (ImageView) view.findViewById(R.id.image_animation);
		tv_contacts_count = (TextView) view
				.findViewById(R.id.tv_contacts_count);
		tv_download = (TextView) view.findViewById(R.id.tv_download);
		simpleAdapter = new SimpleAdapter(activity, contacts,
				R.layout.contacts_listview_item, new String[] { "itemName",
						"itemNum" }, new int[] { R.id.tv_name, R.id.tv_number });
		lv_content.setAdapter(simpleAdapter);
		lv_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (GocsdkCallbackImp.hfpStatus >= 3) {
					clickItemCallPhone(position);
				} else {
					Toast.makeText(activity, "请您先连接设备", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		tv_download.setOnClickListener(this);
	}

	private void clickItemCallPhone(int position) {
		// 获得点击联系人的信息
		HashMap<String, String> People = (HashMap<String, String>) simpleAdapter
				.getItem(position);
		String Name = People.get("itemName");
		final String Num = People.get("itemNum");
		createCallOutDialog(Name, Num);
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
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	private void donwLoadData() {
		Intent intent = new Intent(activity, TransActivity.class);
		startActivity(intent);
		reflashContactsData();
	}

	private void reflashContactsData() {
		try {
			Handler mainActivityHandler = MainActivity.getHandler();
			if (mainActivityHandler == null) {
				return;
			}
			mainActivityHandler.sendEmptyMessage(MainActivity.MSG_UPDATE_PHONEBOOK);
			// 判断联系人列表是否为空，不为空时清空它。
			if (contacts.isEmpty() == false) {
				contacts.clear();
			}
			GocDatabase.getDefault().clearPhonebook();
			// 联系人列表下载
			MainActivity.getService().phoneBookStartUpdate();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void callOut(String phoneNumber2) {
		placeCall(phoneNumber2);
	}

	// 拨打正确的电话
	private static void placeCall(String mLastNumber) {
		
		if (mLastNumber.length() == 0)
			return;
		if (PhoneNumberUtils.isGlobalPhoneNumber(mLastNumber)) {
			if (mLastNumber == null || !TextUtils.isGraphic(mLastNumber)) {
				return;
			}
			
			try {
				MainActivity.getService().phoneDail(mLastNumber);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View view) {
		if(tv_download == view){
			donwLoadData();
			showDownLoading();
		}
	}

	private void showDownLoading() {
		rl_downloading.setVisibility(View.VISIBLE);
		lv_content.setVisibility(View.GONE);
		tv_device_disconnect.setVisibility(View.GONE);
		tv_download.setVisibility(View.GONE);
		
		
		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.8f);
		animation.setDuration(1000);
		animation.setFillAfter(false);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		image_animation.startAnimation(animation);
	}
}
