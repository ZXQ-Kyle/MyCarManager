package com.kyle.mycar.Bean;

import android.graphics.Bitmap;

/**
 * eventbus bean
 * Created by Zhang on 2017/5/4.
 */

public class MessageEvent {

    private String msg;

    private int flag;

    private Object obj;

    public MessageEvent() {
    }

    public MessageEvent( int flag) {
        this.flag = flag;
    }

    public MessageEvent(String msg, int flag) {
        this.msg = msg;
        this.flag = flag;
    }

    public MessageEvent(int flag, Object obj) {
        this.flag = flag;
        this.obj = obj;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
