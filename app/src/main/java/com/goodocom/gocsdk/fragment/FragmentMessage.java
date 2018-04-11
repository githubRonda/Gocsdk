package com.goodocom.gocsdk.fragment;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodocom.gocsdk.R;
import com.goodocom.gocsdk.activity.MainActivity;
import com.goodocom.gocsdk.event.MessageListEvent;
import com.goodocom.gocsdk.event.MessageTextEvent;
import com.goodocom.gocsdk.service.GocsdkCallbackImp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FragmentMessage extends Fragment implements OnClickListener {

	private MainActivity activity;
	private Button btn_message_in;
	private Button btn_message_out;
	private Button btn_message_rubbish;
	private ListView lv_message_in;
	private RelativeLayout rl_message_out;
	private RelativeLayout rl_write_message;
	private ListView lv_select_contact;
	private RelativeLayout rl_message_detail_content;
	private ListView lv_rubbish_message;
	private Button btn_write_message;
	private ListView lv_message_content;
	private Button btn_return;
	private EditText et_addressee;
	private Button btn_select_contact;
	private EditText et_message_content;
	private Button btn_send;
	private List<MessageListEvent> inMessages = new ArrayList<MessageListEvent>();
	private List<MessageListEvent> outMessages = new ArrayList<MessageListEvent>();
	private List<MessageListEvent> delMessages = new ArrayList<MessageListEvent>();
	private String message_content;
	private MyInAdapter inAdapter;
	private Button btn_detail_return;
	private TextView tv_addressee_name;
	private TextView tv_addressee_number;
	private Button btn_other;
	private TextView tv_message_detail_content;
	private MyOutAdapter outAdapter;
	private MyDelAdapter delAdapter;
	private int pager = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View.inflate(activity, R.layout.fragmentmessage, null);
		// 用来接收消息的
		EventBus.getDefault().register(this);

		btn_message_in = (Button) view.findViewById(R.id.btn_message_in);
		btn_message_out = (Button) view.findViewById(R.id.btn_message_out);
		btn_message_rubbish = (Button) view
				.findViewById(R.id.btn_message_rubbish);

		lv_message_in = (ListView) view.findViewById(R.id.lv_message_in);

		rl_message_out = (RelativeLayout) view
				.findViewById(R.id.rl_message_out);
		btn_write_message = (Button) view.findViewById(R.id.btn_write_message);
		lv_message_content = (ListView) view
				.findViewById(R.id.lv_message_content);

		rl_write_message = (RelativeLayout) view
				.findViewById(R.id.rl_write_message);
		btn_return = (Button) view.findViewById(R.id.btn_return);
		et_addressee = (EditText) view.findViewById(R.id.et_addressee);
		btn_select_contact = (Button) view
				.findViewById(R.id.btn_select_contact);
		et_message_content = (EditText) view
				.findViewById(R.id.et_message_content);
		btn_send = (Button) view.findViewById(R.id.btn_send);

		lv_select_contact = (ListView) view
				.findViewById(R.id.lv_select_contact);

		rl_message_detail_content = (RelativeLayout) view
				.findViewById(R.id.rl_message_detail_content);
		btn_detail_return = (Button) view.findViewById(R.id.btn_detail_return);
		tv_addressee_name = (TextView) view
				.findViewById(R.id.tv_addressee_name);
		tv_addressee_number = (TextView) view
				.findViewById(R.id.tv_addressee_number);
		btn_other = (Button) view.findViewById(R.id.btn_other);
		tv_message_detail_content = (TextView) view
				.findViewById(R.id.tv_message_detail_content);

		lv_rubbish_message = (ListView) view
				.findViewById(R.id.lv_rubbish_message);

		btn_message_in.setOnClickListener(this);
		btn_message_out.setOnClickListener(this);
		btn_message_rubbish.setOnClickListener(this);

		btn_write_message.setOnClickListener(this);
		btn_return.setOnClickListener(this);
		btn_select_contact.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btn_detail_return.setOnClickListener(this);

		// initTestData();
		// 第一次进来，显示的是收件箱信息，所以先获得收件箱列表。
		if (GocsdkCallbackImp.hfpStatus > 0) {
			if (inMessages != null) {
				inMessages.clear();
			}
			try {
				MainActivity.getService().getMessageInboxList();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(activity, "请您先连接设备", Toast.LENGTH_SHORT).show();
		}
		inAdapter = new MyInAdapter();
		outAdapter = new MyOutAdapter();
		delAdapter = new MyDelAdapter();

		lv_message_in.setAdapter(inAdapter);
		lv_message_in.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MessageListEvent messageInfo = inAdapter.getItem(position);
				showDetailMessage(messageInfo);
			}
		});

		lv_message_content.setAdapter(outAdapter);
		lv_message_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MessageListEvent messageInfo = outAdapter.getItem(position);
				showDetailMessage(messageInfo);
			}
		});

		lv_rubbish_message.setAdapter(delAdapter);
		lv_rubbish_message
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						MessageListEvent messageInfo = delAdapter
								.getItem(position);
						showDetailMessage(messageInfo);
					}
				});
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	// 接收短信具体内容方法
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(MessageTextEvent content) {
		/*
		 * if (!status.playing) { iv_pause.setVisibility(View.GONE);
		 * iv_play.setVisibility(View.VISIBLE); } else {
		 * iv_pause.setVisibility(View.VISIBLE);
		 * iv_play.setVisibility(View.GONE); }
		 */
		message_content = content.text;
	}

	// 接收短信信息方法
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(MessageListEvent info) {
		switch (pager) {
		case 0:
			inMessages.add(info);
			break;
		case 1:
			outMessages.add(info);
			break;
		case 2:
			delMessages.add(info);
			break;
		}
	}

	protected void showDetailMessage(MessageListEvent messageInfo) {
		try {
			MainActivity.getService().getMessageText(messageInfo.handle);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		lv_message_in.setVisibility(View.GONE);
		rl_message_out.setVisibility(View.GONE);
		rl_write_message.setVisibility(View.GONE);
		lv_select_contact.setVisibility(View.GONE);
		rl_message_detail_content.setVisibility(View.VISIBLE);
		lv_rubbish_message.setVisibility(View.GONE);

		tv_addressee_name.setText(messageInfo.name);
		tv_addressee_number.setText(messageInfo.num);
		tv_message_detail_content.setText(message_content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_message_in:
			if (GocsdkCallbackImp.hfpStatus > 0) {
				// 切换页面时，获取列表数据，先清理列表，避免重复加载数据
				if (inMessages != null) {
					inMessages.clear();
				}
				try {
					MainActivity.getService().getMessageInboxList();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(activity, "请您先连接设备", Toast.LENGTH_SHORT).show();
			}

			showInMessage();
			break;
		case R.id.btn_message_out:
			if (GocsdkCallbackImp.hfpStatus > 0) {
				if (outMessages != null) {
					outMessages.clear();
				}
				try {
					MainActivity.getService().getMessageSentList();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(activity, "请您先连接设备", Toast.LENGTH_SHORT).show();
			}
			showOutMessage();
			break;
		case R.id.btn_message_rubbish:
			if (GocsdkCallbackImp.hfpStatus > 0) {
				if (delMessages != null) {
					delMessages.clear();
				}
				try {
					MainActivity.getService().getMessageDeletedList();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(activity, "请您先连接设备", Toast.LENGTH_SHORT).show();
			}
			showRubbishMessage();
			break;
		case R.id.btn_write_message:
			showWriteMessage();
			break;
		case R.id.btn_return:
			showOutMessage();
			break;
		case R.id.btn_select_contact:
			showContactList();
			break;
		case R.id.btn_send:
			break;
		case R.id.btn_detail_return:
			switch (pager) {
			case 0:
				showInMessage();
				break;
			case 1:
				showOutMessage();
				break;
			case 2:
				showRubbishMessage();
				break;
			}
			break;
		}
	}

	private void showContactList() {
		lv_message_in.setVisibility(View.GONE);
		rl_message_out.setVisibility(View.GONE);
		rl_write_message.setVisibility(View.GONE);
		lv_select_contact.setVisibility(View.VISIBLE);
		rl_message_detail_content.setVisibility(View.GONE);
		lv_rubbish_message.setVisibility(View.GONE);
	}

	private void showWriteMessage() {
		lv_message_in.setVisibility(View.GONE);
		rl_message_out.setVisibility(View.GONE);
		rl_write_message.setVisibility(View.VISIBLE);
		lv_select_contact.setVisibility(View.GONE);
		rl_message_detail_content.setVisibility(View.GONE);
		lv_rubbish_message.setVisibility(View.GONE);
	}

	private void showRubbishMessage() {
		pager = 2;
		lv_rubbish_message.setVisibility(View.VISIBLE);
		rl_message_out.setVisibility(View.GONE);
		rl_write_message.setVisibility(View.GONE);
		lv_select_contact.setVisibility(View.GONE);
		rl_message_detail_content.setVisibility(View.GONE);
		lv_message_in.setVisibility(View.GONE);
	}

	private void showOutMessage() {
		pager = 1;
		lv_message_in.setVisibility(View.GONE);
		rl_message_out.setVisibility(View.VISIBLE);
		rl_write_message.setVisibility(View.GONE);
		lv_select_contact.setVisibility(View.GONE);
		rl_message_detail_content.setVisibility(View.GONE);
		lv_rubbish_message.setVisibility(View.GONE);
	}

	private void showInMessage() {
		pager = 0;
		lv_rubbish_message.setVisibility(View.GONE);
		rl_message_out.setVisibility(View.GONE);
		rl_write_message.setVisibility(View.GONE);
		lv_select_contact.setVisibility(View.GONE);
		rl_message_detail_content.setVisibility(View.GONE);
		lv_message_in.setVisibility(View.VISIBLE);
	}

	public class MyInAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return inMessages.size();
		}

		@Override
		public MessageListEvent getItem(int position) {
			return inMessages.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(activity,
						R.layout.message_item_layout, null);
				holder.tv_addressee = (TextView) convertView
						.findViewById(R.id.tv_addressee);
				holder.tv_sendOrReceiverTime = (TextView) convertView
						.findViewById(R.id.tv_sendorreceiver_time);
				holder.tv_message_content = (TextView) convertView
						.findViewById(R.id.tv_message_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			MessageListEvent messageInfo = inMessages.get(position);
			holder.tv_addressee.setText(messageInfo.name);
			holder.tv_sendOrReceiverTime.setText(messageInfo.time);
			holder.tv_message_content.setText(messageInfo.title);
			return convertView;
		}

		public class ViewHolder {
			public TextView tv_addressee;
			public TextView tv_sendOrReceiverTime;
			public TextView tv_message_content;
		}
	}

	public class MyOutAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return outMessages.size();
		}

		@Override
		public MessageListEvent getItem(int position) {
			return outMessages.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(activity,
						R.layout.message_item_layout, null);
				holder.tv_addressee = (TextView) convertView
						.findViewById(R.id.tv_addressee);
				holder.tv_sendOrReceiverTime = (TextView) convertView
						.findViewById(R.id.tv_sendorreceiver_time);
				holder.tv_message_content = (TextView) convertView
						.findViewById(R.id.tv_message_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			MessageListEvent messageInfo = outMessages.get(position);
			holder.tv_addressee.setText(messageInfo.name);
			holder.tv_sendOrReceiverTime.setText(messageInfo.time);
			holder.tv_message_content.setText(messageInfo.title);
			return convertView;
		}

		public class ViewHolder {
			public TextView tv_addressee;
			public TextView tv_sendOrReceiverTime;
			public TextView tv_message_content;
		}
	}

	public class MyDelAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return delMessages.size();
		}

		@Override
		public MessageListEvent getItem(int position) {
			return delMessages.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(activity,
						R.layout.message_item_layout, null);
				holder.tv_addressee = (TextView) convertView
						.findViewById(R.id.tv_addressee);
				holder.tv_sendOrReceiverTime = (TextView) convertView
						.findViewById(R.id.tv_sendorreceiver_time);
				holder.tv_message_content = (TextView) convertView
						.findViewById(R.id.tv_message_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			MessageListEvent messageInfo = delMessages.get(position);
			holder.tv_addressee.setText(messageInfo.name);
			holder.tv_sendOrReceiverTime.setText(messageInfo.time);
			holder.tv_message_content.setText(messageInfo.title);
			return convertView;
		}

		public class ViewHolder {
			public TextView tv_addressee;
			public TextView tv_sendOrReceiverTime;
			public TextView tv_message_content;
		}
	}
}
