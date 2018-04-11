package com.goodocom.gocsdk.event;

/**
 * Created by www37 on 2017/5/3.
 */

public class MusicPosEvent {
    public int current;
    public int total;

    public MusicPosEvent(int current,int total){
        this.total = total;
        this.current = current;
    }
}
