package com.kyle.mycar.View;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Zhang on 2017/6/4.
 */

public class MyItemDecoration extends Y_DividerItemDecoration {

    public MyItemDecoration(Context context) {
        super(context);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public Y_Divider getDivider(int itemPosition) {

        return new Y_Divider(false,false,false,true,1,0x66e5e5e5);
    }
}
