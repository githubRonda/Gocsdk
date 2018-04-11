package com.goodocom.gocsdk.event;

public class MessageListEvent {
	public String handle;//短信具体内容获取指令
	public boolean read;//查看状态
	public String time;//时间
	public String name;//名字
	public String num;//号码
	public String title;//短信内容缩写  

	public MessageListEvent(String handle, boolean read, String time,String name, String num, String title) {
		this.handle = handle;
		this.read = read;
		this.time = time;
		this.name = name;
		this.num = num;
		this.title = title;
	}
}
