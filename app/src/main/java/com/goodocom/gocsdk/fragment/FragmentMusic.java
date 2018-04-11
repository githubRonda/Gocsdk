package com.goodocom.gocsdk.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.goodocom.gocsdk.A2dpStatus;
import com.goodocom.gocsdk.IGocsdkService;
import com.goodocom.gocsdk.R;
import com.goodocom.gocsdk.activity.MainActivity;
import com.goodocom.gocsdk.event.A2dpStatusEvent;
import com.goodocom.gocsdk.event.MusicInfoEvent;
import com.goodocom.gocsdk.event.MusicPosEvent;
import com.goodocom.gocsdk.event.PlayStatusEvent;
import com.goodocom.gocsdk.service.GocsdkCallbackImp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FragmentMusic extends Fragment implements OnClickListener {
	private MainActivity activity;
	private ImageView iv_pause;
	private ImageView iv_play;
	
	private TextView tv_music_name;
	private TextView tv_music_artist;

	private TextView tv_music_posandtotal;
	private TextView tv_currenttime;
	private SeekBar sb_progress;

	private AudioManager audioManager;

	private TextView tv_totaltime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/**
		 * 该页面只要加载了，先判断蓝牙是否连接， 如果连接就发送Handler消息给GocsdkService，
		 * 让它发送AT命令字节，调用回调接口的方法，发送音乐信息
		 */
		if (GocsdkCallbackImp.hfpStatus > 0) {
			try {
				IGocsdkService service = MainActivity.getService();
				if(service != null)service.getMusicInfo();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		View view = View.inflate(activity, R.layout.fragmentmusic, null);
		// 用来接收消息的
		EventBus.getDefault().register(this);

		iv_play = (ImageView) view.findViewById(R.id.iv_play);
		iv_pause = (ImageView) view.findViewById(R.id.iv_pause);

		view.findViewById(R.id.iv_previous).setOnClickListener(this);
		view.findViewById(R.id.iv_next).setOnClickListener(this);
		view.findViewById(R.id.iv_vol_up).setOnClickListener(this);
		view.findViewById(R.id.iv_vol_down).setOnClickListener(this);

		tv_music_name = (TextView) view.findViewById(R.id.tv_music_name);
		tv_music_artist = (TextView) view.findViewById(R.id.tv_music_artist);
		tv_totaltime = (TextView) view.findViewById(R.id.tv_totaltime);
		tv_music_posandtotal = (TextView) view.findViewById(R.id.tv_music_posandtotal);
		sb_progress = (SeekBar) view.findViewById(R.id.sb_progress);
		tv_currenttime = (TextView) view.findViewById(R.id.tv_currenttime);

		iv_play.setOnClickListener(this);
		iv_pause.setOnClickListener(this);

		initStatus();

		audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
		return view;
	}

	@Override
	public void onDestroyView() {
		EventBus.getDefault().unregister(this);

		super.onDestroyView();
	}

	private void initStatus() {
		Log.d("goc","a2dpStatus:"+GocsdkCallbackImp.a2dpStatus);
		if(GocsdkCallbackImp.a2dpStatus== A2dpStatus.STREAMING){
			Log.d("app", "initstatus is play!");
			iv_pause.setVisibility(View.VISIBLE);
			iv_play.setVisibility(View.GONE);
		}else{
			Log.d("app", "initstatus is pause!");
			iv_pause.setVisibility(View.GONE);
			iv_play.setVisibility(View.VISIBLE);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(PlayStatusEvent status) {
		
		if (!status.playing) {
			Log.d("app", "callback is pause!");
			iv_pause.setVisibility(View.GONE);
			iv_play.setVisibility(View.VISIBLE);
		} else {
			Log.d("app", "callback is play!");
			iv_pause.setVisibility(View.VISIBLE);
			iv_play.setVisibility(View.GONE);
	
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(MusicPosEvent pos){
		tv_currenttime.setText(String.format("%02d:%02d" , pos.current / 60 , pos.current % 60));
		tv_totaltime.setText(String.format("%02d:%02d",pos.total / 60 , pos.total % 60));
		if(pos.total > 0)sb_progress.setProgress(pos.current * 100 / pos.total);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(A2dpStatusEvent event){
		if(event.status <= A2dpStatus.CONNECTED){
			iv_pause.setVisibility(View.VISIBLE);
			iv_play.setVisibility(View.GONE);
		}else{
			iv_pause.setVisibility(View.VISIBLE);
			iv_play.setVisibility(View.GONE);
		}
	}

	// 接收音乐信息方法
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEvent(MusicInfoEvent info) {
		tv_music_name.setText(info.name);
		tv_music_artist.setText(info.artist);
		//将毫秒转化为秒
		int duration = info.duration / 1000;
		int min = duration / 60;
		int sec = duration % 60;

		String totalTime = "";
		if(min < 10)totalTime += "0";
		totalTime += min;
		totalTime += ":";
		if(sec < 10) totalTime += "0";
		totalTime += sec;

		tv_totaltime.setText(totalTime);
		tv_music_posandtotal.setText(info.pos + "/" + info.total);

		tv_currenttime.setText("00:00");
		sb_progress.setProgress(0);
	}

	// 上一首 下一首 音量的加减？
	@Override
	public void onClick(View v) {
		if (GocsdkCallbackImp.hfpStatus < 3) {
			Toast.makeText(activity, "请先连接设备", Toast.LENGTH_SHORT).show();
			return;
		}

		switch (v.getId()) {
		case R.id.iv_play:
			Log.d("app", "click play image!");
			try {
				if(MainActivity.getService() != null)MainActivity.getService().musicPlayOrPause();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		case R.id.iv_pause:
			Log.d("app", "click pause image!");
			try {
				if(MainActivity.getService() != null)MainActivity.getService().musicPlayOrPause();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		case R.id.iv_previous:
			Log.d("app", "click previous");
			try {
				if(MainActivity.getService() != null)MainActivity.getService().musicPrevious();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			tv_currenttime.setText("00:00");
			sb_progress.setProgress(0);
			break;
		case R.id.iv_next:
			try {
				if(MainActivity.getService() != null)MainActivity.getService().musicNext();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			tv_currenttime.setText("00:00");
			sb_progress.setProgress(0);
			break;
		case R.id.iv_vol_down:
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER,
					AudioManager.FX_FOCUS_NAVIGATION_UP);
			break;
		case R.id.iv_vol_up:
			audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE,
					AudioManager.FX_FOCUS_NAVIGATION_UP);
			break;
		}
	}
}
