package com.goodocom.gocsdk.event;

public class PlayStatusEvent {
	public boolean playing = false;//音乐播放状态
	public PlayStatusEvent(boolean playing){
		this.playing = playing;
	}
}
