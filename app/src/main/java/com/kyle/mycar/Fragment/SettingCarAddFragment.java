package com.kyle.mycar.Fragment;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.kyle.mycar.Bean.MsgSetting;
import com.kyle.mycar.MyUtils.MyConstant;
import com.kyle.mycar.MyUtils.SpUtils;
import com.kyle.mycar.R;
import com.kyle.mycar.db.Dao.CarBrandDao;
import com.kyle.mycar.db.Dao.CarTypeDao;
import com.kyle.mycar.db.Table.CarBrand;
import com.kyle.mycar.db.Table.CarType;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingCarAddFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener, AdapterView
        .OnItemSelectedListener {

    @BindView(R.id.spinner_brand)
    Spinner spinnerBrand;
    @BindView(R.id.spinner_type)
    Spinner spinnerType;
    Unbinder unbinder;

    private MyAdapter mAdapter;
    private List<CarType> carTypeList;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_setting_car_add, null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.car_manager, 2, R.menu.toolbar_confirm, this);
        mActivity.mThreadPool.execute(new getBrand(mActivity));
        spinnerBrand.setOnItemSelectedListener(this);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_confirm) {
            //保存逻辑（单车辆）
            String carBrand = (String) spinnerBrand.getSelectedItem();
            CarType carType = (CarType) spinnerType.getSelectedItem();
            SpUtils.putSring(mActivity,MyConstant.CAR_BRAND,carBrand);
            SpUtils.putSring(mActivity,MyConstant.CAR_TYPE,carType.type);
            mActivity.onBackPressed();
            return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MsgSetting msg) {
        switch (msg.flag) {
            case MyConstant.SETTING_CAR_BRAND:
                ArrayList<String> strings = (ArrayList<String>) msg.object;

                ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item,
                        strings);
                adapter.setDropDownViewResource(R.layout.item_spinner_select_dialog);
                spinnerBrand.setAdapter(adapter);

                break;
            case MyConstant.SETTING_CAR_TYPE:
                carTypeList = (List<CarType>) msg.object;
                if (null != carTypeList) {
                    if (mAdapter==null){
                        mAdapter = new MyAdapter(mActivity, carTypeList);
                        spinnerType.setAdapter(mAdapter);
                    }else {
                        mAdapter.setNewData(carTypeList);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                break;
        }
    }

    //spinnerBrand.setOnItemSelectedListener(this);
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Logger.d("onItemSelected--->position--->" + position);
        mActivity.mThreadPool.execute(new getCarType(mActivity, position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private static class getBrand implements Runnable {
        private Context mContext;

        public getBrand(Context context) {
            mContext = context.getApplicationContext();
        }

        @Override
        public void run() {
            CarBrandDao dao = CarBrandDao.getInstance(mContext);
            List<CarBrand> list = dao.queryAll();

            ArrayList<String> strBrand = new ArrayList<>();
            for (CarBrand c : list) {
                strBrand.add(c.brand);
            }

            EventBus.getDefault().post(new MsgSetting(MyConstant.SETTING_CAR_BRAND, strBrand));
        }
    }

    private static class getCarType implements Runnable {
        private Context mContext;
        private int mPosition;

        public getCarType(Context context, int position) {
            mContext = context.getApplicationContext();
            mPosition = position + 1;
        }

        @Override
        public void run() {
            CarBrandDao brandDao = CarBrandDao.getInstance(mContext);
            List<CarBrand> brands = brandDao.query("id", mPosition);
            List<CarType> list = null;

            if (brands.size() == 1) {
                CarTypeDao dao = CarTypeDao.getInstance(mContext);
                list = dao.query("CarBrand", brands.get(0));
            }
            EventBus.getDefault().post(new MsgSetting(MyConstant.SETTING_CAR_TYPE, list));
        }
    }

    private static class MyAdapter extends BaseAdapter implements SpinnerAdapter {

        private List<CarType> mList;
        private Context mContext;

        public MyAdapter(Context context, List<CarType> list) {
            mContext = context.getApplicationContext();
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_spinner_item, parent,
                        false);
            }
            TextView view = (TextView) convertView;
            view.setText(mList.get(position).type);
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            VH vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_multi_spinner_select_dialog,
                        parent, false);
                vh = new VH(convertView);
                convertView.setTag(vh);
            } else {
                vh = (VH) convertView.getTag();
            }
            CarType carType = mList.get(position);
            vh.tv1.setText(carType.car);
            vh.tv2.setText(carType.type);

            if ((position%2)==1) {
                vh.ll.setBackgroundColor(mContext.getResources().getColor(R.color.BgGray));
            }else {
                vh.ll.setBackgroundColor(0);
            }
            return convertView;
        }

        public void setNewData(List<CarType> list) {
            mList = list;
        }
    }

    private static class VH {
        private TextView tv1;
        private TextView tv2;
        private LinearLayout ll;

        public VH(View view) {
            tv1 = (TextView) view.findViewById(R.id.spinne_tv1);
            tv2 = (TextView) view.findViewById(R.id.spinne_tv2);
            ll = (LinearLayout) view.findViewById(R.id.ll_root);
        }

    }

}
