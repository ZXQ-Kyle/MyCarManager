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
public class OilDetailFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener {


    @Override
    public View initView() {
        return View.inflate(mActivity,R.layout.fragment_oil_detail,null);
    }

    @Override
    public void initData() {
        initToolbar(R.string.oil,2,R.menu.toolbar_detail,this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_delete:

                break;
            case R.id.menu_update:

                break;

        }
        return true;
    }
}
