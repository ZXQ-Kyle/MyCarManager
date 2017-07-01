package com.kyle.mycar.Fragment;


import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.SpUtils;
import com.kyle.mycar.R;
import com.orhanobut.logger.Logger;
import com.transitionseverywhere.TransitionManager;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingCarManagerFragment extends BaseFragment {


    @BindView(R.id.iv_car_brand)
    ImageView ivCarBrand;
    @BindView(R.id.textView3)
    TextView tvCarType;
    @BindView(R.id.iv_car_add)
    ImageView ivCarAdd;
    @BindView(R.id.root)
    ConstraintLayout root;


    Unbinder unbinder;

    @Override
    public View initView() {
        noEventBus=true;
        return View.inflate(mActivity, R.layout.item_car, null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.car_manager, 2, 0, null);
        showCar();

    }

    private void showCar() {
        String carBrand = SpUtils.getString(mActivity, MyConstant.CAR_BRAND);
        if (!TextUtils.isEmpty(carBrand)) {
            String carType = SpUtils.getString(mActivity, MyConstant.CAR_TYPE);
            TransitionManager.beginDelayedTransition(root);
            ivCarBrand.setVisibility(View.INVISIBLE);
            tvCarType.setText(carType);
            tvCarType.setVisibility(View.VISIBLE);

        }
    }


    @OnClick({R.id.iv_car_brand, R.id.textView3, R.id.iv_car_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_car_brand:
            case R.id.textView3:
                //修改
                mActivity.switchFrag(this,new SettingCarAddFragment(),false,null);
                break;
            case R.id.iv_car_add:
                //添加
                if (tvCarType.getVisibility()==View.GONE){
                    mActivity.switchFrag(this,new SettingCarAddFragment(),false,null);
                }else {
                    Snackbar.make(getView(),R.string.single_car_warning,Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        showCar();
    }
}
