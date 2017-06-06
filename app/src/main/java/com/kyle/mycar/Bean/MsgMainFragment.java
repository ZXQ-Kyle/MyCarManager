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
    public static final int UPDATE_AN_OLD_DATA =5;
    public static final int UPDATE_REFRESH =6;

    public static final int DETAIL_DELETE_OIL =7;

    private int flag;
    private List<Record> recordList;
    private Record record;

    public MsgMainFragment(int flag) {
        this(flag,null,null);
    }

    public MsgMainFragment(int flag, Record record) {
        this(flag,null,record);
    }

    public MsgMainFragment(int flag, List<Record> recordList) {
        this(flag,recordList,null);
    }

    public MsgMainFragment(int flag, List<Record> recordList, Record record) {
        this.flag = flag;
        this.recordList = recordList;
        this.record = record;
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

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
