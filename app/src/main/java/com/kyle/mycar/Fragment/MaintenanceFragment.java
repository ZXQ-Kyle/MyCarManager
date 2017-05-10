package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kyle.mycar.R;
import com.kyle.mycar.View.ImgAndEtView;
import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaintenanceFragment extends BaseFragment {


    @BindView(R.id.iae_mt_date)
    ImgAndEtView iaeMtDate;
    @BindView(R.id.iae_mt_odometer)
    ImgAndEtView iaeMtOdometer;
    @BindView(R.id.iae_mt_money)
    ImgAndEtView iaeMtMoney;
    Unbinder unbinder;
    private String mDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_maintenance, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        //设置日期默认为当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        Date date = new Date(System.currentTimeMillis());
        mDate = sdf.format(date);
        iaeMtDate.setText(mDate);

        initTags();

        //初始化Tags
    }

    private void initTags() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    @Override
//    public void onTagClick(String tag) {
//        Toast.makeText(mActivity, tag, Toast.LENGTH_SHORT).show();
//        Log.i("aa", "onTagClick: "+tag);
//    }

}
