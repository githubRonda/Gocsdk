package com.goodocom.gocsdk.service;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.goodocom.gocsdk.HfpStatus;
import com.goodocom.gocsdk.IGocsdkCallback;
import com.goodocom.gocsdk.activity.MainActivity;
import com.goodocom.gocsdk.domain.BlueToothInfo;
import com.goodocom.gocsdk.domain.BlueToothPairedInfo;
import com.goodocom.gocsdk.domain.CallLogInfo;
import com.goodocom.gocsdk.event.A2dpStatusEvent;
import com.goodocom.gocsdk.event.CurrentNumberEvent;
import com.goodocom.gocsdk.event.HfpStatusEvent;
import com.goodocom.gocsdk.event.MusicInfoEvent;
import com.goodocom.gocsdk.event.MusicPosEvent;
import com.goodocom.gocsdk.event.PlayStatusEvent;
import com.goodocom.gocsdk.fragment.FragmentCallog;
import com.goodocom.gocsdk.fragment.FragmentPairedList;
import com.goodocom.gocsdk.fragment.FragmentPhonebookList;
import com.goodocom.gocsdk.fragment.FragmentPhonebookList.Phonebook;
import com.goodocom.gocsdk.fragment.FragmentSearch;
import com.goodocom.gocsdk.fragment.FragmentSetting;

import org.greenrobot.eventbus.EventBus;

public class GocsdkCallbackImp extends IGocsdkCallback.Stub {
	public static String number = "";
	public static int hfpStatus = 1;
	public static int a2dpStatus = 1;


	@Override
	public void onHfpConnected() throws RemoteException {
		Handler handler2 = FragmentPairedList.getHandler();
		if(handler2!=null){
			handler2.sendEmptyMessage(FragmentPairedList.MSG_CONNECT_SUCCESS);
		}
		Handler handler = FragmentSearch.getHandler();
		if(handler != null){
			handler.sendEmptyMessage(FragmentSearch.MSG_CONNECT_SUCCESS);
		}
		Handler handler3 = FragmentPhonebookList.getHandler();
		if(handler3 != null){
			handler3.sendEmptyMessage(FragmentPhonebookList.MSG_DEVICE_CONNECT);
		}
		Handler handler4 = FragmentCallog.getHandler();
		if(handler4!=null){
			handler4.sendEmptyMessage(FragmentCallog.MSG_DEVICE_CONNECTED);
		}
		GocsdkCallbackImp.hfpStatus = 3;
	}

	@Override
	public void onHfpDisconnected() throws RemoteException {
		Handler handler = FragmentSearch.getHandler();
		if(handler != null){
			handler.sendEmptyMessage(FragmentSearch.MSG_CONNECT_FAILE);
		}
		Handler handler1 = FragmentCallog.getHandler();
		if(handler1!=null){
			handler1.sendEmptyMessage(FragmentCallog.MSG_DEVICE_DISCONNECTED);
		}
		Handler handler2 = FragmentPairedList.getHandler();
		if(handler2!=null){
			handler2.sendEmptyMessage(FragmentPairedList.MSG_CONNECT_FAILE);
		}
		Handler handler3 = FragmentPhonebookList.getHandler();
		if(handler3!=null){
			handler3.sendEmptyMessage(FragmentPhonebookList.MSG_CONNECT_FAILE);
		}
		GocsdkCallbackImp.hfpStatus = 1;
	}

	@Override
	public void onCallSucceed(String number) throws RemoteException {
		Log.d("app", "GocsdkCallbackImp onCallSucceed"+number);
		Handler handler = MainActivity.getHandler();
		if(handler!=null){
			handler.sendMessage(handler.obtainMessage(MainActivity.MSG_OUTGING,number));
		}
		GocsdkCallbackImp.hfpStatus = 4;
	}

	@Override
	public void onIncoming(String number) throws RemoteException {
		
		Handler handler1 = MainActivity.getHandler();
		handler1.sendMessage(handler1.obtainMessage(MainActivity.MSG_COMING,
				number));
		GocsdkCallbackImp.number = number;
		GocsdkCallbackImp.hfpStatus = 5;

	}

