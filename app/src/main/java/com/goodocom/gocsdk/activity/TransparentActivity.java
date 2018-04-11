package com.goodocom.gocsdk.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.goodocom.gocsdk.HfpStatus;
import com.goodocom.gocsdk.IGocsdkService;
import com.goodocom.gocsdk.R;
import com.goodocom.gocsdk.db.GocDatabase;
import com.goodocom.gocsdk.event.BackgroundCurrentNumberEvent;
import com.goodocom.gocsdk.event.BackgroundHfpStatusEvent;
import com.goodocom.gocsdk.service.GocsdkService;
import com.goodocom.gocsdk.view.TransparentCallDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TransparentActivity extends Activity implements OnClickListener {
	public static final int MSG_HANGUP_PHONE = 0;
	public static final int MSG_CONNECTION_PHONE = 1;
	public static final int MSG_BLUETOOTH_DISCONNECTED = 2;
	public static final int MSG_HFP_STATUS = 3;

	private IGocsdkService iGocsdkService;
	private Intent gocsdkService;
	private MyConn conn;

	private TextView tv_name;
	private TextView tv_number;
	private TextView tv_status;
	private ImageButton ibt_accept;
	private ImageButton ibt_reject;
	private TransparentCallDialog dialog;
	private Chronometer chronometer;

	private String currentNumber;

	private int currentStatus;
	// 声音在车机端或手机端的标记，车机端:false,手机端：true
	private boolean volume_flag = false;

    public static volatile boolean running = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notallhas);
		Intent intent = getIntent();
		currentStatus = intent.getIntExtra("status",0);
		currentNumber = intent.getStringExtra("number");

        createDialog();

		gocsdkService = new Intent(TransparentActivity.this, GocsdkService.class);
		conn = new MyConn();
		bindService(gocsdkService, conn, BIND_AUTO_CREATE);

		EventBus.getDefault().register(this);

        running = true;
	}

	public static void start(Context context, int status, String number){
        if(running)return;
        running = true;

        Intent intent = new Intent(context,TransparentActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("number", number);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intent);
    }

	@Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
	public void onEvent(BackgroundHfpStatusEvent event){
        if(event.status <= HfpStatus.CONNECTED){
            chronometer.stop();
            chronometer.setVisibility(View.GONE);
            dialog.dismiss();
            finish();
        }else if(event.status == HfpStatus.TALKING){
            ibt_accept.setImageResource(R.drawable.btn_little_qieshengdao_selector);
            tv_status.setText("正在通话：");
            chronometer.setVisibility(View.VISIBLE);
            chronometer.setFormat("%s");
            chronometer.setBase(SystemClock.elapsedRealtime());// 复位键
            chronometer.start();
        }

        EventBus.getDefault().removeStickyEvent(event);
	}

	@Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
	public void onEvent(BackgroundCurrentNumberEvent event){
        currentNumber = event.number;
        updateNumber();

		EventBus.getDefault().removeStickyEvent(event);
	}

	@Override
	protected void onDestroy() {
		unbindService(conn);
        EventBus.getDefault().unregister(this);

        running = false;

		super.onDestroy();
	}

	private void createDialog() {
		dialog = new TransparentCallDialog(this, R.style.transparentdialog);
        dialog.setCanceledOnTouchOutside(false);
		initDialogView();
		initDialogData();
        dialog.show();
	}

	private void updateNumber(){
        if (!TextUtils.isEmpty(currentNumber)) {
            String name = GocDatabase.getDefault().getNameByNumber(currentNumber);

            if (TextUtils.isEmpty(name)) {
                tv_name.setText("未知联系人");
            } else {
                tv_name.setText(name);
            }
            tv_number.setText(currentNumber);
        }
    }

	private void initDialogData() {
        if(currentStatus == HfpStatus.CALLING) {
            tv_status.setText("去电...");
        }else if(currentStatus == HfpStatus.INCOMING){
            tv_status.setText("来电...");
        }else if(currentStatus == HfpStatus.TALKING){
            ibt_accept.setImageResource(R.drawable.btn_little_qieshengdao_selector);
            tv_status.setText("正在通话：");
            chronometer.setVisibility(View.VISIBLE);
            chronometer.setFormat("%s");
            chronometer.setBase(SystemClock.elapsedRealtime());// 复位键
            chronometer.start();
        }

        updateNumber();
	}

	private void initDialogView() {
		View customView = dialog.getCustomView();
		tv_name = (TextView) customView.findViewById(R.id.incoming_name);
		tv_number = (TextView) customView.findViewById(R.id.incoming_number);
		tv_status = (TextView) customView.findViewById(R.id.tv_phone_incoming);
		ibt_accept = (ImageButton) customView.findViewById(R.id.ibt_accept_or_switch);
		ibt_reject = (ImageButton) customView.findViewById(R.id.ibt_reject);
        chronometer = (Chronometer) customView.findViewById(R.id.chronometer_incoming);
		ibt_accept.setOnClickListener(this);
		ibt_reject.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibt_accept_or_switch:
		    if(currentStatus == 6){
                switchCarAndphone();
            }else {
                try {
                    iGocsdkService.phoneAnswer();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
			break;
		case R.id.ibt_reject:
			try {
				iGocsdkService.phoneHangUp();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private void switchCarAndphone() {
		volume_flag = !volume_flag;
		if (volume_flag) {// 手机端
			try {
				iGocsdkService.phoneTransfer();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			Toast.makeText(this, "手机端", Toast.LENGTH_SHORT).show();
		} else {// 车机端
			try {
				iGocsdkService.phoneTransferBack();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			Toast.makeText(this, "车机端", Toast.LENGTH_SHORT).show();
		}
	}

	private class MyConn implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
            iGocsdkService = IGocsdkService.Stub.asInterface(service);
            try {
                iGocsdkService.inqueryHfpStatus();
                iGocsdkService.getLocalName();
                iGocsdkService.getPinCode();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	}

    @Override
    public void onBackPressed() {
    }
}
