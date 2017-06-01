package com.kyle.mycar.Fragment;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zhang on 2017/6/1.
 */

public class DateValueFormatter implements IAxisValueFormatter {

    private SimpleDateFormat mFormat=new SimpleDateFormat("MM-dd");

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String format = mFormat.format(new Date((long) value));
        // TODO: 2017/6/1 判断年份 ，只显示月日
        return format;
    }
}
