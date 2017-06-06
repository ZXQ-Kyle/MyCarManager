package com.kyle.mycar.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kyle.mycar.Bean.MsgMainFragment;
import com.kyle.mycar.MyUtils.MyDateUtils;
import com.kyle.mycar.R;
import com.kyle.mycar.View.NumberAnimTextView;
import com.kyle.mycar.db.Dao.OilDao;
import com.kyle.mycar.db.Dao.RecordDao;
import com.kyle.mycar.db.Table.Oil;
import com.kyle.mycar.db.Table.Record;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OilDetailFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener, View.OnClickListener,
        DialogInterface.OnClickListener{


    @BindView(R.id.tv_money_oil_detail)
    NumberAnimTextView tvToatlMoney;
    @BindView(R.id.tv_odo_oil_detail)
    TextView tvOdo;
    @BindView(R.id.tv_date_oil_detail)
    TextView tvDate;
    @BindView(R.id.tv_oil_type_oil_detail)
    TextView tvOilType;
    @BindView(R.id.tv_oil_money_oil_detail)
    TextView tvOilMoney;
    @BindView(R.id.tv_oil_price_oil_detail)
    TextView tvOilPrice;
    @BindView(R.id.tv_oil_quantity_oil_detail)
    TextView tvOilQuantity;
    @BindView(R.id.tv_fuelC_oil_detail)
    TextView tvFuelC;
    @BindView(R.id.tv_perKm_oil_detail)
    TextView tvPerKm;
    @BindView(R.id.iv_pic_oil_detail)
    ImageView ivPic;
    Unbinder unbinder;

    public Record mRecord;


    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_oil_detail, null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.oil, 2, R.menu.toolbar_detail, this);
        setDate();
    }

    public void setDate() {
        if (null!= mRecord){
            Oil oil=mRecord.getOil();
            String money = oil.getMoney();
            tvToatlMoney.setNumberString(money);
            tvToatlMoney.setPrefixString(getResources().getString(R.string.RMB));
            tvToatlMoney.setDuration(1000);

            tvOdo.setText(oil.getOdometer()+getString(R.string.km));
            tvDate.setText(MyDateUtils.longToStr(oil.getDate()));

            tvOilType.setText(oil.getOilType());
            tvOilMoney.setText(money+"\n(元)");
            tvOilPrice.setText(oil.getPrice()+"\n(元/L)");
            tvOilQuantity.setText(oil.getQuantity()+"\n(L)");

            String fuelC = oil.getFuelC();
            if (TextUtils.isEmpty(fuelC)){
                tvFuelC.setText("N/A");
                Drawable drawable = getResources().getDrawable(R.drawable.warning);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumWidth());
                tvFuelC.setCompoundDrawables(drawable,null,null,null);
                tvFuelC.setCompoundDrawablePadding(8);
                tvFuelC.setOnClickListener(this);
            }else {
                tvFuelC.setText(fuelC);
            }
            String perKm = oil.getPricePerKm();
            if (TextUtils.isEmpty(perKm)){
                tvPerKm.setText("N/A");
            }else {
                tvPerKm.setText(perKm);
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle(R.string.delete)
                        .setIcon(R.drawable.ic_delete_pressed)
                        .setMessage(R.string
                                .setting_oiltype_dialog_msg)
                        .setPositiveButton(R.string.confirm, this)
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                break;
            case R.id.menu_update:
                OilFragment fragment1 = new OilFragment();
                fragment1.mOil = mRecord.getOil();
                fragment1.mRecord = mRecord;
                mActivity.switchFrag(this, fragment1, false, null);
                break;

        }
        return true;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(R.string.tips)
                .setMessage(R.string.tips_msg).setPositiveButton(R.string.confirm,null).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Logger.d(which);
        if (which==-1){
            //删除逻辑
            Oil oil = mRecord.getOil();
            oil.setDelete(true);
            OilDao.getInstance(mActivity.getApplicationContext()).update(oil);

            mRecord.setDelete(true);
            RecordDao.getInstance(mActivity.getApplicationContext()).update(mRecord);
            Toast.makeText(mActivity.getApplicationContext(), R.string.delete_sucess, Toast.LENGTH_SHORT)
                    .show();
            EventBus.getDefault().post(new MsgMainFragment(MsgMainFragment.DETAIL_DELETE_OIL,mRecord));
            mActivity.onBackPressed();
            }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MsgMainFragment msg){
        if (msg.getFlag()==MsgMainFragment.UPDATE_AN_OLD_DATA){
            mRecord=msg.getRecord();
            setDate();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tvFuelC.getWindowToken(),0);
    }
}