	@Override
	public void onHangUp() throws RemoteException {
		Handler handler1 = MainActivity.getHandler();
		if(handler1!=null){
			handler1.sendEmptyMessage(MainActivity.MSG_HANGUP);
		}
	}

	@Override
	public void onTalking(String str) throws RemoteException {
		Handler handler = MainActivity.getHandler();
		if(handler==null){
			return;
		}
		handler.sendMessage(handler.obtainMessage(MainActivity.MSG_TALKING,str));
		GocsdkCallbackImp.hfpStatus = 6;
	}

	@Override
	public void onRingStart() throws RemoteException {
	}

	@Override
	public void onRingStop() throws RemoteException {
	}

	@Override
	public void onHfpLocal() throws RemoteException {
	}

	@Override
	public void onHfpRemote() throws RemoteException {
	}

	@Override
	public void onInPairMode() throws RemoteException {
	}

	@Override
	public void onExitPairMode() throws RemoteException {
	}

	@Override
	public void onInitSucceed() throws RemoteException {
		
	}

	@Override
	public void onMusicPlaying() throws RemoteException {
		Log.d("app", "callback play status event true");
		EventBus.getDefault().postSticky(new PlayStatusEvent(true));
	}

	@Override
	public void onMusicStopped() throws RemoteException {
		Log.d("app", "callback play status event false");
		EventBus.getDefault().postSticky(new PlayStatusEvent(false));
	}

	@Override
	public void onAutoConnectAccept(String autoStatus)
			throws RemoteException {
		Handler handler = FragmentSetting.getHandler();
		if(handler == null){
			return;
		}
		Message msg = Message.obtain();
		msg.what = FragmentSetting.MSG_AUTO_STATUS;
		msg.obj = autoStatus;
		handler.sendMessage(msg);
	}

	@Override
	public void onCurrentAddr(String addr) throws RemoteException {
		Handler handler2 = FragmentPairedList.getHandler();
		if(handler2!=null){
			Message msg = Message.obtain();
			msg.what = FragmentPairedList.MSG_CONNECT_ADDRESS;
			msg.obj = addr;
			handler2.sendMessage(msg);
		}
	}

	@Override
	public void onCurrentName(String name) throws RemoteException {
		Handler handler = MainActivity.getHandler();
		
		if(handler!=null){
			Message msg = Message.obtain();
			msg.what = MainActivity.MSG_CURRENT_CONNECT_DEVICE_NAME;
			msg.obj = name;
			handler.sendMessage(msg);
		}
	}

	// 1:未连接 3:已连接 4：电话拨出 5：电话打入 6：通话中
	/*
	 * 0~初始化 1~待机状态 2~连接中 3~连接成功 4~电话拨出 5~电话打入 6~通话中
	 */
	@Override
	public void onHfpStatus(int status) throws RemoteException {
        int prevStatus = GocsdkCallbackImp.hfpStatus;

		GocsdkCallbackImp.hfpStatus = status;

		Handler handler = FragmentSearch.getHandler();
		if(handler != null){
			Message msg = Message.obtain();
			msg.what = FragmentSearch.MSG_HFP_STATUS;
			msg.obj = status;
			handler.sendMessage(msg);
		}
		Handler handler2 = FragmentPairedList.getHandler();
		if(handler2 != null){
			Message msg = Message.obtain();
			msg.what = FragmentPairedList.MSG_HFP_STATUS;
			msg.obj = status;
			handler2.sendMessage(msg);
		}

		if(prevStatus <= HfpStatus.CONNECTED) {
            Handler mainHandler = MainActivity.getHandler();
            if (mainHandler != null) {
                if (status == HfpStatus.CALLING) {
                    mainHandler.sendMessage(handler.obtainMessage(MainActivity.MSG_OUTGING, ""));
                } else if (status == HfpStatus.INCOMING){
                    mainHandler.sendMessage(handler.obtainMessage(MainActivity.MSG_COMING, ""));
                }else if(status == HfpStatus.TALKING){
                    mainHandler.sendMessage(handler.obtainMessage(MainActivity.MSG_TALKING, ""));
                }
            }
        }

		EventBus.getDefault().postSticky(new HfpStatusEvent(status));
	}

