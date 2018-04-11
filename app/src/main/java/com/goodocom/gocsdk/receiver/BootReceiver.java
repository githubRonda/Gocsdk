package com.goodocom.gocsdk.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.goodocom.gocsdk.Config;
import com.goodocom.gocsdk.service.GocsdkService;
import com.goodocom.gocsdk.service.PlayerService;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(Config.JAVA_SDK) {
			Intent service = new Intent(context, GocsdkService.class);
			context.startService(service);
		}

		if(Config.JAVA_PLAYER) {
			Intent playService = new Intent(context, PlayerService.class);
			context.startService(playService);
		}
	}
}
