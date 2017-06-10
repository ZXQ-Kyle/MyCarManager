package com.kyle.mycar.Bean;

import com.kyle.mycar.db.Table.Record;

import java.util.List;

/**
 * eventbus bean
 * Created by Zhang on 2017/5/4.
 */

public class MsgMainFragment {

    public static final int SET_ADAPTER=50;
    public static final int REFRESH =51;
    public static final int UPDATE_AN_NEW_ONE_DATA =92;
    public static final int LOAD_MORE = 53;
    public static final int LOAD_MORE_END = 54;
    public static final int UPDATE_AN_OLD_DATA =55;
    public static final int UPDATE_REFRESH =56;

    public static final int DETAIL_DELETE_OIL =57;
    public static final int DETAIL_DELETE_MT = 58 ;

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

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
