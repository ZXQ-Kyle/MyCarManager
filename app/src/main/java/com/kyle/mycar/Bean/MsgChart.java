package com.kyle.mycar.Bean;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by Zhang on 2017/6/1.
 */

public class MsgChart {

    public ArrayList<Entry> entries;
    public float average;

    public MsgChart(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    public MsgChart(ArrayList<Entry> entries, float average) {
        this.entries = entries;
        this.average = average;
    }
}