	@Override
	public void onAvStatus(int status) throws RemoteException {
		a2dpStatus = status;

		EventBus.getDefault().post(new A2dpStatusEvent(status));
	}

	@Override
	public void onVersionDate(String version) throws RemoteException {
	}

	@Override
	public void onCurrentDeviceName(String name) throws RemoteException {
		Handler handler = MainActivity.getHandler();
		if(handler!=null){
			Message msg = Message.obtain();
			msg.obj = name;
			msg.what = MainActivity.MSG_DEVICENAME;
			handler.sendMessage(msg);
		}
		Handler handler1 = FragmentSetting.getHandler();
		if(handler1!=null){
			Message msg = Message.obtain();
			msg.what = FragmentSetting.MSG_DEVICE_NAME;
			msg.obj = name;
			handler1.sendMessage(msg);
		}
		Handler handler2 = FragmentSearch.getHandler();
		if(handler2!=null){
			Message msg = Message.obtain();
			msg.what = FragmentSearch.MSG_DEVICE_NAME;
			msg.obj = name;
			handler2.sendMessage(msg);
		}
	}

	@Override
	public void onCurrentPinCode(String code) throws RemoteException {
		Handler handler = MainActivity.getHandler();
		
		if(handler!=null){
			Message msg = Message.obtain();
			msg.obj = code;
			msg.what = MainActivity.MSG_DEVICEPINCODE;
			handler.sendMessage(msg);
		}
//		Handler handler1 = FragmentSetting.getHandler();
//		if(handler1!=null){
//			Message msg = Message.obtain();
//			msg.what = FragmentSetting.MSG_PIN_CODE;
//			msg.obj = code;
//			handler1.sendMessage(msg);
//		}
//		Handler handler2 = FragmentSearch.getHandler();
//		if(handler2!=null){
//			Message msg = Message.obtain();
//			msg.what = FragmentSearch.MSG_PIN_CODE;
//			msg.obj = code;
//			handler2.sendMessage(msg);
//		}
	}

	@Override
	public void onA2dpConnected() throws RemoteException {
	}
	//配对列表
	@Override
	public void onCurrentAndPairList(int index, String name, String addr)
			throws RemoteException {
		Handler handler = FragmentPairedList.getHandler();
		if(handler == null){
			return;
		}
		BlueToothPairedInfo info = new BlueToothPairedInfo();
		info.index = index;
		info.name = name;
		info.address = addr;
		Message msg = Message.obtain();
		msg.obj = info;
		msg.what = FragmentPairedList.MSG_PAIRED_DEVICE;
		handler.sendMessage(msg);
	}

	@Override
	public void onA2dpDisconnected() throws RemoteException {
	}

	@Override
	public void onPhoneBook(String name, String number) throws RemoteException {
		Handler handler = FragmentPhonebookList.getHandler();
		if (handler == null) {
			return;
		}
		Message msg = Message.obtain();
		msg.what = FragmentPhonebookList.MSG_PHONE_BOOK;
		Phonebook phonebook = new Phonebook();
		phonebook.name = name;
		phonebook.num = number;
		msg.obj = phonebook;
		handler.sendMessage(msg);
	}

	@Override
	public void onPhoneBookDone() throws RemoteException {
		Handler mainActivityHandler = MainActivity.getHandler();
		if (mainActivityHandler == null)
			return;
		mainActivityHandler
				.sendEmptyMessage(MainActivity.MSG_UPDATE_PHONEBOOK_DONE);

		Handler handler = FragmentPhonebookList.getHandler();
		if (handler == null) {
			return;
		}
		Message msg = Message.obtain();
		msg.what = FragmentPhonebookList.MSG_PHONE_BOOK_DONE;
		handler.sendMessage(msg);
	}

	@Override
	public void onSimBook(String name, String number) throws RemoteException {

	}

	@Override
	public void onSimDone() throws RemoteException {

	}

