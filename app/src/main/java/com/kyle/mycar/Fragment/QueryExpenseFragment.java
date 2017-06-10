package com.kyle.mycar.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyle.mycar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryExpenseFragment extends BaseFragment {

    @Override
    public View initView() {
        return View.inflate(mActivity,R.layout.fragment_query_expense,null);
    }

    @Override
    public void initData() {

    }
}
