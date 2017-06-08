package com.kyle.mycar.Fragment;

import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.j256.ormlite.misc.TransactionManager;
import com.kyle.mycar.Bean.MessageEvent;
import com.kyle.mycar.Bean.MsgMainFragment;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 */
public class OilFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener, TextWatcher {

    private static final String OIL_PRICE = "oil_price";
    private static final String OIL_TYPE = "oil_type";
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
    @BindView(R.id.et_oil_money)
    EditText etOilMoney;
    @BindView(R.id.et_oil_price)
    EditText etOilPrice;
    @BindView(R.id.et_oil_quantity)
    EditText etOilQuantity;
    @BindView(R.id.iae_note)
    ImgAndEtView iaeNote;
    @BindView(R.id.cb_oil_is_full)
    CheckBox cbOilIsFull;
    @BindView(R.id.cb_oil_forget_last)
    CheckBox cbOilForgetLast;


    private String mDate;
    public Oil mOil;
    public Record mRecord;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_oil, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initData() {
        initToolbar(R.string.oil, 2, R.menu.toolbar_confirm, this);
        //spinner初始化
        OilTypeDao typeDao = OilTypeDao.getInstance(mActivity);
        List<OilType> list = typeDao.queryAllButIsDelete("id", true);
        ArrayList<String> spinnerStr = new ArrayList<>();
        for (OilType type : list) {
            spinnerStr.add(type.getOilType());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, spinnerStr);
        adapter.setDropDownViewResource(R.layout.item_spinner_select_dialog);
        spinnerOil.setAdapter(adapter);
        if (mOil == null) {
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
        etOilMoney.addTextChangedListener(this);
        etOilPrice.addTextChangedListener(this);

        iaeOdometer.rqFocus();
        mActivity.showKeyboard(iaeOdometer);

        if (mOil != null) {
            mDate = MyDateUtils.longToStr(mOil.getDate());
            iaeDate.setText(mDate);
            iaeOdometer.setText(mOil.getOdometer());
            int indexOf = spinnerStr.indexOf(mOil.getOilType());
            if (indexOf >= 0) {
                spinnerOil.setSelection(indexOf);
            } else {
                spinnerOil.setSelection(0);
            }
            etOilMoney.setText(mOil.getMoney());
            etOilPrice.setText(mOil.getPrice());
            etOilQuantity.setText(mOil.getQuantity());
            cbOilIsFull.setChecked(mOil.isFull());
            cbOilForgetLast.setChecked(mOil.isForgetLast());
//            iaeNote.setText(oil.getNote());
        }
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
    @OnClick({R.id.iae_date})
    public void onViewClicked(View view) {
        showDatePicker();
    }

    private void saveData() {
        //事务保存
        TransactionManager manager = new TransactionManager(DbOpenHelper.getInstance(mActivity).getConnectionSource());
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                save();
                return true;
            }
        };
        try {
            manager.callInTransaction(callable);
//            Toast.makeText(mActivity, R.string.sucess, Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
//            Toast.makeText(mActivity, R.string.fail, Toast.LENGTH_LONG).show();
        }
    }

    private void save() {
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

        if (null == mOil) {
            //保存数据，下次进入自动读取
            SpUtils.putSring(mActivity.getApplicationContext(), OIL_PRICE, oilPrice);
            SpUtils.putSring(mActivity.getApplicationContext(), OIL_TYPE, oilType);
        }

        OilDao oilDao = OilDao.getInstance(mActivity);
        long l = oilDao.countOf();
        //如果加满油计算油耗
        if (isFullChecked && !isForgetLastChecked) {
            List<Oil> list1 = null;
            if (null == mOil) {
                list1 = oilDao.queryNewestLt("date", date, 1);
            } else {
                list1 = oilDao.queryNewestLt("date", mOil.getDate(), 1);
            }
            if (list1.size() > 0) {
                Oil oilPre = list1.get(0);
                int preId = oilPre.getId();
                String odometer = oilPre.getOdometer();
                //实际里程值
                BigDecimal odo = CalcUtils.reduceBigDecimal(OdometerText, odometer);
                //获取燃油消耗（L）和sumMoney
                BigDecimal quantity = new BigDecimal(oilQuantity);
                BigDecimal sumMoney = new BigDecimal(oilMoney);
                List<Oil> list = null;
                if (null == mOil) {

                    list = oilDao.queryBetween("date", oilPre.getDate() + 1, date - 1);
                } else {
                    list = oilDao.queryBetween("date", oilPre.getDate() + 1, mOil.getDate() - 1);
                }

                if (null != list && list.size() > 0) {
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
        if (null == mOil) {
            //新建数据
            Oil oil = new Oil(date, oilMoney, oilPrice, oilQuantity, OdometerText, oilType, isFullChecked,
                    isForgetLastChecked, pricePerKm, fuel);
            oilDao.add(oil);
            RecordDao dao = RecordDao.getInstance(mActivity);
            mRecord = new Record(oil, date);
            dao.add(mRecord);
        } else {
            //更新数据但是不用更新record
            mOil.update(date, oilMoney, oilPrice, oilQuantity, OdometerText, oilType, isFullChecked,
                    isForgetLastChecked, pricePerKm, fuel);
            oilDao.update(mOil);
            if (mRecord.getDate() != date) {
                //需要更新record
                mRecord.setDate(date);
                RecordDao.getInstance(mActivity.getApplicationContext()).update(mRecord);
            }

        }

    }

    //toolbar menu点击事件
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_confirm) {
            String odometer = iaeOdometer.getText();
            if (TextUtils.isEmpty(odometer)) {
                Toast.makeText(mActivity.getApplicationContext(), R.string.err_empty_odo, Toast.LENGTH_LONG).show();
                iaeOdometer.rqFocus();
                mActivity.showKeyboard(iaeOdometer);
                return true;
            }
            odometer=null;
            String money = etOilMoney.getText().toString().trim();
            if (TextUtils.isEmpty(money)) {
                Toast.makeText(mActivity.getApplicationContext(), R.string.err_empty_money, Toast.LENGTH_LONG).show();
                etOilMoney.requestFocus();
                mActivity.showKeyboard(etOilMoney);
                return true;
            }
            money=null;

            mActivity.mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    saveData();
                    if (null == mOil) {
                        //新建数据
                        EventBus.getDefault().post(new MsgMainFragment(MsgMainFragment.UPDATE_AN_NEW_ONE_DATA,
                                mRecord));
                    } else {
                        //更新数据
                        EventBus.getDefault().post(new MsgMainFragment(MsgMainFragment.UPDATE_AN_OLD_DATA, mRecord));
                    }

//                    mActivity.switchFrag(OilFragment.class, MainFragment.class, true);
                    mActivity.onBackPressed();
                }
            });
            return true;
        }
        return false;
    }


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
}
