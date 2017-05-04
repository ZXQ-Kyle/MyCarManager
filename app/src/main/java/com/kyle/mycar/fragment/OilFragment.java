package com.kyle.mycar.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyle.mycar.R;

/**
 */
public class OilFragment extends BaseFragment {

    @Override
    public View initView() {
        return View.inflate(mActivity,R.layout.fragment_oil,null);
    }

    @Override
    public void initData() {

    }
}
