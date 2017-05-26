package com.kyle.mycar.Fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.j256.ormlite.misc.TransactionManager;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.MainActivity;
import com.kyle.mycar.MyUtils.CalcUtils;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.MyDateUtils;
import com.kyle.mycar.MyUtils.SpUtils;
import com.kyle.mycar.R;
import com.kyle.mycar.View.ImgAndEtView;
import com.kyle.mycar.db.Dao.OilDao;
import com.kyle.mycar.db.Dao.OilTypeDao;
import com.kyle.mycar.db.Dao.RecordDao;
import com.kyle.mycar.db.DbOpenHelper;
import com.kyle.mycar.db.Table.Oil;
import com.kyle.mycar.db.Table.OilType;
import com.kyle.mycar.db.Table.Record;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 */
public class OilFragment extends BaseFragment {

    private static final String OIL_PRICE = "oil_price";
    private static final String OIL_TYPE = "oil_type";
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
    @BindView(R.id.et_oil_price)
    EditText etOilPrice;
    @BindView(R.id.et_oil_quantity)
    EditText etOilQuantity;
    @BindView(R.id.iae_note)
    ImgAndEtView iaeNote;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.cb_oil_is_full)
    CheckBox cbOilIsFull;
    @BindView(R.id.cb_oil_forget_last)
    CheckBox cbOilForgetLast;
    private String mDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = View.inflate(mActivity, R.layout.fragment_oil, null);
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
    public void initData() {
        //spinner初始化
        OilTypeDao typeDao = OilTypeDao.getInstance(mActivity);
        Log.i("---", "typeDao: " + typeDao.toString());
        List<OilType> list = typeDao.queryAllButIsDelete("id", true);
        ArrayList<String> spinnerStr = new ArrayList<>();
        for (OilType type : list) {
            spinnerStr.add(type.getOilType());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, spinnerStr);
        adapter.setDropDownViewResource(R.layout.item_spinner_select_dialog);
        spinnerOil.setAdapter(adapter);
        //读取上次保存的汽油种类并设置
        String oilType = SpUtils.getString(mActivity.getApplicationContext(), OIL_TYPE);
        if (!TextUtils.isEmpty(oilType)) {
            spinnerOil.setSelection(spinnerStr.indexOf(oilType));
        }
        //读取上次保存的汽油价格
        String price = SpUtils.getString(mActivity.getApplicationContext(), OIL_PRICE);
        if (!TextUtils.isEmpty(oilType)) {
            etOilPrice.setText(price);
        }

        //界面初始化
        iaeNote.setSingleLine();
        iaeNote.setTextAlg(View.TEXT_ALIGNMENT_TEXT_START);
        iaeOdometer.setInputTypeOfNumber();

        //设置日期默认为当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        Date date = new Date(System.currentTimeMillis());
        mDate = sdf.format(date);
        iaeDate.setText(mDate);

        //初始化默认值
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = etOilMoney.getText().toString();
                String s2 = etOilPrice.getText().toString();
                if (!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)) {
                    BigDecimal money = new BigDecimal(s1);
                    BigDecimal price = new BigDecimal(s2);
                    BigDecimal quantity = money.divide(price, 2, BigDecimal.ROUND_HALF_UP);
                    etOilQuantity.setText(String.valueOf(quantity));
                }
            }
        };
        etOilMoney.addTextChangedListener(textWatcher);
        etOilPrice.addTextChangedListener(textWatcher);

    }


    private void showDatePicker() {
        DatePickerDialogFragment dialogFragment = DatePickerDialogFragment.newInstance(MyConstant
                .OIL_FRAGMENT_RETURN_DATE, MyConstant.OIL_FRAGMENT_RETURN_TIME);
        dialogFragment.show(getFragmentManager(), "oilDate");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDateMessage(MessageEvent msg) {

        switch (msg.getFlag()) {
            case MyConstant.OIL_FRAGMENT_RETURN_DATE:
                mDate = msg.getMsg() + mDate.substring(11);
                break;
            case MyConstant.OIL_FRAGMENT_RETURN_TIME:
                mDate = mDate.substring(0, 13) + msg.getMsg();
                break;
        }
        iaeDate.setText(mDate);
    }


    //视图点击事件
    @OnClick({R.id.iae_date, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iae_date:
                showDatePicker();
                break;
            case R.id.btn_confirm:
                new Thread() {
                    @Override
                    public void run() {
                        Logger.d(System.currentTimeMillis());
                        saveData();
                        Logger.d(System.currentTimeMillis());

                        getFragmentManager().beginTransaction().hide(getFragmentManager().findFragmentByTag
                                (MainActivity.OILFRAGMENT)).
                                show(getFragmentManager().findFragmentByTag(MainActivity.MAIN_FRAGMENT)).commit();

                        EventBus.getDefault().post(new MsgMainFragment(MsgMainFragment.UPDATE_AN_NEW_ONE_DATA));
                    }
                }.start();

                break;
        }
    }

    private void saveData() {
        //事务保存
        TransactionManager manager = new TransactionManager(DbOpenHelper.getInstance(mActivity).getConnectionSource());
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                transactionSave();
                return true;
            }
        };
        try {
            manager.callInTransaction(callable);
            if (getActivity() != null) {
                Snackbar.make(getView(), getString(R.string.sucess), Snackbar.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (getActivity() != null) {
                Snackbar.make(getView(), getString(R.string.fail), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void transactionSave() {
        long date = MyDateUtils.strToLong(mDate);
        String OdometerText = iaeOdometer.getText();
        String oilType = spinnerOil.getSelectedItem().toString();
        String oilMoney = etOilMoney.getText().toString();
        String oilPrice = etOilPrice.getText().toString();
        String oilQuantity = etOilQuantity.getText().toString();
        boolean isFullChecked = cbOilIsFull.isChecked();
        boolean isForgetLastChecked = cbOilForgetLast.isChecked();
        String noteText = iaeNote.getText();
        String fuel = null;
        String pricePerKm = null;
        //保存数据，下次进入自动读取
        SpUtils.putSring(mActivity.getApplicationContext(), OIL_PRICE, oilPrice);
        SpUtils.putSring(mActivity.getApplicationContext(), OIL_TYPE, oilType);
        OilDao oilDao = OilDao.getInstance(mActivity);
        long l = oilDao.countOf();
        //如果加满油计算油耗，并把此次id保存，以便下次读取
        if (isFullChecked && !isForgetLastChecked) {
            List<Oil> list1 = oilDao.queryNewest("isFull", true);
            Logger.d(list1);
            if (list1.size() > 0) {
                Oil oilPre = list1.get(0);
                int preId = oilPre.getId();
                String odometer = oilPre.getOdometer();
                //实际里程值
                BigDecimal odo = CalcUtils.reduceBigDecimal(OdometerText, odometer);
                //获取燃油消耗（L）和sumMoney
                BigDecimal quantity = new BigDecimal(oilQuantity);
                BigDecimal sumMoney = new BigDecimal(oilMoney);
                List<Oil> list = oilDao.queryBetween("id", preId + 1, l);
                if (list.size() > 0) {
                    for (Oil oil : list) {
                        quantity = CalcUtils.appendBigDecimal(quantity, oil.getQuantity());
                        sumMoney = CalcUtils.appendBigDecimal(sumMoney, oil.getMoney());
                    }
                }
                //计算油耗 L/百公里和元/每公里
                BigDecimal odo100 = odo.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                fuel = (quantity.divide(odo100, 2, BigDecimal.ROUND_HALF_UP)).toString();

                pricePerKm = (sumMoney.divide(odo, 2, BigDecimal.ROUND_HALF_UP)).toString();
            }

        }
        Oil oil = new Oil(date, oilMoney, oilPrice, oilQuantity, OdometerText, oilType, isFullChecked,
                isForgetLastChecked, pricePerKm, fuel);
        int add = oilDao.add(oil);
        Logger.d(oil);
        RecordDao dao = RecordDao.getInstance(mActivity);
        dao.add(new Record(oil, date));
    }


}
