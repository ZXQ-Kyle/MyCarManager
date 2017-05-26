package com.kyle.mycar.MyUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * putString,getString
 * putboolean,getboolean
 * putInt,getInt
 * remove
 * Created by Kyle on 2016/10/13.
 */

public class SpUtils {

    private static SharedPreferences sp;

    /**
     * @param context
     * @param keyName
     * @param value
     */
    public static void putSring(Context context, String keyName, String value) {
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(keyName, value);
        editor.apply();
    }

    /**
     * @param context
     * @param keyName 存储键名称
     * @return 返回存储的string值，否则为""
     */
    public static String getString(Context context, String keyName) {
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        return sp.getString(keyName,"");
    }

    public static void putboolean(Context context, String keyName, boolean value) {
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        sp.edit().putBoolean(keyName, value).apply();
    }

    /**
     * @param context
     * @param keyName
     * @return defValue false
     */
    public static boolean getboolean(Context context, String keyName) {
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(keyName,false);
    }
    public static void remove(Context context, String keyName) {
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(keyName).apply();

    }

    public static void putInt(Context context, String keyName, int value) {
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putInt(keyName,value).apply();
    }

    /**
     * @param context
     * @param keyName
     * @return 无数据时默认返回0
     */
    public static int getInt(Context context, String keyName) {
        if (sp == null){
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getInt(keyName,0);
    }


}
