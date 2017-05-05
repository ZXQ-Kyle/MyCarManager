package com.kyle.mycar.Fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kyle.mycar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 */
public class OilFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.spinner_oil)
    AppCompatSpinner spinnerOil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_oil, null);
    }

    @Override
    public void initData() {
        //spinner初始化
        String[] stringArray = getResources().getStringArray(R.array.spinner_oil);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item,stringArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOil.setAdapter(adapter);


    }
}
