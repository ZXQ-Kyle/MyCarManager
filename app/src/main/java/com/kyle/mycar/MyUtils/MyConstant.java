package com.kyle.mycar.MyUtils;

import com.kyle.mycar.Fragment.OilFragment;

/**
 * 全局常量
 * Created by Zhang on 2017/5/7.
 */

public class MyConstant {


    //第一次进入app，初始化db
    public static String First_IN = "first_in";
    //获取时间后返回数据
    // MessageEvent flag
    public static final int OIL_FRAGMENT_RETURN_DATE =1;
    public static final int OIL_FRAGMENT_RETURN_TIME =2;

    public static final int MT_FRAGMENT_RETURN_DATE =3;
    public static final int MT_FRAGMENT_RETURN_TIME =4;

    public static final int UPDATE_HEAD_IMAGE = 5;
    public static final int OPEN_DRAWER = 6;
    public static final int UPDATE_MT = 7;

    public static final int QUERY_FROM_DATE = 13;
    public static final int QUERY_TO_DATE = 14;

    //MsgSetting 事件flag
    public static final int SETTING_ADD_OIL_TYPE = 8;
    public static final int DELIVER_OIL_TYPE = 9;
    public static final int SETTING_UPDATE_OIL_TYPE = 10;
    public static final int SETTING_ADD_TAG = 11;
    public static final int SETTING_UPDATE_TAG = 12;

    //MsgQuery 事件flag
    public static final int QUERY_SET_ADAPTER = 13;



}
