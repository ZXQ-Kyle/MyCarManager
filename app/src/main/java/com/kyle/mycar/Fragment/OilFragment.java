package com.kyle.mycar.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.kyle.mycar.MyUtils.Tint;
import com.kyle.mycar.R;
import com.kyle.mycar.View.ImgAndEtView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 */
public class OilFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.spinner_oil)
    AppCompatSpinner spinnerOil;
    @BindView(R.id.iv_oil_oilcan)
    ImageView ivOilOilcan;
    @BindView(R.id.iae_full_oil)
    ImgAndEtView iaeFullOil;
    @BindView(R.id.iae_warning)
    ImgAndEtView iaeWarning;
    @BindView(R.id.iae_date)
    ImgAndEtView iaeDate;
    @BindView(R.id.iae_odometer)
    ImgAndEtView iaeOdometer;
    @BindView(R.id.iv_oil_money)
    ImageView ivOilMoney;
    @BindView(R.id.et_oil_money)
    EditText etOilMoney;
    @BindView(R.id.te_oil_price)
    EditText teOilPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = initView();
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, stringArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOil.setAdapter(adapter);

        ivOilMoney.setImageDrawable(Tint.tintDrawable(ivOilMoney.getDrawable().mutate(),getResources().
                getColorStateList(R.color.colorCyan)));

        ivOilOilcan.setImageDrawable(Tint.tintDrawable(getResources().getDrawable(R.drawable.oilcan).mutate(), getResources()
                .getColorStateList(R.color.colorCyan)));


        iaeFullOil.setUnEditable();
        iaeFullOil.setText("本次邮箱是否加满？");
        iaeWarning.setUnEditable();
        iaeWarning.setText("忘记记录上次加油？");

    }
}