	@Override
	public void onCalllog(int type, String name, String number)
			throws RemoteException {
		Handler handler = FragmentCallog.getHandler();
		if (handler == null) {
			return;
		}
		CallLogInfo info = new CallLogInfo();
		info.number = number;
		info.type = type;
		info.name = name;
		Message msg = Message.obtain();
		msg.obj = info;
		msg.what = FragmentCallog.MSG_CALLLOG;
		handler.sendMessage(msg);
	}

	@Override
	public void onCalllogDone() throws RemoteException {
		Handler mainHandler = MainActivity.getHandler();
		mainHandler.sendEmptyMessage(MainActivity.MSG_UPDATE_CALLLOG_DONE);
		
		Handler handler = FragmentCallog.getHandler();
		if (handler == null) {
			return;
		}
		Message msg = Message.obtain();
		msg.what = FragmentCallog.MSG_CALLLOG_DONE;
		handler.sendMessage(msg);
	}

	@Override
	public void onDiscovery(String type,String name, String addr) throws RemoteException {
		Handler handler = FragmentSearch.getHandler();
		Message msg = Message.obtain();
		msg.what = FragmentSearch.MSG_SEARCHE_DEVICE;
		BlueToothInfo info = new BlueToothInfo();
		info.name = name;
		info.address = addr;
		msg.obj = info;
		if (handler == null) {
			return;
		}
		handler.sendMessage(msg);
	}

	@Override
	public void onDiscoveryDone() throws RemoteException {
		Handler handler = FragmentSearch.getHandler();
		if (handler == null) {
			return;
		}
		handler.sendEmptyMessage(FragmentSearch.MSG_SEARCHE_DEVICE_DONE);
	}

	@Override
	public void onLocalAddress(String addr) throws RemoteException {
	}

	//得到拨出或者通话中的号码
	@Override
	public void onOutGoingOrTalkingNumber(String number) throws RemoteException {
		EventBus.getDefault().postSticky(new CurrentNumberEvent(number));
	}

	@Override
	public void onConnecting() throws RemoteException {
	}

	@Override
	public void onSppData(int index, String data) throws RemoteException {
	}

	@Override
	public void onSppConnect(int index) throws RemoteException {
	}

	@Override
	public void onSppDisconnect(int index) throws RemoteException {
	}

	@Override
	public void onSppStatus(int status) throws RemoteException {
	}

	@Override
	public void onOppReceivedFile(String path) throws RemoteException {
	}

	@Override
	public void onOppPushSuccess() throws RemoteException {
	}

	@Override
	public void onOppPushFailed() throws RemoteException {
	}

	@Override
	public void onHidConnected() throws RemoteException {
	}

	@Override
	public void onHidDisconnected() throws RemoteException {
	}

	@Override
	public void onHidStatus(int status) throws RemoteException {
	}

	@Override
	public void onMusicInfo(String name, String artist,String album,int duration, int pos,
			int total) throws RemoteException {
		EventBus.getDefault().post(
				new MusicInfoEvent(name, artist,album,duration, pos, total));
	}

	@Override
	public void onMusicPos(int current, int total) throws RemoteException {
		EventBus.getDefault().post(new MusicPosEvent(current,total));
	}

	@Override
	public void onPanConnect() throws RemoteException {
	}

	@Override
	public void onPanDisconnect() throws RemoteException {
	}

	@Override
	public void onPanStatus(int status) throws RemoteException {

	}

	@Override
	public void onVoiceConnected() throws RemoteException {
	}

	@Override
	public void onVoiceDisconnected() throws RemoteException {

	}

	@Override
	public void onProfileEnbled(boolean[] enabled) throws RemoteException {
	}

	@Override
	public void onMessageInfo(String content_order, String read_status,
			String time, String name, String num, String title)
			throws RemoteException {/*
		EventBus.getDefault().post(
				new MessageListEvent(content_order,
						read_status.equals("1") ? true : false, time, name,
						num, title));
	*/}

	@Override
	public void onMessageContent(String content) throws RemoteException {
		//EventBus.getDefault().post(new MessageTextEvent(content));
	}

	@Override
	public void onPairedState(int state) throws RemoteException {
		Log.d("app",""+state);
	}

}
