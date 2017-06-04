package com.kyle.mycar.View;

import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.kyle.mycar.R;

/**
 * Created by Zhang on 2017/5/28.
 */

public class MyMarkerView extends MarkerView {

    private TextView tv;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tv = (TextView) findViewById(R.id.tv_markerView);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to selector_update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
            tv.setText("" + Utils.formatNumber(e.getY(), 1, false));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
