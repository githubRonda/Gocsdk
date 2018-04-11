package com.goodocom.gocsdk.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodocom.gocsdk.HfpStatus;
import com.goodocom.gocsdk.R;
import com.goodocom.gocsdk.db.GocDatabase;
import com.goodocom.gocsdk.event.CurrentNumberEvent;
import com.goodocom.gocsdk.event.HfpStatusEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class IncomingActivity extends Activity implements OnClickListener {
	public static boolean running = false;

	private String currentNumber = "";

	private ImageView iv_connect;
	private ImageView iv_hangup;
	private TextView tv_incoming_number;
	private TextView tv_incoming_name;

	public void updateNumber(){
		if(!TextUtils.isEmpty(currentNumber)){
			tv_incoming_number.setText(currentNumber);
			String name = GocDatabase.getDefault().getNameByNumber(currentNumber);

			if(!TextUtils.isEmpty(name)){
				tv_incoming_name.setText(name);
			}else{
				tv_incoming_name.setText("未知联系人");
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incoming);
		Log.d("app", "IncomingActivity onCreate");
		Intent intent = getIntent();
		currentNumber = intent.getStringExtra("number");
		tv_incoming_name = (TextView) findViewById(R.id.tv_incoming_name);
		tv_incoming_number = (TextView)findViewById(R.id.tv_incoming_number);
		iv_connect = (ImageView) findViewById(R.id.iv_connect);
		iv_hangup = (ImageView) findViewById(R.id.iv_hangup);
		iv_connect.setOnClickListener(this);
		iv_hangup.setOnClickListener(this);

		updateNumber();

		EventBus.getDefault().register(this);

		running = true;
	}

	public static void start(Context context,int status,String number){
		if(running)return;
		running = false;

		Intent intent = new Intent(context,IncomingActivity.class);
		intent.putExtra("number",number);
		context.startActivity(intent);
	}


	@Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
	public void onEvent(HfpStatusEvent event){
		if(event.status <= HfpStatus.CONNECTED){
			finish();
		}else if(event.status == HfpStatus.CALLING){
			finish();
		}else if(event.status == HfpStatus.INCOMING){

		}else if(event.status == HfpStatus.TALKING){
			this.finish();
			CallActivity.start(this,HfpStatus.TALKING, currentNumber);
		}

		EventBus.getDefault().removeStickyEvent(event);
	}

	@Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
	public void onEvent(CurrentNumberEvent event){
		currentNumber = event.number;
		updateNumber();

		EventBus.getDefault().removeStickyEvent(event);
	}

	@Override
	protected void onDestroy() {
		Log.d("app","IncomingActivity onDestroy!");
        EventBus.getDefault().unregister(this);
		super.onDestroy();

		running = false;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_connect:
			try {
				MainActivity.getService().phoneAnswer();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		case R.id.iv_hangup:
			hangupInComing();
			break;
		}
	}

	private void hangupInComing() {
		try {
			MainActivity.getService().phoneHangUp();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
	}
}
