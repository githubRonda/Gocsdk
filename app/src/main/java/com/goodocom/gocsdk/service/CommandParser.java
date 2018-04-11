package com.goodocom.gocsdk.service;

import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.goodocom.gocsdk.Commands;
import com.goodocom.gocsdk.IGocsdkCallback;
import com.goodocom.gocsdk.activity.TransparentActivity;
import com.goodocom.gocsdk.event.BackgroundCurrentNumberEvent;
import com.goodocom.gocsdk.event.BackgroundHfpStatusEvent;

import org.greenrobot.eventbus.EventBus;

public class CommandParser {
	private static final String TAG = CommandParser.class.getName();

	private RemoteCallbackList<IGocsdkCallback> callbacks;
	private Context mContext;

	public CommandParser(RemoteCallbackList<IGocsdkCallback> callbacks, GocsdkService gocsdkService) {
		this.callbacks = callbacks;
		mContext = gocsdkService;
	}

	private byte[] serialBuffer = new byte[1024];
	private int count = 0;

	private void handleWithoutCallback(String cmd){
		Log.d(TAG, "callbacks.getRegisteredCallbackCount() == 0");
		if (cmd.startsWith(Commands.IND_INCOMING)) {
			Log.d(TAG, "IND_INCOMING fromBehind!");
            TransparentActivity.start(mContext,5,cmd.substring(2));
		}else if(cmd.startsWith(Commands.IND_CALL_SUCCEED)){
            Log.d(TAG, "IND_CALL_SUCCEED fromBehind!");
            TransparentActivity.start(mContext,4,cmd.substring(2));
        }else if(cmd.startsWith(Commands.IND_TALKING)){
            Log.d(TAG, "IND_CALL_SUCCEED fromBehind!");
            TransparentActivity.start(mContext,6,cmd.substring(2));
		}else if(cmd.startsWith(Commands.IND_HFP_STATUS)){
            Log.d(TAG, "IND_HFP_STATUS fromBehind!");
            int status = Integer.parseInt(cmd.substring(Commands.IND_HFP_STATUS.length()));
            EventBus.getDefault().postSticky(new BackgroundHfpStatusEvent(status));
        }else if(cmd.startsWith(Commands.IND_OUTGOING_TALKING_NUMBER)){
            Log.d(TAG, "IND_CALL_SUCCEED fromBehind!");
            EventBus.getDefault().postSticky(new BackgroundCurrentNumberEvent(cmd.substring(Commands.IND_OUTGOING_TALKING_NUMBER.length())));
        }
	}

