package com.kyle.mycar.Fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackuhan.flowlayouttags.FlowlayoutTags;
import com.kyle.mycar.R;
import com.kyle.mycar.View.ImgAndEtView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @BindView(R.id.tags_mt)
    FlowlayoutTags tagsMt;
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

        //初始化Tags
        initTags();


    }

    private void initTags() {
        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.about));
        list.add(getString(R.string.maintenance));
        list.add(getString(R.string.confirm));
        list.add(getString(R.string.hello_blank_fragment));
        list.add(getString(R.string.action_settings));
        list.add(getString(R.string.navigation_drawer_close));
        list.add(getString(R.string.oil));
        refreshCategorys(tagsMt, list);
        tagsMt.setOnTagChangeListener(new FlowlayoutTags.OnTagChangeListener() {
            @Override
            public void onAppend(FlowlayoutTags flowlayoutTags, String tag) {
                Snackbar.make(getView(), "onAppend" + tag, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onDelete(FlowlayoutTags flowlayoutTags, String tag) {
                Snackbar.make(getView(), "onDelete" + tag, Snackbar.LENGTH_LONG).show();
            }
        });
        tagsMt.setOnTagClickListener(new FlowlayoutTags.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {

                String[] strings = tagsMt.getCheckedTagsText();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < strings.length; i++) {
                    sb.append(strings[i]).append(",");
                }
                Snackbar.make(getView(), "onTagClick:" + sb.toString(), Snackbar.LENGTH_LONG).show();
            }
        });
//        list.add("haha");
        tagsMt.appendTag("haha");
//        tagsMt.
    }

    public void refreshCategorys(FlowlayoutTags flowlayoutTags, List<String> list) {
        flowlayoutTags.removeAllViews();

        flowlayoutTags.setTags(list);
        flowlayoutTags.setTagsUncheckedColorAnimal(false);

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
