package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kyle.mycar.R;
import com.kyle.mycar.db.DbOpenHelper;
import com.kyle.mycar.db.Table.Oil;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.tv)
    TextView tv;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.fragment_main, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void initData() {
        try {
            tv.setText(DbOpenHelper.getInstance(mActivity).getDao(Oil.class).toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