	private void onSerialCommand(String cmd) {
		if (callbacks.getRegisteredCallbackCount() == 0) {
			handleWithoutCallback(cmd);
			return;
		}

		int i = callbacks.beginBroadcast();
		while (i > 0) {
			i--;
			IGocsdkCallback cbk = callbacks.getBroadcastItem(i);
			try {
				if (cmd.startsWith(Commands.IND_HFP_CONNECTED)) {
					cbk.onHfpConnected();
				} else if (cmd.startsWith(Commands.IND_HFP_DISCONNECTED)) {
					cbk.onHfpDisconnected();
				} else if (cmd.startsWith(Commands.IND_CALL_SUCCEED)) {
					if (cmd.length() < 4) {
						cbk.onCallSucceed("");
					} else {
						cbk.onCallSucceed(cmd.substring(2));
					}
				} else if (cmd.startsWith(Commands.IND_INCOMING)) {
					if (cmd.length() <= 2) {
						cbk.onIncoming("");
					} else {
						cbk.onIncoming(cmd.substring(2));
					}
				} else if (cmd.startsWith(Commands.IND_HANG_UP)) {
					cbk.onHangUp();
				} else if (cmd.startsWith(Commands.IND_TALKING)) {// 通话中:::IG[number]
					if (cmd.length() <= 2) {
						cbk.onTalking("");
					} else {
						cbk.onTalking(cmd.substring(2));
					}
				} else if (cmd.startsWith(Commands.IND_RING_START)) {// 开始响铃
					cbk.onRingStart();
				} else if (cmd.startsWith(Commands.IND_RING_STOP)) {// 停止响铃
					cbk.onRingStop();
				} else if (cmd.startsWith(Commands.IND_HF_LOCAL)) {//
					cbk.onHfpLocal();
				} else if (cmd.startsWith(Commands.IND_HF_REMOTE)) {// 蓝牙接听
					cbk.onHfpRemote();
				} else if (cmd.startsWith(Commands.IND_IN_PAIR_MODE)) {// 进入配对模式:::II
					cbk.onInPairMode();
				} else if (cmd.startsWith(Commands.IND_EXIT_PAIR_MODE)) {// 退出配对模式
					cbk.onExitPairMode();
				} else if (cmd.startsWith(Commands.IND_INIT_SUCCEED)) {// 上电初始化成功:::IS
					cbk.onInitSucceed();
				} else if (cmd.startsWith(Commands.IND_MUSIC_PLAYING)) {// 音乐播放
					Log.d(TAG, "callback Commands playing" + cmd);
					cbk.onMusicPlaying();
				} else if (cmd.startsWith(Commands.IND_MUSIC_STOPPED)) {// 音乐停止
					Log.d(TAG, "callback Commands stoped" + cmd);
					cbk.onMusicStopped();
				} else if (cmd.startsWith(Commands.IND_VOICE_CONNECTED)) {
					// cbk.onVoiceConnected();
				} else if (cmd.startsWith(Commands.IND_VOICE_DISCONNECTED)) {
					// cbk.onVoiceDisconnected();
				} else if (cmd.startsWith(Commands.IND_AUTO_CONNECT_ACCEPT)) {
					if (cmd.length() < 4) {
						Log.e(TAG, cmd + "=====error command");
					} else {
						cbk.onAutoConnectAccept(cmd.substring(2, 4));
					}
				} else if (cmd.startsWith(Commands.IND_CURRENT_ADDR)) {
					if (cmd.length() < 3) {
						Log.e(TAG, cmd + "==== error command");
					} else {
						cbk.onCurrentAddr(cmd.substring(2));
					}
				} else if (cmd.startsWith(Commands.IND_CURRENT_NAME)) {
					if (cmd.length() < 3) {
						Log.e(TAG, cmd + "==== error command");
					} else {
						cbk.onCurrentName(cmd.substring(2));
					}
				} else if (cmd.startsWith(Commands.IND_AV_STATUS)) {
					if (cmd.length() < 3) {
						Log.e(TAG, cmd + "=====error");
					} else {
						cbk.onAvStatus(Integer.parseInt(cmd.substring(2, 3)));
					}
				} else if (cmd.startsWith(Commands.IND_HFP_STATUS)) {
					if (cmd.length() < 3) {
						Log.e(TAG, cmd + " ==== error");
					} else {
						int status = Integer.parseInt(cmd.substring(2, 3));
						cbk.onHfpStatus(status);
					}
				} else if (cmd.startsWith(Commands.IND_VERSION_DATE)) {
					if (cmd.length() < 3) {
						Log.e(TAG, cmd + "====error");
					} else {
						cbk.onVersionDate(cmd.substring(2));
					}
				} else if (cmd.startsWith(Commands.IND_CURRENT_DEVICE_NAME)) {
					if (cmd.length() < 3) {
						Log.e(TAG, cmd + "====error");
					} else {
						cbk.onCurrentDeviceName(cmd.substring(2));
					}
				} else if (cmd.startsWith(Commands.IND_CURRENT_PIN_CODE)) {
					if (cmd.length() < 3) {
						Log.e(TAG, cmd + "====error");
					} else {
						cbk.onCurrentPinCode(cmd.substring(2));
					}
				} else if (cmd.startsWith(Commands.IND_A2DP_CONNECTED)) {
					cbk.onA2dpConnected();
				} else if (cmd.startsWith(Commands.IND_A2DP_DISCONNECTED)) {
					cbk.onA2dpDisconnected();
				} else if (cmd.startsWith(Commands.IND_CURRENT_AND_PAIR_LIST)) {
					if (cmd.length() < 15) {
						Log.e(TAG, cmd + "====error");
					} else if (cmd.length() == 15) {
						cbk.onCurrentAndPairList(Integer.parseInt(cmd.substring(2, 3)), "", cmd.substring(3, 15));
					} else {
						cbk.onCurrentAndPairList(Integer.parseInt(cmd.substring(2, 3)), cmd.substring(15),
								cmd.substring(3, 15));
					}
				} else if (cmd.startsWith(Commands.IND_PHONE_BOOK)) {// 联系人信息
					if (cmd.length() < 6) {
						Log.e(TAG, cmd + "====error");
					} else {

						String name = null;
						String number = null;
						if (cmd.contains("[FF]")) {
							String[] split = cmd.split("\\[FF\\]");
							if (split.length == 2) {
								name = split[0].substring(2);
								number = split[1];
							}
						} else {
							int nameLen = Integer.parseInt(cmd.substring(2, 4));
							int numLen = Integer.parseInt(cmd.substring(4, 6));
							byte[] bytes = cmd.getBytes();
							if (nameLen > 0) {
								byte[] buffer = new byte[nameLen];
								System.arraycopy(bytes, 6, buffer, 0, nameLen);
								name = new String(buffer);
							} else {
								name = "";
							}
							if (numLen > 0) {
								if((6 + nameLen+numLen) == bytes.length){
									byte[] buffer = new byte[numLen];
									System.arraycopy(bytes, 6 + nameLen, buffer, 0, numLen);
									number = new String(buffer);
								}else{
									Log.e("goc", "PhoneBook bytes length is err!");
								}

							} else {
								number = "";
							}
						}
						if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)) {
							cbk.onPhoneBook(name, number);
						}

					}
				} else if (cmd.startsWith(Commands.IND_PHONE_BOOK_DONE)) {
					cbk.onPhoneBookDone();
				} else if (cmd.startsWith(Commands.IND_SIM_DONE)) {
					cbk.onSimDone();
				} else if (cmd.startsWith(Commands.IND_CALLLOG_DONE)) {
					cbk.onCalllogDone();
				} else if (cmd.startsWith(Commands.IND_CALLLOG)) {
					if (cmd.length() < 4) {
						Log.e(TAG, cmd + "====error");
					} else {
						String[] split = cmd.substring(3).split("\\[FF\\]");
						cbk.onCalllog(Integer.parseInt(cmd.substring(2, 3)), split[0], split[1]);
					}
				} else if (cmd.startsWith(Commands.IND_DISCOVERY)) {
					if (cmd.length() < 14) {
						Log.e(TAG, cmd + "===error");
					} else if (cmd.length() == 14) {
						cbk.onDiscovery("", "", cmd.substring(2));
					} else {
						cbk.onDiscovery("", cmd.substring(14), cmd.substring(2, 14));
					}
				} else if (cmd.startsWith(Commands.IND_DISCOVERY_DONE)) {
					cbk.onDiscoveryDone();
				} else if (cmd.startsWith(Commands.IND_LOCAL_ADDRESS)) {
					if (cmd.length() != 14) {
					}
					cbk.onLocalAddress(cmd.substring(2));
				} else if (cmd.startsWith(Commands.IND_OUTGOING_TALKING_NUMBER)) {
					if (cmd.length() <= 2) {
						cbk.onOutGoingOrTalkingNumber("");
					} else {
						cbk.onOutGoingOrTalkingNumber(cmd.substring(2));
					}
				} else if (cmd.startsWith(Commands.IND_MUSIC_INFO)) {
					if (cmd.length() <= 2) {
						Log.e(TAG, cmd + "===error");
					} else {
						String info = cmd.substring(2);
						String[] arr = info.split("\\[FF\\]");
						if (arr.length == 5) {
							cbk.onMusicInfo(arr[0], arr[1], "none",Integer.parseInt(arr[2]), Integer.parseInt(arr[3]),
									Integer.parseInt(arr[4]));

						} else if (arr.length == 6) {
							cbk.onMusicInfo(arr[0], arr[1],arr[2],Integer.parseInt(arr[3]), Integer.parseInt(arr[4]),
									Integer.parseInt(arr[5]));
						} else {
							Log.e(TAG, cmd + "===error");
						}
					}
				} else if (cmd.startsWith(Commands.IND_MUSIC_POS)) {
					if(cmd.length() != 10){
						Log.e(TAG, cmd + "====error");
					}else{
						cbk.onMusicPos(Integer.parseInt(cmd.substring(2,6),16),Integer.parseInt(cmd.substring(6,10),16));
					}
				} else if (cmd.startsWith(Commands.IND_PROFILE_ENABLED)) {
					if (cmd.length() < 12) {
						Log.e(TAG, cmd + "====error");
					} else {
						boolean[] enabled = new boolean[10];
						for (int ii = 0; ii < 10; ii++) {
							if (cmd.charAt(ii + 2) == '0') {
								enabled[ii] = false;
							} else {
								enabled[ii] = true;
							}
						}
						cbk.onProfileEnbled(enabled);
					}
				} else if (cmd.startsWith(Commands.IND_MESSAGE_LIST)) {
					String text = cmd.substring(2);
					if (text.length() == 0) {
						Log.e("goc", "cmd error:param==0" + cmd);
					} else {
						String[] arr = text.split("\\[FF\\]", -1);
						if (arr.length != 6) {
							Log.e("goc", "cmd error:arr.length=" + arr.length + ";" + cmd);
						} else {
							cbk.onMessageInfo(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
						}
					}
				} else if (cmd.startsWith(Commands.IND_MESSAGE_TEXT)) {
					cbk.onMessageContent(cmd.substring(2));
				} else if (cmd.startsWith(Commands.IND_OK)) {
				} else if (cmd.startsWith(Commands.IND_ERROR)) {
				} else {
				}
			} catch (RemoteException e) {
			}
		}

		callbacks.finishBroadcast();
	}


	private void onByte(byte b) {
		if ('\n' == b)
			return;
		if (count >= 1000)
			count = 0;
		if ('\r' == b) {
			if (count > 0) {
				byte[] buf = new byte[count];
				System.arraycopy(serialBuffer, 0, buf, 0, count);
				onSerialCommand(new String(buf));
				count = 0;
			}
			return;
		}
		if ((b & 0xFF) == 0xFF) {
			serialBuffer[count++] = '[';
			serialBuffer[count++] = 'F';
			serialBuffer[count++] = 'F';
			serialBuffer[count++] = ']';
		} else {
			serialBuffer[count++] = b;
		}
	}

	public void onBytes(byte[] data) {
		for (byte b : data) {
			onByte(b);
		}
	}

}
