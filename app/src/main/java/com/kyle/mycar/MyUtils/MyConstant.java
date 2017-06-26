package com.kyle.mycar.MyUtils;


/**
 * 全局常量
 * Created by Zhang on 2017/5/7.
 */

public class MyConstant {

    public static final long PAGE_SIZE = 15;
    public static final String USER_ID = "userID";
    public static final String COPY_DB_SUCESS = "copy_db_sucess";

    public static final String CAR_BRAND = "car_brand";
    public static final String CAR_TYPE = "car_type";
    //获取时间后返回数据
    // MessageEvent flag
    public static final int OIL_FRAGMENT_RETURN_DATE =1;

    public static final int OIL_FRAGMENT_RETURN_TIME =2;
    public static final int MT_FRAGMENT_RETURN_DATE =3;

    public static final int MT_FRAGMENT_RETURN_TIME =4;
    public static final int UPDATE_HEAD_IMAGE = 5;
    public static final int OPEN_DRAWER = 6;

    public static final int UPDATE_MT = 7;
    public static final int QUERY_OIL_FROM_DATE = 16;
    public static final int QUERY_OIL_TO_DATE = 17;
    public static final int QUERY_EXPENSE_FROM_DATE = 18;

    public static final int QUERY_EXPENSE_TO_DATE = 19;

    public static final int  COLSE_LOGIN=23;
    public static final int APP_UPDATE = 26;
    //MsgSetting 事件flag
    public static final int SETTING_ADD_OIL_TYPE = 8;
    public static final int DELIVER_OIL_TYPE = 9;
    public static final int SETTING_UPDATE_OIL_TYPE = 10;
    public static final int SETTING_ADD_TAG = 11;

    public static final int SETTING_UPDATE_TAG = 12;
    public static final int SETTING_CAR_BRAND = 24;

    public static final int SETTING_CAR_TYPE = 25;

    //MsgQuery 事件flag
    public static final int QUERY_NEW_DATA = 13;
    public static final int QUERY_LOAD_MORE = 14;
    public static final int QUERY_REFRESH = 15;


    public static final int QUERY_EX_NEW_DATA = 20;
    public static final int QUERY_EX_LOAD_MORE = 21;
    public static final int QUERY_EX_REFRESH = 22;

}
