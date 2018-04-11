package com.goodocom.gocsdk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.goodocom.gocsdk.R;

public class TransActivity extends Activity {
	public static final int MSG_FINISH = 0;
	
	private static Handler  hand;
	public static Handler getHandler(){
		return hand;
	}
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_FINISH:
				finish();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trans);
		hand = handler;
	}
}
