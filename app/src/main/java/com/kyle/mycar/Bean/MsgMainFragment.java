package com.kyle.mycar.Bean;

import com.kyle.mycar.db.Table.Record;

import java.util.List;

/**
 * eventbus bean
 * Created by Zhang on 2017/5/4.
 */

public class MsgMainFragment {

    public static final int SET_ADAPTER=0;
    public static final int REFRESH =1;
    public static final int UPDATE_AN_NEW_ONE_DATA =2;
    public static final int LOAD_MORE = 3;
    public static final int LOAD_MORE_END = 4;

    private int flag;
    private List<Record> recordList;

    public MsgMainFragment(int flag) {
        this(flag,null);
    }

    public MsgMainFragment(int flag, List<Record> recordList) {
        this.flag = flag;
        this.recordList = recordList;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<Record> getTag() {
        return recordList;
    }

    public void setTag(List<Record> recordList) {
        this.recordList = recordList;
    }
}
