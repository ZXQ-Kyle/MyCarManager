package com.kyle.mycar.Bean;

/**
 * Created by Zhang on 2017/6/5.
 */

public class MsgSetting {
    public int flag;
    public Object object;

    public MsgSetting() {
    }

    public MsgSetting(int flag) {
        this.flag = flag;
    }

    public MsgSetting(int flag, Object object) {
        this.flag = flag;
        this.object = object;
    }
}
