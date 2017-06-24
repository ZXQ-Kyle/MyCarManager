package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kyle.mycar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingCarFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener {

    @Override
    public View initView() {
        noEventBus=true;
        return View.inflate(mActivity,R.layout.fragment_setting_car,null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.car_manager,2,R.menu.toolbar_confirm,this);

        initDb();
    }

    private void initDb() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()==R.id.menu_confirm){
            //保存逻辑（单车辆）


            return true;
        }
        return false;
    }
}
