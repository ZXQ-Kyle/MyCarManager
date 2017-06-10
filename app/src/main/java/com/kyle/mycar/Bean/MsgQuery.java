package com.kyle.mycar.Bean;

/**
 * Created by Zhang on 2017/6/9.
 */

public class MsgQuery {
    public int flag;
    public String string;
    public Object object;

    public MsgQuery(int flag, String string) {
        this.flag = flag;
        this.string = string;
    }

    public MsgQuery(int flag, Object object) {
        this.flag = flag;
        this.object = object;
    }
}
