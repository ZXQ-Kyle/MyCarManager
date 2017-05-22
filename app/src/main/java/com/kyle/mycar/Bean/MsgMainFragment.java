package com.kyle.mycar.Bean;

/**
 * eventbus bean
 * Created by Zhang on 2017/5/4.
 */

public class MsgMainFragment {

    public static final int SET_ADAPTER=0;
    public static final int ADAPTER_NOTIFY_CHANGE=1;
    public static final int UPDATE_DATA=2;

    private int flag;

    public MsgMainFragment(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
