package com.kyle.mycar.MyUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 默认"yyyy年MM月dd日  HH:mm"格式日期
 * Created by Kyle on 2017/2/9.
 */

public class MyDateUtils {

    private static SimpleDateFormat mDateFormat;

    /**string转long
     * @param dateStr yyyy年MM月dd日  HH:mm
     * @return 返回long型时间，失败返回-1
     */
    public static long strToLong(String dateStr) {
        if (null== mDateFormat) mDateFormat = new SimpleDateFormat("yyyy年MM月dd日  HH:mm", Locale.getDefault());

        try {
            return mDateFormat.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**long转string
     * @param time
     * @return yyyy年MM月dd日  HH:mm 失败返回null
     */
    public static String longToStr(long time) {
        if (null== mDateFormat) mDateFormat = new SimpleDateFormat("yyyy年MM月dd日  HH:mm", Locale.getDefault());

        return mDateFormat.format(new Date(time));
    }
    /**long转string
     * @param time
     * @param sdf yyyy年MM月dd日  HH:mm:ss SSS
     * @return 失败返回null
     */
    public static String longToStr(long time,String sdf) {
        mDateFormat = new SimpleDateFormat(sdf, Locale.getDefault());
        return mDateFormat.format(new Date(time));
    }

}
