package com.goodocom.gocsdk.activity;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodocom.gocsdk.HfpStatus;
import com.goodocom.gocsdk.R;
import com.goodocom.gocsdk.db.GocDatabase;
import com.goodocom.gocsdk.event.CurrentNumberEvent;
import com.goodocom.gocsdk.event.HfpStatusEvent;
import com.goodocom.gocsdk.service.GocsdkCallbackImp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CallActivity extends Activity implements OnClickListener {
	public static boolean running = false;

	private RelativeLayout rl_call_page;
	private RelativeLayout rl_talking_page;
	private TextView tv_calling_name;
	private Chronometer chronometer;
	private TextView tv_talking_number;
	private LinearLayout ll_number;
	private ImageView iv_number;
	private ImageView iv_guaduan;
	private ImageView iv_qieshengdao;
	private ImageView iv_bujingyin;

    // flag
	private boolean isShowNumber = false;
	private boolean volume_flag = false;
	private boolean isMute = true;

	private String currentNumber = "";
	private int currentStatus = HfpStatus.IDLE;

	private TextView tv_calling_number;
	private TextView tv_talking_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call);
		initView();
		initData();

        EventBus.getDefault().register(this);

        running = true;
	}

	@Override
	protected void onDestroy() {
        EventBus.getDefault().unregister(this);
		super.onDestroy();

        running = false;
	}

	@Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
	public void onEvent(CurrentNumberEvent event){
        currentNumber = event.number;
        String name = GocDatabase.getDefault().getNameByNumber(currentNumber);
        if (TextUtils.isEmpty(name)) {
            name = "未知联系人";
        }

        tv_calling_name.setText(name);
        tv_talking_name.setText(name);
        tv_calling_number.setText(currentNumber);
        tv_talking_number.setText(currentNumber);

        EventBus.getDefault().removeStickyEvent(event);
	}

	@Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
	public void onEvent(HfpStatusEvent event){
		if(event.status <= HfpStatus.CONNECTED){
			currentNumber = "";
			finish();
        }else if(event.status == HfpStatus.CALLING){
            onCalling();
		}else if(event.status == HfpStatus.TALKING){
            onTalking();
		}

        EventBus.getDefault().removeStickyEvent(event);
	}

	private void onTalking(){
		rl_call_page.setVisibility(View.GONE);
		rl_talking_page.setVisibility(View.VISIBLE);

        if(!TextUtils.isEmpty(currentNumber)) {
            tv_talking_number.setText(currentNumber);

            String name = GocDatabase.getDefault().getNameByNumber(currentNumber);
            if (!TextUtils.isEmpty(name)) {
                tv_talking_name.setText(name);
            } else {
                tv_talking_name.setText("未知联系人");
            }
        }else{
            tv_talking_number.setText("");
            tv_talking_name.setText("未知联系人");
        }

		chronometer.setFormat("%s");
		chronometer.setBase(SystemClock.elapsedRealtime());// 复位键
		chronometer.start();
	}

	private void onCalling(){
        rl_call_page.setVisibility(View.VISIBLE);
        rl_talking_page.setVisibility(View.GONE);

        if(!TextUtils.isEmpty(currentNumber)){
            tv_calling_number.setText(currentNumber);
            String name = GocDatabase.getDefault().getNameByNumber(currentNumber);
            if(!TextUtils.isEmpty(name)){
                tv_calling_name.setText(name);
            }else{
                tv_calling_name.setText("未知联系人");
            }
        }else{
            tv_calling_number.setText("");
            tv_calling_name.setText("未知联系人");
        }
    }

	private void initData() {
		Intent intent = getIntent();
		currentNumber = intent.getStringExtra("number");
		Log.d("app","initData currentNumber:"+ currentNumber);
        currentStatus  = intent.getIntExtra("status",0);

        if(currentStatus <= HfpStatus.CONNECTED){
            finish();
        }else if(currentStatus == HfpStatus.CALLING){
            onCalling();
        }else if(currentStatus == HfpStatus.TALKING){
            onTalking();
        }
	}

	public static void start(Context context,int status, String number){
        if(running)return;
        running = true;

        Intent intent = new Intent(context,CallActivity.class);
        intent.putExtra("number",number);
        intent.putExtra("status",status);
        context.startActivity(intent);
    }


	private void initView() {
		rl_call_page = (RelativeLayout) findViewById(R.id.rl_call_page);
		tv_calling_name = (TextView) findViewById(R.id.tv_call_name);
		tv_calling_number = (TextView) findViewById(R.id.tv_call_number);

		rl_talking_page = (RelativeLayout) findViewById(R.id.rl_talking_page);
		chronometer = (Chronometer) findViewById(R.id.chronometer);
		tv_talking_number = (TextView) findViewById(R.id.tv_connection_number);
		tv_talking_name = (TextView) findViewById(R.id.tv_connection_name);

		ll_number = (LinearLayout) findViewById(R.id.ll_numpad);
		iv_number = (ImageView) findViewById(R.id.iv_number);
		iv_guaduan = (ImageView) findViewById(R.id.iv_guaduan);
		iv_qieshengdao = (ImageView) findViewById(R.id.iv_qieshengdao);
		iv_bujingyin = (ImageView) findViewById(R.id.iv_bujingyin);
		
		
		findViewById(R.id.iv_one).setOnClickListener(this);
		findViewById(R.id.iv_two).setOnClickListener(this);
		findViewById(R.id.iv_three).setOnClickListener(this);
		findViewById(R.id.iv_four).setOnClickListener(this);
		findViewById(R.id.iv_five).setOnClickListener(this);
		findViewById(R.id.iv_six).setOnClickListener(this);
		findViewById(R.id.iv_seven).setOnClickListener(this);
		findViewById(R.id.iv_eight).setOnClickListener(this);
		findViewById(R.id.iv_nine).setOnClickListener(this);
		findViewById(R.id.iv_xinghao).setOnClickListener(this);
		findViewById(R.id.iv_zero).setOnClickListener(this);
		findViewById(R.id.iv_jinghao).setOnClickListener(this);

		iv_number.setOnClickListener(this);
		iv_guaduan.setOnClickListener(this);
		iv_qieshengdao.setOnClickListener(this);
		iv_bujingyin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_number: 
			showNumber();
			break;
		case R.id.iv_guaduan:
			hangUp();
			break;
		case R.id.iv_qieshengdao:
			if (GocsdkCallbackImp.hfpStatus >= 3) {
				switchCarAndphone();
			} else {
				Toast.makeText(this, "请您先连接设备", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.iv_bujingyin:
			switchMute();
			break;
		case R.id.iv_one:
			phoneDTMFCode('1');
			break;
		case R.id.iv_two:
			phoneDTMFCode('2');
			break;
		case R.id.iv_three:
			phoneDTMFCode('3');
			break;
		case R.id.iv_four:
			phoneDTMFCode('4');
			break;
		case R.id.iv_five:
			phoneDTMFCode('5');
			break;
		case R.id.iv_six:
			phoneDTMFCode('6');
			break;
		case R.id.iv_seven:
			phoneDTMFCode('7');
			break;
		case R.id.iv_eight:
			phoneDTMFCode('8');
			break;
		case R.id.iv_nine:
			phoneDTMFCode('9');
			break;
		case R.id.iv_xinghao:
			phoneDTMFCode('*');
			break;
		case R.id.iv_zero:
			phoneDTMFCode('0');
			break;
		case R.id.iv_jinghao:
			phoneDTMFCode('#');
			break;
		}
	}
	private void phoneDTMFCode(char code){
		try {
			MainActivity.getService().phoneTransmitDTMFCode(code);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	private void switchMute() {
		Handler handler = MainActivity.getHandler();
		isMute = !isMute;
		if(isMute){
			handler.sendEmptyMessage(MainActivity.MSG_SET_MICPHONE_OFF);
			iv_bujingyin.setImageResource(R.drawable.btn_jianpan_bujingyin_selector);
			try {
				MainActivity.getService().muteOpenAndClose(1);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}else{
			handler.sendEmptyMessage(MainActivity.MSG_SET_MICPHONE_ON);
			iv_bujingyin.setImageResource(R.drawable.btn_jianpan_jingyin_selector);
			try {
				MainActivity.getService().muteOpenAndClose(0);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
	}
	// 切换声音在车机端与手机端
		private void switchCarAndphone() {
			volume_flag = !volume_flag;
			if (volume_flag) {// 手机端
				try {
					MainActivity.getService().phoneTransfer();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				Toast.makeText(this, "手机端", Toast.LENGTH_SHORT).show();
			} else {// 车机端
				try {
					MainActivity.getService().phoneTransferBack();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				Toast.makeText(this, "车机端", Toast.LENGTH_SHORT).show();
			}
		}
	private void showNumber() {
		isShowNumber = !isShowNumber;
		if (isShowNumber) {
			ll_number.setVisibility(View.VISIBLE);
		} else {
			ll_number.setVisibility(View.GONE);
		}
	}

	// 挂断
	private void hangUp() {
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
