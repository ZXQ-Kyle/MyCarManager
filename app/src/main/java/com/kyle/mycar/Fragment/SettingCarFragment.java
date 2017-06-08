package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyle.mycar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingCarFragment extends BaseFragment {

    @Override
    public View initView() {
        noEventBus=true;
        return View.inflate(mActivity,R.layout.fragment_setting_car,null);
    }

    @Override
    public void initData() {

    }

}
