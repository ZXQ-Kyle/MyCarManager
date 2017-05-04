package com.kyle.mycar.fragment;


import android.view.View;

import com.kyle.mycar.R;

/**
 *
 */
public class MainFragment extends BaseFragment {

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_main,null);
        return view;
    }

    @Override
    public void initData() {

    }

}
