package com.kyle.mycar.Fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.MyUtils.GlobalConstant;
import com.kyle.mycar.R;
import com.kyle.mycar.View.ImgAndEtView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 */
public class OilFragment extends BaseFragment {

    public static final String TAG="OilFragment";
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
    @BindView(R.id.et_oil_quantity)
    EditText etOilQuantity;
    @BindView(R.id.iae_note)
    ImgAndEtView iaeNote;
    @BindView(R.id.btn_oil)
    Button btnOil;
    private String strDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = initView();
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
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

        iaeFullOil.setUnEditable();
        iaeFullOil.setText("本次邮箱是否加满？");
        iaeWarning.setUnEditable();
        iaeWarning.setText("忘记记录上次加油？");
        iaeDate.setUnEditable();

        //设置日期默认为当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        Date date = new Date(System.currentTimeMillis());
        strDate = sdf.format(date);
        iaeDate.setText(strDate);
        //初始化默认值
        iaeOdometer.setInputTypeOfNumber();


    }
    //视图点击事件
    @OnClick({R.id.iae_date, R.id.btn_oil})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iae_date:
                showDatePicker();
                break;
            case R.id.btn_oil:

                break;
        }
    }

    private void showDatePicker() {
        DatePickerDialogFragment dialogFragment = new DatePickerDialogFragment();
        dialogFragment.show(getFragmentManager(),"date");
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDateMessage(MessageEvent msg){

        switch (msg.getFlag()) {
            case GlobalConstant.OILFRAGMENT_RETURN_DATE:
                strDate= msg.getMsg()+strDate.substring(11);
                break;
            case GlobalConstant.OILFRAGMENT_RETURN_TIME:
                strDate=strDate.substring(0,13)+msg.getMsg();
                break;
        }
        iaeDate.setText(strDate);

    }



}
