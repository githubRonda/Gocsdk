package com.goodocom.gocsdk.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.util.Log;

import com.goodocom.gocsdk.Commands;
import com.goodocom.gocsdk.Config;
import com.goodocom.gocsdk.IGocsdkCallback;
import com.goodocom.gocsdk.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class GocsdkService extends Service {
	public static final String TAG = "GocsdkService";
	public static final int MSG_START_SERIAL = 1;//串口
	public static final int MSG_SERIAL_RECEIVED = 2; //接收到串口信息
	private static final int RESTART_DELAY = 2000; // ms
	private CommandParser parser;
	private final boolean use_socket = false;
	private SerialThread serialThread = null;
	private volatile boolean running = true;
	private RemoteCallbackList<IGocsdkCallback> callbacks;

	private static  final String ACTION_CLOSE_BT = "android.intent.action.BT_OFF";
	private static  final String ACTION_OPEN_BT = "android.intent.action.BT_ON";


	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(ACTION_CLOSE_BT.equals(intent.getAction())){
				write(Commands.CLOSE_BT);
			}else if(ACTION_OPEN_BT.equals(intent.getAction())){
				write(Commands.OPEN_BT);
			}
		}
	};

	@Override
	public void onCreate() {


		Log.d("app","Service onCreate");
		callbacks = new RemoteCallbackList<IGocsdkCallback>();
		parser = new CommandParser(callbacks,this);//CommandParser.getInstance(callbacks, this);
		handler.sendEmptyMessage(MSG_START_SERIAL);
		hand = handler;

		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_OPEN_BT);
		filter.addAction(ACTION_CLOSE_BT);

		registerReceiver(receiver,filter);

		super.onCreate();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("app","onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		running = false;
		callbacks.kill();
		Log.d("app","Service onDestroy");
		unregisterReceiver(receiver);

		super.onDestroy();
	}
	private static Handler hand = null;
	public static Handler getHandler(){
		return hand;
	}
	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == MSG_START_SERIAL) {
				Log.d("app", "serialThread start!");
				serialThread = new SerialThread();
				serialThread.start();
			} else if (msg.what == MSG_SERIAL_RECEIVED) {
				byte[] data = (byte[]) msg.obj;
				parser.onBytes(data);
			}
		};
	};
	@Override
	public IBinder onBind(Intent intent) {
		Log.d("app","onBind");
		return new GocsdkServiceImp(this);
	}
	@Override
	public boolean onUnbind(Intent intent) {
		Log.d("app","onUnbind");
		return super.onUnbind(intent);
	}
	class SerialThread extends Thread {
		private InputStream inputStream;
		private OutputStream outputStream = null;
		private byte[] buffer = new byte[1024];
		public void write(byte[] buf) {
			if (outputStream != null) {
				try {
					outputStream.write(buf);
				} catch (IOException e) {
					
				}
			}
		}
		
		public SerialThread() {
		}

		@Override
		public void run() {
			LocalSocket client = null;
			SerialPort serial = null;
			
			int n;
			try {
				if(use_socket){
					Log.d("app", "use socket!");
					client = new LocalSocket();
					client.connect(new LocalSocketAddress(Config.SERIAL_SOCKET_NAME,LocalSocketAddress.Namespace.RESERVED));
					inputStream = client.getInputStream();
					outputStream = client.getOutputStream();
				}else{
					Log.d("app","use serial!");
					serial = new SerialPort(new File("/dev/goc_serial"),115200,0);
					if(serial!=null){
						Log.d("app","serial not is null!");
					}else{
						Log.d("app","serial is null!");
					}
					inputStream = serial.getInputStream();
					outputStream = serial.getOutputStream();
				}
				while (running) {
					n = inputStream.read(buffer);
					if (n < 0) {
						if(use_socket ){
							if(client != null)client.close();
						}else{
							if(serial != null)serial.close();
						}
						throw new IOException("n==-1");
					}
					
					byte[] data = new byte[n];
					System.arraycopy(buffer, 0, data, 0, n);
					handler.sendMessage(handler.obtainMessage(
							MSG_SERIAL_RECEIVED, data));
				}
			} catch (IOException e) {
				try {
					if(use_socket){
						if(client != null)client.close();
					}else{
						if(serial != null)serial.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				handler.sendEmptyMessageDelayed(MSG_START_SERIAL, RESTART_DELAY);
				return;
			}
			
			try {
				if(use_socket){
					if(client != null)client.close();
				}else{
					if(serial != null)serial.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void write(String str) {
		if (serialThread == null) return;
		Log.d("app","write:"+str);
		serialThread.write((Commands.COMMAND_HEAD + str + "\r\n").getBytes());
	}

	public void registerCallback(IGocsdkCallback callback) {
		Log.d(TAG, "registerCallback");
		callbacks.register(callback);
		Log.d(TAG, "callback count:"+callbacks.getRegisteredCallbackCount());
	}

	public void unregisterCallback(IGocsdkCallback callback) {
		callbacks.unregister(callback);
	}

}
